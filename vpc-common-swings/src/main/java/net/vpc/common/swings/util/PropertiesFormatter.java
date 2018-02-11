/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.swings.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 *
 * @author vpc
 */
public final class PropertiesFormatter {

    private PropertyFormat format;
    private boolean ignoreNotFound = false;
    private String notFoundValue = "";
    private boolean notFoundKey = true;
    private boolean regexp = false;
    private boolean useReferences = true;
    private int maxNested = 10;
    private Mapper<String, String> properties;

    public PropertiesFormatter(PropertyFormat format) {
        this.format = format;
    }

    public PropertiesFormatter(PropertyFormat format,Mapper<String, String> properties) {
        this.format = format;
        setProperties(properties);
    }

    public PropertiesFormatter(PropertyFormat format,Map<String, String> properties) {
        this.format = format;
        setProperties(properties);
    }

    public PropertiesFormatter(PropertyFormat format,Properties properties) {
        this.format = format;
        setProperties(properties);
    }

    public Mapper<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Mapper<String, String> properties) {
        if (properties == null) {
            throw new NullPointerException();
        }
        this.properties = properties;
    }

    public void setProperties(Map<String, String> properties) {
        if (properties == null) {
            throw new NullPointerException();
        }
        this.properties = new DefaultMapper<String, String>(properties);
    }

    public void setProperties(Properties properties) {
        if (properties == null) {
            throw new NullPointerException();
        }
        Map<String, String> m = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Object k = entry.getKey();
            Object v = entry.getValue();
            String ks = k == null ? null : String.valueOf(k);
            String vs = v == null ? null : String.valueOf(v);
            m.put(ks, vs);
        }
        this.properties = new DefaultMapper<String, String>(m);
    }

    public String format(String str) {
        switch (format) {
            case UNIX: {
                return formatUnix(str, properties, useReferences, regexp, ignoreNotFound, maxNested);
            }
            case BRACES: {
                return formatBraces(str, properties, useReferences, regexp, ignoreNotFound, maxNested);
            }
        }
        throw new IllegalArgumentException("Not Supported");
    }

    private String formatUnix(String str, Mapper<String, String> properties, boolean useReferences, boolean regexp, boolean ignoreKeysNotFound, int maxNested) {
        if (!useReferences || str == null || str.length() == 0) {
            return str;
        }
        if (regexp) {
            throw new IllegalArgumentException("Not yet supported");
        }

        String current = str;
        StringBuilder sb = new StringBuilder();
        int len = current.length();
        for (int i = 0; i < len; i++) {
            char c = current.charAt(i);
            if (c == '$' && i < len - 1 && current.charAt(i + 1) == '{') {
                StringBuilder k = new StringBuilder();
                i = i + 2;
                while (i < len) {
                    c = current.charAt(i);
                    if (c == '}') {
                        break;
                    } else {
                        k.append(c);
                        i++;
                    }
                }
                String kstr = k.toString();
                String v = properties.get(kstr);
                if (v == null) {
                    if (!ignoreKeysNotFound) {
                        throw new NoSuchElementException(kstr);
                    }
                    if (notFoundKey) {
                        sb.append("${").append(k).append("}");
                    } else {
                        sb.append(notFoundValue);
                    }
                } else {
                    if (useReferences && maxNested > 0) {
                        v = formatUnix(v, properties, useReferences, regexp, ignoreKeysNotFound, maxNested - 1);
                    }
                    sb.append(v);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String formatBraces(String str, Mapper<String, String> properties, boolean useReferences, boolean regexp, boolean ignoreKeysNotFound, int maxNested) {
        if (!regexp) {
            BracketsTokenizer bracketsTokenizer = new BracketsTokenizer(str, "{}");
            StringBuilder sb = new StringBuilder();
            while (bracketsTokenizer.hasNext()) {
                SubString token = bracketsTokenizer.next();
                switch (token.getType()) {
                    case 0: { // '{...}'
                        String key = token.middle(1, 1);
                        if (key.indexOf(' ') >= 0) {
                            //jdbc escape???
                            //TODO !!! revove this from here ==> change all jdbc calls to a clone of this function
                            sb.append(token.toString());
                        } else {
                            String v = properties.get(key);
                            if (v != null) {
                                if (useReferences && maxNested > 0) {
                                    v = formatBraces(str, properties, useReferences, regexp, ignoreKeysNotFound, maxNested - 1);
                                }
                                sb.append(v);
                            } else if (!ignoreKeysNotFound) {
                                throw new NoSuchElementException(key);
                            } else {
                                if (notFoundKey) {
                                    sb.append("{").append(key).append("}");
                                } else {
                                    sb.append(notFoundValue);
                                }
                            }
                        }
                        break;
                    }
                    default: {// ... simple code
                        sb.append(token.toString());
                        break;
                    }

                }
            }
            return sb.toString();
        } else {
            BracketsTokenizer bracketsTokenizer = new BracketsTokenizer(str, "{}[]");
            StringBuilder sb = new StringBuilder();
            ArrayList all_tokens = new ArrayList();
            boolean look_a_head = false;
            boolean last_failed = false;
            while (bracketsTokenizer.hasNext()) {
                SubString token = bracketsTokenizer.next();
                all_tokens.add(token);
                switch (token.getType()) {
                    case -1: {// ... simple code
                        if (all_tokens.size() > 1 && "|".equals(token.toString()) && ((SubString) all_tokens.get(all_tokens.size() - 2)).getType() == 1) {
                            look_a_head = true;
                        } else {
                            look_a_head = false;
                            sb.append(token.toString());
                        }
                        break;
                    }
                    case 0: { // '{...}'
                        if (look_a_head) {
                            if (all_tokens.size() > 1 && "|".equals(all_tokens.get(all_tokens.size() - 2).toString())) {
                                sb.append("|");
                            }
                            look_a_head = false;
                        }
                        String key = token.middle(1, 1);

                        String v = properties.get(key);
                        if (v != null) {
                            if (maxNested != 0) {
                                v = formatBraces(str, properties, useReferences, regexp, ignoreKeysNotFound, maxNested - 1);
                            }
                            sb.append(v);
                        } else if (!ignoreKeysNotFound) {
                            throw new NoSuchElementException(key);
                        } else {
                            if (notFoundKey) {
                                sb.append("{").append(key).append("}");
                            } else {
                                sb.append(notFoundValue);
                            }
                        }
                        break;
                    }
                    case 1: { // '[...]'
                        if (look_a_head && all_tokens.size() > 1 && !"|".equals(all_tokens.get(all_tokens.size() - 2).toString())) {
                            look_a_head = false;
                        }
                        String new_str = token.middle(1, 1);
                        String toAppenString = "";
                        boolean old_last_failed = last_failed;
                        try {
                            toAppenString = formatBraces(new_str, properties, useReferences, false, false, maxNested - 1);
                            last_failed = false;
                        } catch (NoSuchElementException e) {
                            last_failed = true;
                            // do nothing
                        }
                        if (!look_a_head || (look_a_head && old_last_failed)) {
                            sb.append(toAppenString);
                        }
                        break;
                    }
                }
            }
            return sb.toString();
        }
    }
}

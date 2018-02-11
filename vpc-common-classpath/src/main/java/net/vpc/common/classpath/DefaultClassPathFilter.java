/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.classpath;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Taha BEN SALAH <taha.bensalah@gmail.com>
 */
public class DefaultClassPathFilter implements ClassPathFilter {

    private static final Logger log = Logger.getLogger(DefaultClassPathFilter.class.getName());

    private boolean emptyResult = false;
    private List<DefaultConfigFilterItem> items = new ArrayList<DefaultConfigFilterItem>();
    private Map<URL, List<DefaultConfigFilterItem>> cache = new HashMap<URL, List<DefaultConfigFilterItem>>();
    
    public DefaultClassPathFilter() {

    }

    public DefaultClassPathFilter(ScanFilter[] filterList) {
        for (ScanFilter filter : filterList) {
            add(filter);
        }
    }

    public List<DefaultConfigFilterItem> getDefaultConfigFilterItem(URL url) {
        List<DefaultConfigFilterItem> found = cache.get(url);
        if (found == null) {
            found = new ArrayList<DefaultConfigFilterItem>();
            for (DefaultConfigFilterItem defaultConfigFilterItem : items) {
                if (defaultConfigFilterItem.getLibFilter().accept(url)) {
                    PatternListClassNameFilter p = defaultConfigFilterItem.getTypeFilter();
                    if (p.getUserPatterns().length == 0) {
                        found.add(defaultConfigFilterItem);
                    } else {
                        for (String pattern : p.getUserPatterns()) {
                            StringBuilder prefix = new StringBuilder();
                            char[] patternCharArray = pattern.toCharArray();
                            int x = 0;
                            boolean exitWhile = false;
                            while (!exitWhile && x < patternCharArray.length) {
                                char c = patternCharArray[x];
                                switch (c) {
                                    case '*':
                                    case '?': {
                                        if (prefix.length() > 0 && prefix.toString().endsWith(".")) {
                                            //okkay
                                        } else {
                                            while (prefix.length() > 0 && !prefix.toString().endsWith(".")) {
                                                prefix.deleteCharAt(prefix.length() - 1);
                                            }
                                        }
                                        exitWhile = true;
                                        break;
                                    }
                                    default: {
                                        prefix.append(patternCharArray[x]);
                                        break;
                                    }
                                }
                                x++;
                            }
                            if (prefix.length() > 0) {
                                String rr = prefix.toString().replace('.', '/');
                                ClassPathRoot cr = new ClassPathRoot(url);
                                try {
                                    if (cr.contains(rr)) {
                                        found.add(defaultConfigFilterItem);
                                        break;
                                    }
                                } catch (Exception e) {
                                    log.log(Level.SEVERE, null, e);
                                }
                            } else {
                                found.add(defaultConfigFilterItem);
                                break;
                            }
                        }
                    }

                }
            }
            cache.put(url, found);
        }
        return found;
    }

    public boolean acceptLibrary(URL url) {
        List<DefaultConfigFilterItem> found = getDefaultConfigFilterItem(url);
        if (found.size() == 0) {
            if (items.size() == 0) {
                return emptyResult;
            }
            return false;
        }
        return true;
    }

    public boolean acceptClassName(URL url, String className, boolean anonymous) {
        List<DefaultConfigFilterItem> item = getDefaultConfigFilterItem(url);
        for (DefaultConfigFilterItem y : item) {
            if (y.getTypeFilter().accept(className)) {
                return true;
            }
        }
        if (items.size() == 0) {
            return emptyResult;
        }
        return false;
    }

    public boolean acceptClass(URL url, String className, boolean anonymous, Class type) {
        return acceptClassName(url,className, anonymous);
    }

    private void add(ScanFilter filter) {
        DefaultConfigFilterItem defaultConfigFilterItem = new DefaultConfigFilterItem(new PatternListLibNameFilter(new String[]{filter.getLibs()}), new PatternListClassNameFilter(new String[]{filter.getTypes()}));
        items.add(defaultConfigFilterItem);
    }

//    public ClassNameFilter getClassNameFilter(URL url) {
//        for (DefaultConfigFilterItem defaultConfigFilterItem : items) {
//            switch (defaultConfigFilterItem.getLibFilter().accept(url)) {
//                case GRANT: {
//                    return defaultConfigFilterItem.getTypeFilter();
//                }
//                case DENY: {
//                    return null;
//                }
//            }
//        }
//        return null;//new PatternListClassNameFilter(null);
//    }
}

/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.prs.messageset;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 13 juil. 2006 22:14:21
 */
public class MessageSet extends AbstractMessageSetBundle {
    //    private static Hashtable<URL, ImageIcon> cachedIcons = new Hashtable<URL, ImageIcon>();

    private ArrayList<MessageSetBundle> bundlesVector;
    private boolean useReferences = true;
    final boolean VERBOSE = "true".equals(System.getProperty("dev.verbose.printStackTrace"));
    private MessageSetChangeListener listener = new MessageSetChangeListener() {

        public void messageSetChanged(MessageSetChangeEvent evt) {
            revalidate();
        }
    };
    private Logger logger;

    public MessageSet(LoggerProvider loggerProvider) {
        bundlesVector = new ArrayList<MessageSetBundle>(1);
        this.logger = (loggerProvider==null? LoggerProvider.DEFAULT:loggerProvider).getLogger(MessageSet.class.getName());
    }

    public MessageSet(LoggerProvider loggerProvider, MessageSetBundle... bundles) {
        this(loggerProvider);
        for (MessageSetBundle bundle : bundles) {
            addBundle(bundle);
        }
    }

//    public static ImageIcon loadImageIcon(URL imgLocation) {
//        ImageIcon o = cachedIcons.get(imgLocation);
//        if (o == null) {
//            o = new ImageIcon(imgLocation);
//            cachedIcons.put(imgLocation, o);
//        }
//        return o;
//    }
//    public static ImageIcon loadImageIcon(String imgLocation) {
//        return loadImageIcon(imgLocation, true);
//    }
//
//    public static ImageIcon loadImageIcon(String imgLocation, boolean warn) {
//        URL url = MessageSet.class.getResource(imgLocation);
//        if (url == null) {
//            iconNotFound(imgLocation, warn);
//            return null;
//        }
//        return loadImageIcon(url);
//    }
//    private static void iconNotFound(String iconLocation, boolean warn) {
//        if (warn) {
//            System.err.println("Unresolved icon location : \"" + iconLocation + "\"");
//            if ("true".equals(System.getProperty("dev.verbose.printStackTrace"))) {
//                new Throwable().printStackTrace();
//            }
//        }
//    }
    public String get(String[] keys) {
        return get(keys, keys[0], true);
    }

    public String get(String[] keys, boolean warn) {
        return get(keys, keys[0], warn);
    }

    public String get(String[] keys, String defaultValue, boolean warn) {
        for (int i = 0; i < keys.length; i++) {
            String v = get(keys[i], null, false);
            if (v != null) {
                return v;
            }
        }
        keysNotFound(keys, warn);
        return defaultValue;
    }

    private void keysNotFound(String[] keys, boolean warn) {
        if (warn) {
            if (VERBOSE) {
                logger.log(Level.WARNING, "none of " + Arrays.asList(keys) + " was not found in " + bundlesVector, new Throwable());
            } else {
                logger.log(Level.WARNING, "none of {0} was not found in {1}", new Object[]{Arrays.asList(keys), bundlesVector});
            }
        }
    }

    private void keyNotFound(String key, boolean warn) {
        if (warn) {
            if (VERBOSE) {
                logger.log(Level.WARNING, key + " was not found in " + bundlesVector, new Throwable());
            } else {
                logger.log(Level.WARNING, "{0} was not found in {1}", new Object[]{key, bundlesVector});
            }
        }
    }

    public String get(String[] keys, Object[] values) {
        return get(keys, keys[0], true, values);
    }

    public String get(String[] keys, boolean warn, Object[] values) {
        return get(keys, keys[0], warn, values);
    }

    public String get(String[] keys, String defaultPattern, boolean warn, Object[] values) {
        for (int i = 0; i < keys.length; i++) {
            String newKey = get(keys[i], null, false);
            if (newKey != null) {
                try {
                    return MessageFormat.format(newKey, values);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "MessageSet error", e);
                }
            }
        }
        keysNotFound(keys, warn);
        if (defaultPattern != null) {
            try {
                return MessageFormat.format(defaultPattern, values);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "MessageSet error", e);
            }
        }
        return defaultPattern;
    }

    public String get(String key, boolean warn) {
        if (warn) {
            return get(key, key, true);
        } else {
            return get(key, null, false);
        }

    }

    public String get(String key) {
        return get(key, key, true);
    }

    public URL getURL(String[] possibleKeys, boolean warn) {
        String goodPath = get(possibleKeys, null, warn);
        if (goodPath == null) {
            return null;
        }
        try {
            return new URL(goodPath);
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Bad URL", e);
        }
        return null;
    }

    public URL getURL(String key, boolean warn) {
        String goodPath = get(key, null, warn);
        if (goodPath == null) {
            return null;
        }
        try {
            return new URL(goodPath);
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Bad URL", e);
        }
        return null;
    }

    public String get(String key, String defaultValue, boolean warn) {
        return get(key, defaultValue, warn, useReferences);
    }

    public String getString/*NoCache*/(String key) throws MissingResourceException {
        String s = get(key, null, false);
        if (s == null) {
            throw new MissingResourceException("Unable to Find " + key, "MessageSet", s);
        }
        return s;
    }

    public String get(String key, String defaultValue, boolean warn, boolean useReferences) {
        for (int i = bundlesVector.size() - 1; i >= 0; i--) {
            try {
                String ret = (bundlesVector.get(i)).getString(key);
                if (ret != null) {
                    if (!useReferences) {
                        return ret;
                    }
                    return expandReferences(key, ret);
                }
            } catch (MissingResourceException e) {
                //
            } catch (Exception e) {
                logger.log(Level.SEVERE, "MessageSet error", e);
            }
        }
        keyNotFound(key, warn);
        if (useReferences) {
            return expandReferences(key, defaultValue);
        }
        return defaultValue;
    }

    private String expandReferences(String key, String str) {
        if (!useReferences || str == null || str.length() == 0) {
            return str;
        }
        TreeSet<String> seen = new TreeSet<String>();
        seen.add(key);
        String current = str;
        int maxNested = 10;
        int currentNested = 0;
        while (currentNested < maxNested) {
            boolean someVar = false;
            StringBuilder sb = new StringBuilder();
            int len = current.length();
            for (int i = 0; i < len; i++) {
                char c = current.charAt(i);
                if (c == '$' && i < len - 1 && current.charAt(i + 1) == '{') {
                    StringBuilder k = new StringBuilder();
                    i = i + 2;
                    someVar = true;
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
                    seen.add(kstr);
                    sb.append(get(kstr, null, true, false));
                } else {
                    sb.append(c);
                }
            }
            current = sb.toString();
            if (!someVar) {
                return current;
            }
            currentNested++;
        }
        throw new IllegalArgumentException("Recursive resource definition : " + seen);
    }

    public String get(String key, String defaultPattern, boolean defaultIsPattern, boolean warn, Object[] values) {
        String newKey = get(key, null, warn);
        if (newKey != null) {
            try {
                return MessageFormat.format(newKey, values);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "MessageSet error", e);
                //            Log.warning("MessageSet.error : " + e);
            }
        }
        if (defaultPattern != null) {
            if (defaultIsPattern) {
                try {
                    return MessageFormat.format(defaultPattern, values);
                } catch (Exception e) {
                logger.log(Level.SEVERE, "MessageSet error", e);
                    //            Log.warning("MessageSet.error : " + e);
                }
            } else {
                return defaultPattern;
            }
        }
        return defaultPattern;
    }

    public String get(String key, Object[] values) {
        return get(key, key, true, true, values);
    }

    public String get2(String key, Object... values) {
        return get(key, key, true, true, values);
    }

    public void addBundle(MessageSetBundle bundle) {
        bundle.setLocale(getLocale());
        bundle.revalidate();
        bundlesVector.add(bundle);
        //bundle.addChangeListener(listener);
        fireChanged();
    }

    public boolean addBundle(String bundleName) {
        return addBundle(bundleName, MessageSetManager.getDefaultClassLoader());
    }

    public boolean addBundle(String bundleName, ClassLoader classLoader) {
        try {
            addBundle(new MessageSetResourceBundleWrapper(bundleName, getLocale(), classLoader));
            return true;
        } catch (Exception e) {
                logger.log(Level.SEVERE, "MessageSet error", e);
//            Log.error(e.toString());
        }
        return false;
    }

    //------------------------------------------------------------------
    // ICONS
    //------------------------------------------------------------------
//    public ImageIcon getIcon(String[] possibleKeys, boolean warn) {
//        return getIcon(possibleKeys, null, warn);
//    }
//
//    public ImageIcon getIcon(String[] possibleKeys, String defaultIcon, boolean warn) {
//        String goodPath = get(possibleKeys, defaultIcon, warn);
//        if (goodPath == null) {
//            return null;
//        }
//        return loadImageIcon(goodPath, warn);
//    }
//
//    public ImageIcon getIcon(String[] possibleKeys) {
//        return getIcon(possibleKeys, true);
//    }
//
//    public ImageIcon getIcon(String key) {
//        return getIcon(key, true);
//    }
//
//    public ImageIcon getIcon(String key, String defaultIcon) {
//        return getIcon(key, true);
//    }
//
//    public ImageIcon getIcon(String key, boolean warn) {
//        return getIcon(key, null, warn);
//    }
//
//    public ImageIcon getIconSet(String key, boolean warn) {
//        return getIconSet(key, null, warn);
//    }
//
//    public ImageIcon getIcon(String key, String defaultIcon, boolean warn) {
//        String goodPath = get(key, defaultIcon, warn);
//        if (goodPath == null) {
//            return null;
//        }
//        return loadImageIcon(goodPath, warn);
//    }
//
//    public ImageIcon getIconSet(String key, String defaultIcon, boolean warn) {
//        String goodPath = get(key, defaultIcon, warn);
//        if (goodPath != null) {
//            try {
//                return IconSetManager.getIcon(goodPath, getLocale());
//            } catch (IconSetNotFoundException e) {
//                //
//            } catch (IconNotFoundException e) {
//                //
//            }
//            if (warn) {
//                iconNotFound(goodPath, warn);
//            }
//        }
//        return null;
//    }
    //------------------------------------------------------------------
    // GENERIC ICONS
    //------------------------------------------------------------------
//    public ImageIcon getGenericIcon(String genericKey, String[] keyParams, String defaultIcon, boolean warn) {
//        String goodPath = getGeneric(genericKey, keyParams, defaultIcon, warn);
//        if (goodPath == null) {
//            return null;
//        }
//        return loadImageIcon(goodPath, warn);
//    }
//
//    public ImageIcon getGenericIcon(String genericKey, String[] keyParams, boolean warn) {
//        return getGenericIcon(genericKey, keyParams, null, warn);
//    }
//
//    public ImageIcon getGenericIcon(String genericKey, String[] keyParams) {
//        return getGenericIcon(genericKey, keyParams, null, true);
//    }
//
//    public ImageIcon getGenericIcon(String genericKey, String keyParam, String defaultIcon, boolean warn) {
//        return getGenericIcon(genericKey, new String[]{keyParam}, defaultIcon, warn);
//    }
//
//    public ImageIcon getGenericIcon(String genericKey, String keyParam, boolean warn) {
//        return getGenericIcon(genericKey, keyParam, null, warn);
//    }
//
//    public ImageIcon getGenericIcon(String genericKey, String keyParams) {
//        return getGenericIcon(genericKey, keyParams, null, true);
//    }
    //------------------------------------------------------------------
    // GENERICS
    //------------------------------------------------------------------
    //------------------------------------------------------------------
    // getGeneric
    //------------------------------------------------------------------
    public String getGeneric(String genericKey, String[] keyParams, String defaultValue, boolean warn) {
        return get(createResourceKeyArray(genericKey, keyParams), defaultValue, warn);
    }

    public String getGeneric(String genericKey, String[] keyParams, boolean warn) {
        return get(createResourceKeyArray(genericKey, keyParams), genericKey, warn);
    }

    public String getGeneric(String genericKey, String[] keyParams) {
        return get(createResourceKeyArray(genericKey, keyParams), genericKey, true);
    }

    public String getGeneric(String genericKey, String keyParam, String defaultValue, boolean warn) {
        return get(createResourceKeyArray(genericKey, keyParam), defaultValue, warn);
    }

    public String getGeneric(String genericKey, String keyParam, boolean warn) {
        return get(createResourceKeyArray(genericKey, keyParam), genericKey, warn);
    }

    public String getGeneric(String genericKey, String keyParam) {
        return get(createResourceKeyArray(genericKey, keyParam), genericKey, true);
    }

    public String getGeneric(String genericKey, String[] keyParams, String defaultPattern, boolean warn, Object[] values) {
        return get(createResourceKeyArray(genericKey, keyParams), defaultPattern, warn, values);
    }

    public String getGeneric(String genericKey, String[] keyParams, boolean warn, Object[] values) {
        return get(createResourceKeyArray(genericKey, keyParams), genericKey, warn, values);
    }

    public String getGeneric(String genericKey, String[] keyParams, Object[] values) {
        return get(createResourceKeyArray(genericKey, keyParams), genericKey, true, values);
    }

    public String getGeneric(String genericKey, String keyParam, String defaultPattern, boolean warn, Object[] values) {
        return get(createResourceKeyArray(genericKey, keyParam), defaultPattern, warn, values);
    }

    public String getGeneric(String genericKey, String keyParam, boolean warn, Object[] values) {
        return get(createResourceKeyArray(genericKey, keyParam), genericKey, warn, values);
    }

    public String getGeneric(String genericKey, String keyParam, Object[] values) {
        return get(createResourceKeyArray(genericKey, keyParam), genericKey, true, values);
    }

    public static String[] createResourceKeyArray(String mask, String param1, String param2, String param3) {
        return createResourceKeyArray(mask, new String[]{param1, param2, param3});
    }

    public static String[] createResourceKeyArray(String mask, String param1, String param2) {
        return createResourceKeyArray(mask, new String[]{param1, param2});
    }

    public static String[] createResourceKeyArray(String mask, String param1) {
        return createResourceKeyArray(mask, new String[]{param1});
    }

    public static String[] createResourceKeyArray(String mask, String[] params) {
        boolean[][] comb = getCombinaison(params.length);
        String[] s = new String[comb.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = createResourceKey(mask, params, comb[s.length - i - 1]);
        }
        return s;
    }

    public static String createResourceKey(String mask, String[] params, boolean[] comb) {
        int index = 0;
        int lastAsterix = mask.indexOf('*');
        int lastPosition = 0;
        StringBuffer sb = new StringBuffer();
        while (index < params.length && lastAsterix >= 0 && lastPosition < mask.length()) {
            sb.append(mask.substring(lastPosition, lastAsterix));
            if (comb[index]) {
                sb.append(params[index]);
            } else {
                sb.append('*');
            }
            index++;
            lastPosition = lastAsterix + 1;
            lastAsterix = mask.indexOf('*', lastAsterix + 1);
            if (lastAsterix < 0 && mask.length() > lastPosition) {
                sb.append(mask.substring(lastPosition));
            }
        }
        return sb.toString();
    }

    public static boolean[][] getCombinaison(int nbr) {
        int tmp_all = 2 << (nbr - 1);
        boolean[][] ret = new boolean[tmp_all][nbr];
        for (int index = 0; index < tmp_all; index++) {
            for (int c = 0; c < nbr; c++) {
                int val = c == 0 ? 1 : 2 << (c - 1);
                ret[index][c] = (index & val) != 0;
            }
        }
        return ret;
    }
    private boolean validating = false;

    public final synchronized void revalidate() {
        if (!validating) {
            validating = true;
            try {
                super.revalidate();
                for (MessageSetBundle messageSetBundle : bundlesVector) {
                    messageSetBundle.setLocale(getLocale());
                    messageSetBundle.revalidate();
                }
            } finally {
                validating = false;
            }
        }
    }

    public String toString() {
        return "MessageSet(" + bundlesVector + ")";
    }
}

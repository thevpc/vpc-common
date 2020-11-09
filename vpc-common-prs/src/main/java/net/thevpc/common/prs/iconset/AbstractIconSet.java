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
package net.thevpc.common.prs.iconset;

import java.util.logging.Level;
import javax.swing.Icon;
import java.util.*;
import java.awt.*;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 avr. 2007 19:58:30
 */
public abstract class AbstractIconSet implements IconSet {

    private Map<String, Icon> cachedIconsByPaths = new HashMap<String, Icon>();
    private Map<String, Icon> cachedIconsByIds = new HashMap<String, Icon>();
    private boolean cacheEnabled = true;
    private String unknownIconPath;
    private String defaultIconPath;
    private Locale locale;
    private LoggerProvider loggerProvider;
//    private IconSetDescriptor iconSetDescriptor;
    private String id;
    private String name;
    
    private static Icon NullIcon = new Icon() {

        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

        public int getIconWidth() {
            return 0;
        }

        public int getIconHeight() {
            return 0;
        }
    };
    private static Icon NotFoundIcon = new Icon() {

        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

        public int getIconWidth() {
            return 0;
        }

        public int getIconHeight() {
            return 0;
        }
    };

    protected AbstractIconSet() {
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected void setLoggerProvider(LoggerProvider loggerProvider) {
        this.loggerProvider = loggerProvider==null?LoggerProvider.DEFAULT:loggerProvider;
    }

    protected void setName(String name) {
        this.name = name;
    }
    

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    
    
    
//    public String getString(String id) throws IconNotFoundException {
//        String id0 = id;
//        TreeSet<String> seen = new TreeSet<String>();
//        seen.add(id);
//        String value = null;
//        boolean isAlias = false;
//        while (true) {
//            try {
//                value = getIconPath(id0);
//            } catch (MissingResourceException e) {
//                if (isAlias) {
//                    System.err.println("Alias not found " + id0 + " (used by " + id + ")");
//                }
//                if (getDefaultIconPath() != null) {
//                    value = getDefaultIconPath()
//                            .replace("${id}", id0)
//                            .replace("${unknown}", getUnknownIconPath());
//                }
//                //thorw e;
//                return value;
//            }
//            if (value != null && value.length() > 0 && value.charAt(0) == '\\') {
//                return value.substring(1);
//            }
//            if (value != null && value.length() > 0 && value.charAt(0) == '$') {
//
//                id0 = value.substring(1);
//                if (seen.contains(id0)) {
//                    break;
//                }
//                seen.add(id0);
//                isAlias = true;
//            } else {
//                break;
//            }
//        }
//        if (getDefaultIconPath() != null && value != null && value.length() > 0 && value.charAt(0) == '@') {
//            value = getDefaultIconPath()
//                    .replace("${id}", value.substring(1))
//                    .replace("${unknown}", getUnknownIconPath());
//        }
//        return value;
//    }

    public Icon getIconByPath(String path) throws IconNotFoundException {
        if (path != null && path.trim().length() == 0) {
            return null;
        }
        if (path == null) {
            path = "";
        }
        String[] allPaths = path.split(";");
        Stack<String> stack = new Stack<String>();
        for (int i = allPaths.length - 1; i >= 0; i--) {
            stack.push(allPaths[i]);
        }
        try {
            TreeSet<String> visited = new TreeSet<String>();
            while (!stack.isEmpty()) {
                String singlePath = stack.pop();
                if (visited.contains(singlePath)) {
                    //ignore it
                } else {
                    visited.add(singlePath);
                    if (singlePath.length() == 0) {
                        //continue;
                    } else if (singlePath.charAt(0) == '\\') {
                        singlePath = singlePath.substring(1);
                        break;
                    } else if (singlePath.charAt(0) == '$') {
                        String id0 = singlePath.substring(1);
                        try {
                            singlePath = getIconPath(id0);
                            if (singlePath != null) {
                                String[] allPaths2 = singlePath.split(";");
                                for (int i = allPaths2.length - 1; i >= 0; i--) {
                                    stack.push(allPaths2[i]);
                                }
                            } else {
                                return null;
                            }
                        } catch (MissingResourceException e) {
                            //
                        }
                    } else if (singlePath.charAt(0) == '@') {
                        String iconPath = getDefaultIconPath();
                        if (iconPath == null || iconPath.trim().length() == 0) {
                            return null;
                        }
                        stack.push(iconPath.replace("${id}", singlePath.substring(1)).replace("${unknown}", getUnknownIconPath()));
                    } else {
                        Icon icon = getImageIcon(singlePath);
                        if (icon != null) {
                            return icon;
                        }
                    }
                }
            }
            throw new IconNotFoundException("Icon Path \"" + path + "\" not found in " + getName());
        } catch (MissingResourceException e) {
            throw new IconNotFoundException("Icon Path \"" + path + "\" not found in " + getName());
        }
    }

    public Icon getIcon(String id) throws IconNotFoundException {
        Icon icon = cachedIconsByIds.get(id);
        if (icon != null) {
            if (NullIcon == icon) {
                return null;
            }
            if (NotFoundIcon == icon) {
                throw new IconNotFoundException(id);
            }
            return icon;
        } else {
            try {
                icon = getIconById(id);
                if (icon == null) {
                    cachedIconsByIds.put(id, NullIcon);
                } else {
                    cachedIconsByIds.put(id, icon);
                }
                return icon;
            } catch (IconNotFoundException e) {
                cachedIconsByIds.put(id, NotFoundIcon);
                throw e;
            }
        }
    }

    public Icon getIconById(String id) throws IconNotFoundException {
        try {
//            if("ConnectSession".equals(id)){
//                System.out.println("coucou");
//            }
            return getIconByPath(getIconPath(id));
        } catch (MissingResourceException e) {
            throw new IconNotFoundException("Icon \"" + id + "\" not found in " + getName());
        }
    }

    public Icon getUnknowIcon() throws IconNotFoundException {
        return getImageIcon(getUnknownIconPath());
    }

    public Icon getDefaultIcon(String id) throws IconNotFoundException {
        if (getDefaultIconPath() == null) {
            return null;
        }
        String string;
        string = getDefaultIconPath().replace("${id}", id).replace("${unknown}", getUnknownIconPath());
        return getImageIcon(string);
    }

    private Icon getImageIcon(String path) {
        Icon o = path == null ? null : cachedIconsByPaths.get(path);
        if (o == null) {
            o = loadImageIcon(path);
            if (o == null) {
                return null;
            }
            cachedIconsByPaths.put(path, o);
        }
        return o;
    }

    public abstract String getIconPath(String id) throws MissingResourceException;

    public Icon getIcon(String icon, ErrorType errorType) throws IconNotFoundException {
        try {
            return getIcon(icon);
        } catch (IconNotFoundException e) {
            return warn(icon, errorType, e);
        }
    }

    protected Icon warn(String icon, ErrorType errorType, IconNotFoundException e) throws IconNotFoundException {
        switch (errorType) {
            case ERROR: {
                loggerProvider.getLogger(AbstractIconSet.class.getName()).log(Level.WARNING, "{0} : Icon \"{1}\" not found in {2} : {3}", new Object[]{getId(), icon, getId(), e.getMessage()});
                throw e;
            }
            case REQUIRED: {
                loggerProvider.getLogger(AbstractIconSet.class.getName()).log(Level.WARNING, "{0} : Icon \"{1}\" not found in {2} : {3}", new Object[]{getId(), icon, getId(), e.getMessage()});
                return getUnknowIcon();
            }
            case WARN: {
                loggerProvider.getLogger(AbstractIconSet.class.getName()).log(Level.WARNING, "{0} : Icon \"{1}\" not found in {2} : {3}", new Object[]{getId(), icon, getId(), e.getMessage()});
                break;
            }
        }
        return null;
    }

    public Icon getIconE(String icon) throws IconNotFoundException {
        return getIcon(icon, ErrorType.ERROR);
    }

    public Icon getIconR(String icon) throws IconNotFoundException {
        return getIcon(icon, ErrorType.REQUIRED);
    }

    public Icon getIconW(String icon) throws IconNotFoundException {
        return getIcon(icon, ErrorType.WARN);
    }

    public Icon getIconS(String icon) throws IconNotFoundException {
        return getIcon(icon, ErrorType.SILENT);
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        cachedIconsByPaths.clear();
        cachedIconsByIds.clear();
    }

    public String getUnknownIconPath() {
        return unknownIconPath;
    }

    public void setUnknownIconPath(String unknownIconPath) {
        this.unknownIconPath = unknownIconPath;
    }

    public String getDefaultIconPath() {
        return defaultIconPath;
    }

    public void setDefaultIconPath(String defaultIconPath) {
        this.defaultIconPath = defaultIconPath;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }
    
}

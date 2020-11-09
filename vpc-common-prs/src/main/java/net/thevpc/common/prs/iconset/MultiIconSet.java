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

import javax.swing.*;
import java.util.ArrayList;
import java.util.MissingResourceException;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 avr. 2007 19:47:57
 */
public class MultiIconSet extends AbstractIconSet {

    private IconSet[] iconsets;

    /**
     * @param iconsets iconset list, the first has the most priority
     */
    public MultiIconSet(LoggerProvider loggerProvider, IconSet... iconsets) {
        super(); 
        if (iconsets.length < 2) {
            throw new IllegalArgumentException("Too few args " + iconsets.length + "<2");
        }
        ArrayList<IconSet> all = new ArrayList<IconSet>(iconsets.length);
        StringBuilder combinedName = new StringBuilder(16);
        StringBuilder combinedId = new StringBuilder(16);
        for (IconSet iconSet : iconsets) {
            if (iconSet != null) {
                all.add(iconSet);
                if (combinedName.length() > 0) {
                    combinedName.append(",");
                }
                if (combinedId.length() > 0) {
                    combinedId.append(",");
                }
                if (iconSet.getId() == null) {
                    System.out.println("");
                }
                combinedName.append(iconSet.getName());
                combinedId.append(iconSet.getId());
            }
        }
        if (all.size() < 1) {
            throw new IllegalArgumentException("Too few args " + all.size() + "<1");
        }
        this.iconsets = all.toArray(new IconSet[all.size()]);
        setId("(" + combinedId + ")");
        setName("(" + combinedName + ")");
        setLoggerProvider(loggerProvider);
    }

    private static String buildId(IconSet... iconsets) {
        StringBuilder combinedId = new StringBuilder();
        for (IconSet iconSet : iconsets) {
            if (iconSet != null) {
                if (combinedId.length() > 0) {
                    combinedId.append(",");
                }
                if (iconSet.getId() == null) {
                    System.out.println("");
                }
                combinedId.append(iconSet.getId());
            }
        }
        return "(" + combinedId + ")";
    }

    public Icon getIconById(String icon) throws IconNotFoundException {
        for (int i1 = 0; i1 < iconsets.length; i1++) {
            IconSet iset = iconsets[i1];
            Icon i = null;
            boolean found = false;
            try {
                i = iset.getIcon(icon);
                found = true;
            } catch (IconNotFoundException e) {
                //
            }
            if (found) {
                return i;
            } else {
                String s = null;
                try {
                    s = iset.getIconPath(icon);
                } catch (Exception e) {
                    //
                }
                if (s != null) {
                    for (int j1 = i1 + 1; j1 < iconsets.length; j1++) {
                        try {
                            Icon parentIcon = iconsets[j1].getIconByPath(s);
                            if (parentIcon != null) {
                                return parentIcon;
                            }
                        } catch (IconNotFoundException e) {
                            //
                        }
                    }
                }
            }
        }
        throw new IconNotFoundException(icon);
    }

//    public void init(IconSetDescriptor desc, Locale locale) {
//        super.init(desc, locale);
//    }
    public String getIconPath(String id) throws MissingResourceException {
        String s = null;
        for (IconSet extension : iconsets) {
            try {
                s = extension.getIconPath(id);
            } catch (MissingResourceException e) {
                //nothing
            }
            if (s != null) {
                return s;
            }
        }
        return s;
    }

    public Icon loadImageIcon(String path) {
        for (IconSet extension : iconsets) {
            Icon icon = extension.loadImageIcon(path);
            if (icon != null) {
                return icon;
            }
        }
        return null;
    }

    public String getDefaultIconPath() {
        if (iconsets[0] instanceof AbstractIconSet) {
            return ((AbstractIconSet) iconsets[0]).getDefaultIconPath();
        }
        return super.getDefaultIconPath();
    }

    public String getUnknownIconPath() {
        if (iconsets[0] instanceof AbstractIconSet) {
            return ((AbstractIconSet) iconsets[0]).getUnknownIconPath();
        }
        return super.getUnknownIconPath();
    }
}

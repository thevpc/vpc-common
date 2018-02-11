/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.prs.iconset;

import javax.swing.*;
import java.util.ArrayList;
import java.util.MissingResourceException;
import net.vpc.common.prs.log.LoggerProvider;

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

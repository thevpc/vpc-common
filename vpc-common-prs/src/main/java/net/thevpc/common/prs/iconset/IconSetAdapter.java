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

package net.thevpc.common.prs.iconset;

import javax.swing.*;
import java.util.Locale;
import java.util.logging.Level;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 28 avr. 2007 20:40:04
 */
public abstract class IconSetAdapter implements IconSet {
    private String idPrefix ="Adapter";

    public IconSetAdapter() {
    }

    public abstract IconSet getIconSet();
    protected abstract LoggerProvider getLoggerProvider();

    public String getId() {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            return idPrefix +"(null)";
        }
        return idPrefix +"("+iconSet.getId()+")";
    }

    public String getName() {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            return idPrefix +"(null)";
        }
        return idPrefix +"("+iconSet.getName()+")";
    }

    public Icon getIcon(String id) throws IconNotFoundException {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            throw new IconNotFoundException(id);
        }
        return iconSet.getIcon(id);
    }

    public Icon getUnknowIcon() throws IconNotFoundException {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            throw new IconNotFoundException();
        }
        return iconSet.getUnknowIcon();
    }

    public Locale getLocale() {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            return Locale.getDefault();
        }
        return iconSet.getLocale();
    }

    protected Icon warn(String icon, ErrorType errorType, IconNotFoundException e) throws IconNotFoundException {
        switch (errorType) {
            case ERROR: {
                getLoggerProvider().getLogger(AbstractIconSet.class.getName()).log(Level.WARNING, "{0} : Icon \"{1}\" not found in {2} : {3}", new Object[]{getId(), icon, getId(), e.getMessage()});
                throw e;
            }
            case REQUIRED: {
                getLoggerProvider().getLogger(AbstractIconSet.class.getName()).log(Level.WARNING, "{0} : Icon \"{1}\" not found in {2} : {3}", new Object[]{getId(), icon, getId(), e.getMessage()});
                return getUnknowIcon();
            }
            case WARN: {
                getLoggerProvider().getLogger(AbstractIconSet.class.getName()).log(Level.WARNING, "{0} : Icon \"{1}\" not found in {2} : {3}", new Object[]{getId(), icon, getId(), e.getMessage()});
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

    public Icon getIcon(String icon, ErrorType errorType) throws IconNotFoundException {
        try {
            IconSet iconSet = getIconSet();
            if (iconSet == null) {
                throw new IconNotFoundException(icon);
            }
            Icon s = iconSet.getIconS(icon);
            if (s == null) {
                throw new IconNotFoundException(icon);
            }
            return s;
        } catch (IconNotFoundException exc) {
            return warn(icon, errorType, exc);
        }
    }

    public void setLocale(Locale locale) {
        //do nothing
    }

    public String getIconPath(String iconName) {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            return null;
        }
        return iconSet.getIconPath(iconName);
    }

    public Icon loadImageIcon(String path) {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            return null;
        }
        return iconSet.loadImageIcon(path);
    }

    public Icon getIconByPath(String path) throws IconNotFoundException {
        IconSet iconSet = getIconSet();
        if (iconSet == null) {
            throw new IconNotFoundException(path);
        }
        return iconSet.getIconByPath(path);
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }
}

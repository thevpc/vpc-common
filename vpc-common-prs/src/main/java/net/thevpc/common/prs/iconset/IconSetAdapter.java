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
import java.util.Locale;
import java.util.logging.Level;
import net.thevpc.common.prs.log.LoggerProvider;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 28 avr. 2007 20:40:04
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

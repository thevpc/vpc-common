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
import java.util.Locale;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 23 juin 2006 17:22:04
 */
public interface IconSet {
    Icon getIconByPath(String path) throws IconNotFoundException;

    static enum ErrorType {
        ERROR, REQUIRED, WARN, SILENT
    }

    public String getId();

    public String getIconPath(String iconName);
    
    public String getName();

    public Icon getIcon(String id) throws IconNotFoundException;

    public Icon getUnknowIcon() throws IconNotFoundException;

    public Locale getLocale();

    public Icon getIcon(String icon, ErrorType errorType) throws IconNotFoundException;

    public Icon getIconE(String icon) throws IconNotFoundException;

    public Icon getIconR(String icon) throws IconNotFoundException;

    public Icon getIconW(String icon) throws IconNotFoundException;

    public Icon getIconS(String icon) throws IconNotFoundException;

    public void setLocale(Locale locale);

    public abstract Icon loadImageIcon(String path);
}

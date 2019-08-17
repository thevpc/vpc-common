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

package net.vpc.common.swings.iconset;


import javax.swing.*;
import net.vpc.common.prs.iconset.IconSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 26 juin 2006 08:44:38
 */
public class JLabelIconSetUpdater implements ComponentIconSetUpdater {
    public void updateIconSet(JComponent comp, String id, IconSet iconSet) {
        JLabel b = (JLabel) comp;
//        String theme = (String) comp.getClientProperty(IconSetManager.PREFIX+"IconSet");
//        IconSet iconSet=theme==null? IconSetManager.getIconSet() : IconSetManager.getIconSet(theme);
        String icon = id;
        if (id != null) {
            b.setIcon(iconSet.getIconS(icon));
            icon = id + ".disabled";
            b.setDisabledIcon(iconSet.getIconS(icon));
        }
    }

    public void install(JComponent comp, String id) {
        //
    }
}

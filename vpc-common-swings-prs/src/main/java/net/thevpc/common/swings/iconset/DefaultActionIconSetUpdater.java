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

package net.thevpc.common.swings.iconset;


import javax.swing.*;

import net.thevpc.common.prs.iconset.IconSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 26 juin 2006 10:33:22
 */
public class DefaultActionIconSetUpdater implements ActionIconSetUpdater {
    public void updateIconSet(Action action, IconSet iconSet) {
//        Object action_comman_key = action.getValue(Action.ACTION_COMMAND_KEY);
        String icon = (String) action.getValue(SwingIconSetManager.PROP_ID);
//        System.out.println("action_comman_key = " + action_comman_key+"  ==> icon="+icon);
        if (icon != null) {
            action.putValue(Action.SMALL_ICON, iconSet.getIconW(icon));
        }

    }
}

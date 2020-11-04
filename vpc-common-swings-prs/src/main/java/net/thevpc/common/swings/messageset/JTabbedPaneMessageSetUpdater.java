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

package net.thevpc.common.swings.messageset;

import javax.swing.*;

import net.thevpc.common.prs.messageset.MessageSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  27 juin 2006 00:29:57
 */
public class JTabbedPaneMessageSetUpdater implements ComponentMessageSetUpdater {
    public void updateMessageSet(JComponent component, String id, MessageSet messageSet) {
        JTabbedPane comp = (JTabbedPane) component;
        for(int i=0;i<comp.getTabCount();i++){
            String index="["+i+"]";
            String text = messageSet.get(id+index, true);
            String tooltip = messageSet.get(id+index + ".toolTipText", null, false);
            comp.setTitleAt(i,text);
            comp.setToolTipTextAt(i,tooltip);
        }
    }

    public void install(JComponent comp, String id) {
    }
}
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

import net.vpc.common.swings.messageset.SwingMessageSetManager;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import net.vpc.common.prs.iconset.IconSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  27 juin 2006 00:25:16
 */
public class JToggleButtonIconSetUpdater extends AbstractButtonIconSetUpdater {
    private ItemListener toggleButtonItemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            AbstractButton component = (AbstractButton) e.getSource();
            IconSet r = (IconSet) component.getClientProperty("JToggleButtonMessageSetUpdater.IconSet");
            if (r != null) {
                updateIconSet(component, (String) component.getClientProperty(SwingMessageSetManager.PROP_ID), r);
            }
        }
    };

    public void updateIconSet(JComponent comp, String id, IconSet iconSet) {
        super.updateIconSet(comp, id, iconSet);
        comp.putClientProperty("JToggleButtonMessageSetUpdater.IconSet", iconSet);
    }

    public void install(JComponent comp, String id) {
        ((JToggleButton) comp).addItemListener(toggleButtonItemListener);
    }
}

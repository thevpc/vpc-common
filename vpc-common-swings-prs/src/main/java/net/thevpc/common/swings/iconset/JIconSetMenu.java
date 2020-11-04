/**
 * ==================================================================== vpc-prs
 * library
 *
 * Pluggable Resources Set is a small library for simplifying plugin based
 * applications
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.swings.iconset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.prs.iconset.IconSetDescriptor;
import net.thevpc.common.prs.iconset.IconSetManager;

/**
 * @author vpc Date: 21 janv. 2005 Time: 18:50:21
 */
public class JIconSetMenu extends JMenu {

    private ActionListener iconSetChangeActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String lookAndFeelClassName = (String) ((JComponent) e.getSource()).getClientProperty("IconSet");
            try {
                String oldPlaf = IconSetManager.getIconSet().getId();
                IconSetManager.setIconSet(lookAndFeelClassName);
                firePropertyChange("IconSet", oldPlaf, lookAndFeelClassName);
            } catch (Exception e1) {
                e1.printStackTrace();
                ((JComponent) e.getSource()).setEnabled(false);
                ((JComponent) e.getSource()).setToolTipText(e.toString());
            }
        }
    };

    public JIconSetMenu(String selected) {
        this(selected, null);
    }

    public JIconSetMenu(String selected, IconSetDescriptor[] registredIconSets) {
//        ResourcesSwingHelper.setIgnored(this,true);
        setText("Icons Set");
        setName("iconSetMenu");
        if (registredIconSets == null) {
            registredIconSets = IconSetManager.getIconSetDescriptors();
        }
        ButtonGroup buttonGroup = new ButtonGroup();
        String actualClassName = selected == null ? IconSetManager.getIconSet().getId() : selected;
        Map<String, JMenu> mm = new HashMap<String, JMenu>();
        for (IconSetDescriptor nfo : registredIconSets) {
            IconSet iconSet = null;
            try {
                iconSet = IconSetManager.getIconSet(nfo.getId());
            } catch (Exception ex) {
                //ignore
            }
            if (iconSet != null) {
                JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
//            ResourcesSwingHelper.setIgnored(menuItem,true);
                buttonGroup.add(menuItem);
                menuItem.putClientProperty("IconSet", nfo.getId());
                menuItem.setSelected(actualClassName.equals(nfo.getId()));

                menuItem.setText(iconSet.getName());
                String g = nfo.getGroup();
                if (g == null) {
                    add(menuItem);
                } else {
                    JMenu m = mm.get(g);
                    if (m == null) {
                        m = new JMenu(g);
                        add(m);
                        mm.put(g, m);
                    }
                    m.add(menuItem);
                }
                menuItem.addActionListener(iconSetChangeActionListener);
            }
        }
    }

    public void addIconSetChangeListener(PropertyChangeListener propertyChangeListener) {
        addPropertyChangeListener("IconSet", propertyChangeListener);
    }

    public void removeIconSetChangeListener(PropertyChangeListener propertyChangeListener) {
        removePropertyChangeListener("IconSet", propertyChangeListener);
    }
}

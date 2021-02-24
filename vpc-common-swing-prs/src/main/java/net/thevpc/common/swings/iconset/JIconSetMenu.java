/**
 * ==================================================================== vpc-prs
 * library
 *
 * Pluggable Resources Set is a small library for simplifying plugin based
 * applications
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
 * @author thevpc Date: 21 janv. 2005 Time: 18:50:21
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

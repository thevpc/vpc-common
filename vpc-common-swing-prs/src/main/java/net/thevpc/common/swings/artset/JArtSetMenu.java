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
package net.thevpc.common.swings.artset;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import net.thevpc.common.prs.artset.ArtSet;
import net.thevpc.common.prs.artset.ArtSetManager;

/**
 * @author thevpc
 *         Date: 21 janv. 2005
 *         Time: 18:50:21
 */
public class JArtSetMenu extends JMenu {

    private ButtonGroup buttonGroup = new ButtonGroup();
    private String oldArtSet;
    private ActionListener artSetChangeActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String artSetId = (String) ((JComponent) e.getSource()).getClientProperty("artSet");
            try {
                firePropertyChange("artSet", oldArtSet, artSetId);
            } catch (Exception e1) {
                e1.printStackTrace();
                ((JComponent) e.getSource()).setEnabled(false);
                ((JComponent) e.getSource()).setToolTipText(e.toString());
            }
        }
    };

    public JArtSetMenu(String selected, ArtSet[] artSets) {
//        ResourcesSwingHelper.setIgnored(this,true);
        setText("Arts Set");
        setName("ArtSetMenu");
        ArtSet[] registredArtSets = ArtSetManager.getArtSets();
        String actualClassName = selected == null ? ArtSetManager.getCurrent().getId() : selected;
        Map<String, JMenu> mm = new HashMap<String, JMenu>();
        for (ArtSet nfo : registredArtSets) {
            JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
//            ResourcesSwingHelper.setIgnored(menuItem,true);
            buttonGroup.add(menuItem);
            menuItem.putClientProperty("artSet", nfo.getId());
            menuItem.setSelected((actualClassName == null && nfo.getId() == null) || (actualClassName != null && actualClassName.equals(nfo.getId())));
            ArtSet artSet = ArtSetManager.getArtSet(nfo.getId());
            menuItem.setText(artSet.getName());
            String g = nfo.getGroup();
            if (g == null || g.trim().length() == 0) {
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
            menuItem.addActionListener(artSetChangeActionListener);
        }
    }

    public void addArtSetChangeListener(PropertyChangeListener propertyChangeListener) {
        addPropertyChangeListener("artSet", propertyChangeListener);
    }

    public void removeArtSetChangeListener(PropertyChangeListener propertyChangeListener) {
        removePropertyChangeListener("artSet", propertyChangeListener);
    }

    public String getArtSet() {
        Enumeration<AbstractButton> en = buttonGroup.getElements();
        while (en.hasMoreElements()) {
            AbstractButton b = en.nextElement();
            String as = (String) b.getClientProperty("artSet");
            if(b.isSelected()){
                return as;
            }
        }
        return null;
    }

    public void setArtSet(String artSet) {
        oldArtSet = getArtSet();
        Enumeration<AbstractButton> en = buttonGroup.getElements();
        while (en.hasMoreElements()) {
            AbstractButton b = en.nextElement();
            String as = (String) b.getClientProperty("artSet");
            if (as == artSet || (as != null && as.equals(artSet))) {
                b.setSelected(true);
                break;
            }
        }
    }
}
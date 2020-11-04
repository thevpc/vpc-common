/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
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
package net.thevpc.common.swings;

import net.thevpc.common.swings.plaf.PlafItem;
import net.thevpc.common.swings.plaf.UIManager2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class JPlafMenu extends JMenu {

    private Component mainComponent;
    private ActionListener uiChangeActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            PlafItem item = (PlafItem) ((JComponent) e.getSource()).getClientProperty("PlafItem");
            try {
                //String oldPlaf = UIManager.getLookAndFeel().getClass().getName();
                applyPlaf(item);
                firePropertyChange("LookAndFeel", null, item);
            } catch (Exception e1) {
                ((JComponent) e.getSource()).setEnabled(false);
                ((JComponent) e.getSource()).setToolTipText(e.toString());
            }

        }
    };

    public void applyPlaf(PlafItem item) throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException {
        String lookAndFeelClassName = item == null ? null : item.getPlaf();
        if (lookAndFeelClassName != null) {
            UIManager.setLookAndFeel(lookAndFeelClassName);
            Component c = mainComponent;
            if (c == null) {
                c = JPlafMenu.this;
                while (true) {
                    c = c.getParent();
                    if (c.getParent() == null) {
                        if (c instanceof JPopupMenu && ((JPopupMenu) c).getInvoker() != null) {
                            c = ((JPopupMenu) c).getInvoker();
                        } else {
                            break;
                        }
                    }
                }
            }
            SwingUtilities.updateComponentTreeUI(c);
        }
    }

    public JPlafMenu() {
        this(null);
    }

    public JPlafMenu(Component mainComponent) {
//        ResourcesSwingHelper.setIgnored(this,true);
        setText("L&F");
        setName("plafMenu");
        this.mainComponent = mainComponent;
        UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        ButtonGroup buttonGroup = new ButtonGroup();
        String actualClassName = UIManager.getLookAndFeel().getClass().getName();

        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
//            ResourcesSwingHelper.setIgnored(menuItem,true);
        buttonGroup.add(menuItem);
        menuItem.putClientProperty("PlafItem", null);
        menuItem.setSelected(actualClassName == null);
        menuItem.setText("-");
        add(menuItem);
        menuItem.addActionListener(uiChangeActionListener);
        addSeparator();

        for (UIManager.LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
            PlafItem[] items = UIManager2.getPlafHandler(lookAndFeelInfo.getClassName()).loadItems();
            if (items.length == 0) {
                items = new PlafItem[]{new PlafItem(lookAndFeelInfo.getClassName(), lookAndFeelInfo.getClassName(), null, lookAndFeelInfo.getName())};
            }
            if (items.length == 1) {
                //do nothing;
                menuItem = new JCheckBoxMenuItem();
                buttonGroup.add(menuItem);
                menuItem.putClientProperty("PlafItem", items[0]);
                menuItem.setSelected(actualClassName != null && actualClassName.equals(lookAndFeelInfo.getClassName()));
                menuItem.setText(items[0].getName());
                add(menuItem);
                menuItem.addActionListener(uiChangeActionListener);
            } else {
                JMenu menu=new JMenu(lookAndFeelInfo.getName());
                add(menu);
                for (PlafItem plafItem : items) {
                    menuItem = new JCheckBoxMenuItem();
                    buttonGroup.add(menuItem);
                    menuItem.putClientProperty("PlafItem", plafItem);
                    menuItem.setSelected(actualClassName != null && actualClassName.equals(lookAndFeelInfo.getClassName()));
                    menuItem.setText(plafItem.getName());
                    menu.add(menuItem);
                    menuItem.addActionListener(uiChangeActionListener);
                }
            }

        }
    }

    public void addLookAndFeelChangeListener(PropertyChangeListener propertyChangeListener) {
        addPropertyChangeListener("LookAndFeel", propertyChangeListener);
    }

    public void removeLookAndFeelChangeListener(PropertyChangeListener propertyChangeListener) {
        removePropertyChangeListener("LookAndFeel", propertyChangeListener);
    }
}

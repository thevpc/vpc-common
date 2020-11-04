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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public abstract class JDropDownLabel extends JLabel {

//    private String configNode;
    private JPopupMenu menu;

    public void updateUI() {
        super.updateUI();
        if (menu != null) {
            SwingUtilities.updateComponentTreeUI(menu);
        }
    }

//    public void setConfigNode(String node){
//        configNode=node;
//        if(configNode!=null){
//           setFont(Configuration.getUserConfiguration().getFont(configNode+".font",getFont()));
//           setForeground(Configuration.getUserConfiguration().getColor(configNode+".foreground",getForeground()));
//           setBackground(Configuration.getUserConfiguration().getColor(configNode+".background",getBackground()));
//        }
//    }
//    public String getConfigNode(){
//        return configNode;
//    }

    public JDropDownLabel() {
        menu = new JPopupMenu("Personnaliser");
        JMenuItem setFontButton = new JMenuItem("Changer la police");
        JMenuItem setForegroundButton = new JMenuItem("Changer la couleur du texte");
        JMenuItem setBackgroundButton = new JMenuItem("Changer la couleur du fond");
        menu.add(setFontButton);
        menu.add(setForegroundButton);
        menu.add(setBackgroundButton);
        menu.setInvoker(this);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
                    Point p = menu.getInvoker().getLocationOnScreen();
                    Rectangle r = menu.getInvoker().getBounds();
                    menu.setLocation((int) p.getX(), (int) (p.getY() + r.getHeight()));
                    menu.setVisible(true);
                }
            }
        });

        setFontButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    JFontChooser chooser = new JFontChooser(getFont());
                    int ret = chooser.showDialog(
                            null
//                            SwingUtilities3.DEFAULT_PARENT_COMPONENT
                            , "Choisir la nouvelle Police");
                    if (ret == JFontChooser.ACCEPT_OPTION) {
                        setFont(chooser.getSelectedFont());
//                        if(configNode!=null){
//                           Configuration.getUserConfiguration().setFont(configNode+".font",getFont());
//                        }

                    }
                } catch (Exception e) {
                }
            }

        });
        setForegroundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Color ret = JColorChooser.showDialog(
                            null//SwingUtilities3.DEFAULT_PARENT_COMPONENT
                            , "Choisir la nouvelle Couleur d'\u00E9criture", getForeground());
                    if (ret != null) {
                        setForeground(ret);
//                        if(configNode!=null){
//                           Configuration.getUserConfiguration().setColor(configNode+".foreground",getForeground());
//                        }

                    }
                } catch (Exception e) {
                }
            }
        });
        setBackgroundButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    Color ret = JColorChooser.showDialog(
                            null//SwingUtilities3.DEFAULT_PARENT_COMPONENT
                            , "Choisir la nouvelle Couleur de fond", getBackground());
                    if (ret != null) {
                        setBackground(ret);
//                        if(configNode!=null){
//                           Configuration.getUserConfiguration().setColor(configNode+".background",getBackground());
//                        }

                    }
                } catch (Exception e) {
                }
            }
        });
    }

    public JPopupMenu getPopupMenu() {
        return menu;
    }

}

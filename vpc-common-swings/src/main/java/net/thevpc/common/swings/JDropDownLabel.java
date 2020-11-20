/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
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
package net.thevpc.common.swings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  13 juil. 2006 22:14:21
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

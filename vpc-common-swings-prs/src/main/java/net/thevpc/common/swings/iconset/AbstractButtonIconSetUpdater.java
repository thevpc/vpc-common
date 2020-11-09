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

package net.thevpc.common.swings.iconset;



import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.swings.prs.PRSManager;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 26 juin 2006 08:44:38
 */
public class AbstractButtonIconSetUpdater implements ComponentIconSetUpdater {
    public void updateIconSet(JComponent comp, String id, IconSet iconSet) {
        AbstractButton b = (AbstractButton) comp;
        Action a = b.getAction();
        if (a != null && SwingIconSetManager.isIconSetSupported(a)) {
            updateText(b);
            return;
        }
        String iconId;

        if (id != null) {
            iconId = id;
            b.setIcon(iconSet.getIconW(iconId));
            iconId = id + ".selected";
            b.setSelectedIcon(iconSet.getIconS(iconId));
            iconId = id + ".rollover";
            b.setRolloverIcon(iconSet.getIconS(iconId));
            iconId = id + ".rolloverSelected";
            b.setRolloverSelectedIcon(iconSet.getIconS(iconId));
            iconId = id + ".disabled";
            b.setDisabledIcon(iconSet.getIconS(iconId));
            iconId = id + ".disabledSelected";
            b.setDisabledSelectedIcon(iconSet.getIconS(iconId));
        }
        updateText(b);
    }

    private void updateText(AbstractButton b) {
        Action a = b.getAction();
        String noIconText = (a!=null && SwingIconSetManager.isIconSetSupported(a)) ?(String) a.getValue("noIconText"):(String) b.getClientProperty("noIconText");
        String shortName = (a!=null && SwingIconSetManager.isIconSetSupported(a)) ?(String) a.getValue("shortName"):(String) b.getClientProperty("shortName");
        if(PRSManager.isShortNamePreferred(b)){
            noIconText=shortName;
        }
        Boolean showText=(Boolean) b.getClientProperty("showText");
        if(showText!=null){
            if(showText){
                b.setText(noIconText);
            }else{
                b.setText("");
            }
            return;
        }
        if (!(b instanceof JMenuItem)) {
            Icon diconDisplayed = b.getIcon();
            Icon iconDisplayed = b.getIcon();
            if (!b.getModel().isEnabled()) {
                if (b.getModel().isSelected()) {
                    iconDisplayed = b.getDisabledSelectedIcon();
                } else {
                    iconDisplayed = b.getDisabledIcon();
                }
            } else if (b.getModel().isPressed() && b.getModel().isArmed()) {
                iconDisplayed = b.getPressedIcon();
            } else if (b.isRolloverEnabled() && b.getModel().isRollover()) {
                if (b.getModel().isSelected()) {
                    iconDisplayed = b.getRolloverSelectedIcon();
                } else {
                    iconDisplayed = b.getRolloverIcon();
                }
            } else if (b.getModel().isSelected()) {
                iconDisplayed = b.getSelectedIcon();
            }
            if(iconDisplayed==null){
                iconDisplayed=diconDisplayed;
            }
            if(iconDisplayed==null){
                b.setText(noIconText);
            }else{
                b.setText("");
            }
        }
    }

    public void install(final JComponent comp, String id) {
        AbstractButton jtb = (AbstractButton) comp;
        jtb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateText((AbstractButton) e.getSource());
            }
        });
        jtb.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateText((AbstractButton) e.getSource());
            }
        });
        jtb.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                updateText((AbstractButton) e.getSource());
            }

            public void focusLost(FocusEvent e) {
                updateText((AbstractButton) e.getSource());
            }
        });
        jtb.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateText((AbstractButton) comp);
            }
        });


        jtb.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
                updateText((AbstractButton) comp);
            }

            public void mouseExited(MouseEvent e) {
                updateText((AbstractButton) comp);
            }
        });

        //
    }
}

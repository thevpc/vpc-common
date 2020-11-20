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
/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 8 févr. 2007 20:08:21
 */


public class JButtonColorChooser extends JButton {
    public static final String PROPERTY_COLOR_CHANGED="ColorChanged";
    private Color color;
    private boolean nullable;

    public JButtonColorChooser(Color startupColor,boolean nullable) {
        this.color = startupColor;
        this.nullable = nullable;
        setIcon(new ColorSwatch());
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDialog();
            }
        });
    }


    public void showDialog(){
        Color clr = JColorChooser.showDialog(
                JButtonColorChooser.this,null,
                color
        );
        if(nullable || clr != null){
            Color old=color;
            color = clr;
            firePropertyChange(PROPERTY_COLOR_CHANGED,old,color);
        }
    }

    public void setValue(Color obj){
        color = obj;
        repaint();
    }

    public void supportFirePropertyChange(String s, Object obj, Object obj1) {
        firePropertyChange(s, obj, obj1);
    }

    private class ColorSwatch implements Icon {
        public ColorSwatch() {
        }

        public int getIconWidth() {
            return 11;
        }

        public int getIconHeight() {
            return 11;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(Color.black);
            if (color != null) {
                g.fillRect(x, y, getIconWidth(), getIconHeight());
                g.setColor(color);
                g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
            } else {
                g.fillRect(x, y, getIconWidth(), getIconHeight());
                g.setColor(Color.white);
                g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
                g.setColor(Color.black);
                g.drawLine(x + 2, y + 2, x + 2 + getIconWidth() - 4, y + 2 + getIconHeight() - 4);
                g.drawLine(x + 2 + getIconWidth() - 4, y + 2, x + 2, y + 2 + getIconHeight() - 4);
            }
        }
    }

    public Color getValue(){
        return color;
    }
}


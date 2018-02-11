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
package net.vpc.common.swings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 8 févr. 2007 20:08:21
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


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.function.Supplier;
import javax.swing.Icon;

/**
 *
 * @author vpc
 */
public class ColorSwatch implements Icon {

    int size = 14;
    private final Supplier<Color> color;
    private final Supplier<Boolean> enabled;

    public ColorSwatch(final Supplier<Boolean> enabled, Supplier<Color> color) {
        this.enabled = enabled;
        this.color = color;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Boolean en = enabled.get();
        if (en == null) {
            en = true;
        }
        if (en) {
            g.setColor(Color.black);
        } else {
            g.setColor(Color.gray);
        }
        if (color.get() != null) {
            g.fillRect(x, y, getIconWidth(), getIconHeight());
            if (en) {
                g.setColor(color.get());
            } else {
                g.setColor(Color.lightGray);
            }
            g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
        } else {
            g.fillRect(x, y, getIconWidth(), getIconHeight());
            if (en) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.lightGray);
            }
            g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
            if (en) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.gray);
            }
            g.drawLine(x + 2, y + 2, x + 2 + getIconWidth() - 4, y + 2 + getIconHeight() - 4);
            g.drawLine(x + 2 + getIconWidth() - 4, y + 2, x + 2, y + 2 + getIconHeight() - 4);
        }
    }

    public int getIconWidth() {
        return size;
    }

    public int getIconHeight() {
        return size;
    }

}

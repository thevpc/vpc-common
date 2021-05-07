/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 *
 * @author vpc
 */
public class RectColorIcon implements Icon {

    private final Color color;
    private final int w;
    private final int h;

    public RectColorIcon(Color color, int w) {
        this(color, w, w);
    }

    public RectColorIcon(Color color, int w, int h) {
        this.color = color;
        this.w = w;
        this.h = h;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
        g.setColor(color.getRGB() == 0 ? Color.WHITE : color.darker());
        g.drawRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
    }

    @Override
    public int getIconWidth() {
        return w;
    }

    @Override
    public int getIconHeight() {
        return h;
    }

}

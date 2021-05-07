/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

/**
 *
 * @author vpc
 */
public class RainbowIcon implements Icon {

    private final int w;
    private final int h;

    public RainbowIcon(int w) {
        this(w, w);
    }

    public RainbowIcon(int w, int h) {
        this.w = w;
        this.h = h;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        GradientPaint color = new GradientPaint(x, y, Color.orange, getIconWidth(), y,
                Color.blue);
        ((Graphics2D) g).setPaint(color);
        g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
        g.setColor(Color.WHITE);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.Icon;

/**
 *
 * @author thevpc
 */
public class StrokeIcon implements Icon {

    private final Color color;
    private final int w;
    private final int h;
    private final Stroke stroke;

    public StrokeIcon(Color color, int w, int h, Stroke stroke) {
        this.color = color;
        this.w = w;
        this.h = h;
        this.stroke = stroke;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(stroke);
        g.drawLine(x + 2, y + getIconHeight() / 2, getIconWidth() - 4, y + getIconHeight() / 2);
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

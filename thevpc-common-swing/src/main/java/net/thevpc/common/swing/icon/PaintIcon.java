/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.icon;

import net.thevpc.common.swing.color.ColorUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import javax.swing.Icon;

/**
 *
 * @author thevpc
 */
public class PaintIcon implements Icon {

    private final int w;
    private final int h;
    private final Paint paint;

    public PaintIcon(Paint color, int w, int h) {
        this.paint = color;
        this.w = w;
        this.h = h;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(paint);
        g.fillRect(x + 2, y + 2, getIconWidth() - 4, getIconHeight() - 4);
        Color color = ColorUtils.paintToColor(paint);
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

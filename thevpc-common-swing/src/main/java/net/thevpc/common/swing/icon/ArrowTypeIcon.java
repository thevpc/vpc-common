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
import java.awt.geom.Point2D;
import javax.swing.Icon;
import net.thevpc.common.swing.ArrowsUtils;

/**
 *
 * @author thevpc
 */
public class ArrowTypeIcon implements Icon {

    private final Color color;
    private final int w;
    private final int h;
    private final ArrowsUtils.ArrowType  startArrowType;
    private final ArrowsUtils.ArrowType  endArrowType;

    public ArrowTypeIcon(Color color, int w, int h, ArrowsUtils.ArrowType startArrowType, ArrowsUtils.ArrowType endArrowType) {
        this.color = color;
        this.w = w;
        this.h = h;
        this.startArrowType = startArrowType;
        this.endArrowType = endArrowType;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        Graphics2D g2 = (Graphics2D) g;
        int x1 = x + 2;
        int y1 = y + getIconHeight() / 2;
        int x2 = getIconWidth() - 4;
        int y2 = y + getIconHeight() / 2;
        ArrowsUtils.drawArrow(g2, 
                new Point2D.Double(x1, y1), new Point2D.Double(x2, y2), g2.getStroke(), g2.getStroke(), 
                20, startArrowType, endArrowType);
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

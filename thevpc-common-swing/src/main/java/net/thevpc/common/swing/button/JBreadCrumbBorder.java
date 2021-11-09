/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.button;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.border.Border;

/**
 *
 * @author thevpc
 */
public class JBreadCrumbBorder implements Border {

    private int radius;
    private boolean start;
    private boolean end;

    public JBreadCrumbBorder(int radius, boolean start, boolean end) {
        this.radius = radius;
        this.start = start;
        this.end = end;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + (start?5:5), this.radius + 2, this.radius + (start?0:0));
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        x = x + 2;
        y = y + 2;
        width = width - 4;
        height = height - 4;
        if (start) {
            int l = (int) ((width - 1) - 5);
            int m = (int) (height / 2);
            int[][] points = new int[][]{
                {x, y},
                {x + l, y},
                {x + width, y + m},
                {x + l, y + height - 1},
                {x, y + height - 1}
//                ,
//            {x,y},
            };
//        for (int[] point : points) {
//            System.out.println(point);
//        }
            int[] xx = new int[points.length];
            int[] yy = new int[points.length];
            for (int i = 0; i < xx.length; i++) {
                xx[i] = points[i][0];
                yy[i] = points[i][1];

            }
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            if (model.isRollover()) {
                g.setColor(Color.blue.darker());
            } else {
                g.setColor(Color.black);
            }

            g.drawPolygon(xx, yy, points.length);

        } else {
            int l = (int) ((width - 1) - 5);
            int m = (int) (height / 2);
            int[][] points = new int[][]{
                {x, y},
                {x + l, y},
                {x + width, y + m},
                {x + l, y + height - 1},
                {x, y + height - 1},
                {x+5, y + m - 1},
//                ,
//            {x,y},
            };
//        for (int[] point : points) {
//            System.out.println(point);
//        }
            int[] xx = new int[points.length];
            int[] yy = new int[points.length];
            for (int i = 0; i < xx.length; i++) {
                xx[i] = points[i][0];
                yy[i] = points[i][1];

            }
            AbstractButton button = (AbstractButton) c;
            ButtonModel model = button.getModel();
            if (model.isRollover()) {
                g.setColor(Color.blue.darker());
            } else {
                g.setColor(Color.black);
            }

            g.drawPolygon(xx, yy, points.length);
        }

    }
}

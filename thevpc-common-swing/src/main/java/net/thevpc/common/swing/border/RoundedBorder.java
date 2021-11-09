/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.border;

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
public class RoundedBorder implements Border {

    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        x = x + 2;
        y = y + 2;
        width = width - 4;
        height = height - 4;
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();
        if (model.isRollover()) {
            g.setColor(Color.blue.darker());
        } else {
            g.setColor(Color.black);
        }
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}

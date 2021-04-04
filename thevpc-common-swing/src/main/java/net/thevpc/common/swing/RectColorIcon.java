/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

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

    public RectColorIcon(Color color) {
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(2, 2, getIconWidth()-4, getIconHeight()-4);
        g.setColor(color.getRGB()==0?Color.WHITE:color.darker());
        g.drawRect(2, 2, getIconWidth()-4, getIconHeight()-4);
    }

    @Override
    public int getIconWidth() {
        return 16;
    }

    @Override
    public int getIconHeight() {
        return 16;
    }
    
}

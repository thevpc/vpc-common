package net.thevpc.common.swing;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * empty icon.
 */
public final class EmptyIcon implements Icon, Serializable {

    private int width;
    private int height;

    public EmptyIcon() {
        this(0, 0);
    }

    public EmptyIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getIconHeight() {
        return height;
    }

    public int getIconWidth() {
        return width;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        //do nothing
    }

}
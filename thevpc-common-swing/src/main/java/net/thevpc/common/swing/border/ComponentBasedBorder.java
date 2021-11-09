/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.border;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author thevpc
 */
public class ComponentBasedBorder implements Border {

    private Icon icon;
    private Component borderComponent;
    private JComponent container;
    private Rectangle borderComponentBounds;
    private Border border;
    private int borderOffset = 5;

    public static ComponentBasedBorderBuilder of(JComponent container) {
        return new ComponentBasedBorderBuilder(container);
    }

    public static class ComponentBasedBorderBuilder {

        private Icon icon;
        private JComponent container;
        private Component component;
        private Border border;

        public ComponentBasedBorderBuilder(JComponent container) {
            this.container = container;
        }
        

        public ComponentBasedBorderBuilder withComponent(JComponent comp) {
            comp.setOpaque(true);
            component = comp;
            return this;
        }

        public ComponentBasedBorderBuilder withCheckbox() {
            return withComponent(new JCheckBox());
        }

        public ComponentBasedBorderBuilder withIcon(Icon icon) {
            this.icon = icon;
            return this;
        }

        public ComponentBasedBorderBuilder withBorder(Border border) {
            this.border = border;
            return this;
        }

        public ComponentBasedBorder install() {
            ComponentBasedBorder b = new ComponentBasedBorder(container, icon, component, 
                    border==null?BorderFactory.createEmptyBorder():border
            );
            container.setBorder(b);
            return b;
        }
    }

    public Component getBorderComponent() {
        return borderComponent;
    }

    public ComponentBasedBorder(JComponent container, Icon icon, Component borderComponent, Border border) {
        this.borderComponent = borderComponent;
        this.icon = icon;
        this.container = container;
        this.border = border;
        container.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent event) {
                dispatchEvent(event);
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                dispatchEvent(event);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                dispatchEvent(event);
            }

            @Override
            public void mousePressed(MouseEvent event) {
                dispatchEvent(event);
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                dispatchEvent(event);
            }
        });
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Insets borderInsets = border.getBorderInsets(c);
        Insets insets = getBorderInsets(c);
        int temp = (insets.top - borderInsets.top) / 2;
        if (border != null) {
            border.paintBorder(c, g, x, y + temp, width, height - temp);
        }
        Dimension size = borderComponent.getPreferredSize();
        if (icon != null) {
//            int w = icon.getIconWidth();
//            int h = icon.getIconHeight();
            Image image = iconToImage(icon);
            g.drawImage(image, borderOffset, 0, c);
            borderComponentBounds = new Rectangle(borderOffset + icon.getIconWidth() + 2, 0, size.width, size.height);
            SwingUtilities.paintComponent(g, borderComponent, (Container) c, borderComponentBounds);
        } else {
            borderComponentBounds = new Rectangle(borderOffset, 0, size.width, size.height);
            SwingUtilities.paintComponent(g, borderComponent, (Container) c, borderComponentBounds);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        Dimension size = borderComponent.getPreferredSize();
        Insets insets = border == null ? new Insets(0, 0, 0, 0) : border.getBorderInsets(c);
        insets.top = Math.max(insets.top, size.height);
        return insets;
    }

    private void dispatchEvent(MouseEvent event) {
        if (borderComponentBounds != null && borderComponentBounds.contains(event.getX(), event.getY())) {
            Point pt = event.getPoint();
            pt.translate(-borderOffset, 0);
            borderComponent.setBounds(borderComponentBounds);
            borderComponent.dispatchEvent(
                    new MouseEvent(borderComponent, event.getID(),
                            event.getWhen(), event.getModifiers(),
                            pt.x, pt.y, event.getClickCount(),
                            event.isPopupTrigger(), event.getButton()));
            if (!borderComponent.isValid()) {
                container.repaint();
            }
        }
    }

}

package net.thevpc.common.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import static javax.swing.Action.NAME;

public class ColorChooserButton extends JDropDownButton {

    private Color savedColor;
    private JMenuItem clearMenuItem;

    ActionListener clearColorAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            setColorValue(null);
        }
    };

    public ColorChooserButton() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Color c = Color.BLACK;
                if (savedColor instanceof Color) {
                    c = (Color) savedColor;
                }
                Color color = JColorChooser.showDialog(ColorChooserButton.this, "Color Chooser", c);
                if (color == null) {
                    color = c;
                }
                ColorChooserButton.this.setColorValue(color);
            }
        };
        this.addQuickActionListener(actionListener);
        clearMenuItem = new JMenuItem("Clear");
        add(clearMenuItem);
        clearMenuItem.addActionListener(clearColorAction);
    }

    public Object getCellEditorValue() {
        return savedColor;
    }

    public Color getColorValue() {
        return savedColor;
    }

    public void setColorValue(Color color) {
        savedColor = color;
        if (color != null) {
            this.setBackground(color);
        } else {
            this.setBackground(Color.WHITE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (savedColor == null) {
            BufferedImage texture = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
            Graphics2D txG2d = texture.createGraphics();
            txG2d.setColor(Color.DARK_GRAY);
            txG2d.fillRect(0, 0, 2, 2);
            txG2d.setColor(new Color(255, 255, 255, 150));
            txG2d.fillRect(0, 0, 1, 1);
            txG2d.dispose();
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(new TexturePaint(texture, new Rectangle(1, 1, 3, 3)));
            int inset = 3;
            g2d.fillRect(inset, inset, getWidth() - 2 * inset, getHeight() - 2 * inset);
            if (isPaintHandle()) {
                paintHandle(g);//repaint handle!
            }
        }
    }

}

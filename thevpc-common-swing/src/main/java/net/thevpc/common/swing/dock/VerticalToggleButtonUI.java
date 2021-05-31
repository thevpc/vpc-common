package net.thevpc.common.swing.dock;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.geom.AffineTransform;


public class VerticalToggleButtonUI extends BasicToggleButtonUI {

    private static Rectangle iconBounds = new Rectangle();
    private static Rectangle textBounds = new Rectangle();
    private static Rectangle viewBounds = new Rectangle();
    private static Insets viewInsets = new Insets(0, 0, 0, 0);
    protected Direction direction;

    public VerticalToggleButtonUI(Direction direction) {
        super();
        this.direction = direction;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        String text = b.getText();
        Icon icon = (b.isEnabled()) ? b.getIcon() : b.getDisabledIcon();

        if ((icon == null) && (text == null)) {
            return;
        }

        FontMetrics fm = g.getFontMetrics();
        viewInsets = c.getInsets(viewInsets);

        viewBounds.x = viewInsets.left;
        viewBounds.y = viewInsets.top;

        // Use inverted height &amp; width
        viewBounds.height = c.getWidth() - (viewInsets.left + viewInsets.right);
        viewBounds.width = c.getHeight() - (viewInsets.top + viewInsets.bottom);

        iconBounds.x = iconBounds.y = iconBounds.width = iconBounds.height = 0;
        textBounds.x = textBounds.y = textBounds.width = textBounds.height = 0;

        Graphics2D g2 = (Graphics2D) g;
        AffineTransform tr = g2.getTransform();

        if (direction == Direction.BOTTOM_UP) {
            g2.rotate(-Math.PI / 2);
            g2.translate(-c.getHeight(), 0);
            viewBounds.x = c.getHeight() / 2 - (int) fm.getStringBounds(text, g).getWidth() / 2;
            viewBounds.y = c.getWidth() / 2 - (int) fm.getStringBounds(text, g).getHeight() / 2;
        } else if (direction == Direction.UP_BOTTOM) {
            g2.rotate(Math.PI / 2);
            g2.translate(0, -c.getWidth());
            viewBounds.x = c.getHeight() / 2 - (int) fm.getStringBounds(text, g).getWidth() / 2;
            viewBounds.y = c.getWidth() / 2 - (int) fm.getStringBounds(text, g).getHeight() / 2;
        }
        ButtonModel model = b.getModel();
        if (model.isArmed() && model.isPressed() || model.isSelected()) {
            paintButtonPressed(g, b);
        }
        if (icon != null) {
            icon.paintIcon(c, g, iconBounds.x, iconBounds.y);
        }
        Rectangle textBounds2 = new Rectangle();
        String text2 = SwingUtilities.layoutCompoundLabel(
                c, fm, b.getText(), b.getIcon(),
                b.getVerticalAlignment(), b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                viewBounds, iconBounds, textBounds2,
                b.getText() == null ? 0 : b.getIconTextGap());

        if(text != null && !text.equals("")) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textBounds);
            } else {
                int textX = textBounds.x;
                int textY = textBounds.y + fm.getAscent();

                if (b.isEnabled()) {
                    paintText(g, c, new Rectangle(viewBounds.x, viewBounds.y, textX, textY), text);
                } else {
                    paintText(g, c, new Rectangle(viewBounds.x, viewBounds.y, textX, textY), text);
                }
            }
        }

        g2.setTransform(tr);
    }

    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (b.isContentAreaFilled()) {
            Dimension size = b.getSize();
            g.setColor(getSelectColor());
            g.fillRect(0, 0, size.height, size.width);
        }
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension dim = super.getPreferredSize(c);
        return new Dimension(dim.height, dim.width);
    }

    protected Color getSelectColor() {
        Color selectColor = UIManager.getColor(getPropertyPrefix() + "select");
        return selectColor;
    }

    public enum Direction {
        BOTTOM_UP,
        UP_BOTTOM,
    }
}
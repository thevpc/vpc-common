package net.thevpc.common.swing.util;

import java.awt.*;

public class BorderLayoutHelper {
    public static Component getComponent(Container panel, String borderLayoutConstraint) {
        BorderLayout layout = (BorderLayout) panel.getLayout();
        return layout.getLayoutComponent(borderLayoutConstraint);
    }

    public static Component removeComponent(Container panel, String borderLayoutConstraint) {
        BorderLayout layout = (BorderLayout) panel.getLayout();
        Component c = layout.getLayoutComponent(borderLayoutConstraint);
        if (c != null) {
            panel.remove(c);
        }
        return c;
    }

    public static void setComponent(Container panel, String borderLayoutConstraint, Component comp) {
        removeComponent(panel, borderLayoutConstraint);
        panel.add(comp, borderLayoutConstraint);
    }

    public static Component getCenter(Container panel) {
        return getComponent(panel,BorderLayout.CENTER);
    }

    public static Component removeCenter(Container panel) {
        return removeComponent(panel,BorderLayout.CENTER);
    }

    public static void setCenter(Container panel, Container child) {
        setComponent(panel,BorderLayout.CENTER,child);
    }
}

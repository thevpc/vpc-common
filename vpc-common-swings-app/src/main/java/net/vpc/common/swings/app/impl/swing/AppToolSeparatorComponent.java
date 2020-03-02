package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.swings.app.AppComponent;
import net.vpc.common.swings.app.AppToolComponent;
import net.vpc.common.swings.app.AppToolSeparator;
import net.vpc.common.swings.app.Application;

import javax.swing.*;
import java.awt.*;

public class AppToolSeparatorComponent implements ToolGuiComponentSupplier {
    @Override
    public Object createGuiElement(Object parentGuiElement, AppComponent appComponent, Application application) {
        if (appComponent instanceof AppToolComponent) {
            AppToolSeparator tool = (AppToolSeparator) ((AppToolComponent) appComponent).tool();
            Integer height = tool.height().get();
            Integer width = tool.width().get();
            if (parentGuiElement instanceof JToolBar
                    || parentGuiElement instanceof JToolbarGroup
                    || parentGuiElement instanceof JStatusBarGroup
                    || parentGuiElement instanceof JMenuBar
            ) {
                if (width == Integer.MAX_VALUE) {
                    return Box.createHorizontalGlue();
                }
                if (height == Integer.MAX_VALUE) {
                    return Box.createVerticalGlue();
                }
                if (height == 0 && width == 0) {
                    return new JToolBar.Separator(null);
                }
                return new JToolBar.Separator(new Dimension(width, height));
            }
            if (parentGuiElement instanceof JMenu) {
                if (width == Integer.MAX_VALUE) {
                    return Box.createHorizontalGlue();
                }
                if (height == Integer.MAX_VALUE) {
                    return Box.createVerticalGlue();
                }
                if (height == 0 && width == 0) {
                    return new JPopupMenu.Separator();
                }
                if (height == 0) {
                    return Box.createHorizontalStrut(width);
                }
                if (width == 0) {
                    return Box.createVerticalStrut(height);
                }
                return Box.createRigidArea(new Dimension(width, height));
            }
        }
        return null;
    }
}

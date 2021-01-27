package net.thevpc.common.swing.win;

import javax.swing.*;
import java.awt.*;

public class WindowInfo {
    private Component component;
    private String title;
    private boolean resizable;
    private boolean closable;
    private boolean maximizable;
    private boolean iconifiable;
    private boolean icon;
    private Icon frameIcon;
    private Dimension preferredSize = new Dimension(600, 400);

    public Icon getFrameIcon() {
        return frameIcon;
    }

    public WindowInfo setFrameIcon(Icon frameIcon) {
        this.frameIcon = frameIcon;
        return this;
    }

    public Component getComponent() {
        return component;
    }

    public WindowInfo setComponent(Component component) {
        this.component = component;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public WindowInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isResizable() {
        return resizable;
    }

    public WindowInfo setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public boolean isClosable() {
        return closable;
    }

    public WindowInfo setClosable(boolean closable) {
        this.closable = closable;
        return this;
    }

    public boolean isMaximizable() {
        return maximizable;
    }

    public WindowInfo setMaximizable(boolean maximizable) {
        this.maximizable = maximizable;
        return this;
    }

    public boolean isIconifiable() {
        return iconifiable;
    }

    public WindowInfo setIconifiable(boolean iconifiable) {
        this.iconifiable = iconifiable;
        return this;
    }

    public boolean isIcon() {
        return icon;
    }

    public WindowInfo setIcon(boolean icon) {
        this.icon = icon;
        return this;
    }

    public Dimension getPreferredSize() {
        return preferredSize;
    }

    public WindowInfo setPreferredSize(Dimension preferredSize) {
        this.preferredSize = preferredSize;
        return this;
    }
}

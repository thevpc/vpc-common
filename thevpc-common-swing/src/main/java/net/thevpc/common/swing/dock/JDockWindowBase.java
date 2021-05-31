package net.thevpc.common.swing.dock;

import javax.swing.*;
import java.awt.*;

public abstract class JDockWindowBase extends JPanel {
    protected String id;
    protected String title;
    protected Icon icon;
    protected JComponent component;
    protected boolean closable;
    public JDockWindowBase(LayoutManager layout) {
        super(layout);
    }

    public JDockWindowBase() {
    }

    public JDockWindowBase(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public JDockWindowBase(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public String getId() {
        return id;
    }

    public JDockWindowBase setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public JDockWindowBase setTitle(String title) {
        this.title = title;
        return this;
    }

    public Icon getIcon() {
        return icon;
    }

    public JDockWindowBase setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public JComponent getComponent() {
        return component;
    }

    public JDockWindowBase setComponent(JComponent component) {
        this.component = component;
        return this;
    }

    public boolean isClosable() {
        return closable;
    }

    public JDockWindowBase setClosable(boolean closable) {
        this.closable = closable;
        return this;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if(component!=null){
            component.updateUI();
        }
    }
}

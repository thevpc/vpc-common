package net.thevpc.common.lanterna;


import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.LayoutManager;
import com.googlecode.lanterna.gui2.Panel;

import java.awt.*;

public class PanelExt extends Panel implements ContainerExt{
    private boolean visible=true;
    private ComponentOrientation componentOrientation=ComponentOrientation.LEFT_TO_RIGHT;

    public PanelExt() {
        super(new GridBagLayout());
    }

    public PanelExt(LayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public Panel setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public TerminalSize getMinimumSize() {
        return new TerminalSize(0,0);
    }

    @Override
    public ComponentOrientation getComponentOrientation() {
        return componentOrientation;
    }

    @Override
    public void setComponentOrientation(ComponentOrientation componentOrientation) {
        this.componentOrientation = componentOrientation;
        if(this.componentOrientation==null){
            this.componentOrientation=ComponentOrientation.LEFT_TO_RIGHT;
        }
    }
}

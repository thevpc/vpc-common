package net.vpc.common.lanterna;

import com.googlecode.lanterna.gui2.Container;

import java.awt.*;

public interface ContainerExt extends ComponentExt,Container{
    ComponentOrientation getComponentOrientation();
    void setComponentOrientation(ComponentOrientation other);
}

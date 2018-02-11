package net.vpc.common.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Component;

public interface ComponentExt extends Component {
    TerminalSize getMinimumSize();


//    ComponentOrientation getComponentOrientation();
//    void setComponentOrientation();


    boolean isVisible();

    void setVisible(boolean visible);

}

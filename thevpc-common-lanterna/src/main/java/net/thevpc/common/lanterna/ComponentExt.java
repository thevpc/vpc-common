package net.thevpc.common.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Component;

public interface ComponentExt<T extends Component> extends Component {
    TerminalSize getMinimumSize();


    boolean isVisible();

    T setVisible(boolean visible);

}

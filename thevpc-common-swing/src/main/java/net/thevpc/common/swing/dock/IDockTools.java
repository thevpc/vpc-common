package net.thevpc.common.swing.dock;

import javax.swing.*;

public interface IDockTools {

    JDockAnchor getDockAnchor();

    void add(String id, JComponent component, String title, Icon icon, boolean closable);

    boolean containsId(String id);

    void remove(String id);

    boolean isEmpty();

    void setWindowTitle(String id, String title);

    void setWindowIcon(String id, Icon icon);

    void setWindowClosable(String id, boolean closable);

    void setWindowActive(String id, boolean active);

    void activateLast();
    String[] getActive();
}

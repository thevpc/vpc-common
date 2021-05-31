package net.thevpc.common.swing.dock;

import javax.swing.*;

public class TabsTools extends JTabbedContainer implements IDockTools {

    @Override
    public JDockAnchor getDockAnchor() {
        return JDockAnchor.CENTER;
    }

    @Override
    public void setWindowTitle(String id, String title) {
        setTabTitle(id, title);
    }

    @Override
    public void setWindowIcon(String id, Icon icon) {
        setTabIcon(id, icon);
    }

    @Override
    public void setWindowClosable(String id, boolean closable) {
        setTabClosable(id, closable);
    }

    @Override
    public void setWindowActive(String id, boolean active) {
        setTabActive(id, active);
    }

    @Override
    public void activateLast() {

    }

    @Override
    public String[] getActive() {
        String id = getSelectedTabId();
        return id == null ? new String[]{} : new String[]{id};
    }
}

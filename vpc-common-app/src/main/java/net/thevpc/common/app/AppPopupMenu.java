package net.thevpc.common.app;

public interface AppPopupMenu extends AppToolContainer {
    boolean isActionable();
    void show(Object source,int x,int y);
}
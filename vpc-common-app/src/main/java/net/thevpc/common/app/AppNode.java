package net.thevpc.common.app;

public interface AppNode {
    AppComponent getComponent();

    int getOrder();

    ItemPath getPath();

    AppNode[] getChildren();

}

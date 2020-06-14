package net.vpc.common.app;

public interface AppNode {
    AppComponent getComponent();

    int getOrder();

    ItemPath getPath();

    AppNode[] getChildren();

}

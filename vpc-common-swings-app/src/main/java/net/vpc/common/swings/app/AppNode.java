package net.vpc.common.swings.app;

public interface AppNode {
    AppComponent getComponent();

    int getOrder();

    ItemPath getPath();

    AppNode[] getChildren();

}

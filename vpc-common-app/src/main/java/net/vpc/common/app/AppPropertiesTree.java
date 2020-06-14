package net.vpc.common.app;

public interface AppPropertiesTree {

    AppPropertiesNodeFolder root();

    void refresh();
}

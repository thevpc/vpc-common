package net.vpc.common.app;


public interface AppShutdownVeto {

    void vetoableChange(AppEvent event);
}

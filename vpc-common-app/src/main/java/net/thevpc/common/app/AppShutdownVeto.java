package net.thevpc.common.app;


public interface AppShutdownVeto {

    void vetoableChange(AppEvent event);
}

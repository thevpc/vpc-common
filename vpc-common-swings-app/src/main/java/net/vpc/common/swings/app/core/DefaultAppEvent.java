package net.vpc.common.swings.app.core;

import net.vpc.common.swings.app.AppEvent;
import net.vpc.common.swings.app.Application;

public class DefaultAppEvent implements AppEvent {
    private Application application;
    private Object source;

    public DefaultAppEvent(Application application, Object source) {
        this.application = application;
        this.source = source;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public Object getSource() {
        return source;
    }
}

package net.thevpc.common.app.swing.core;

import net.thevpc.common.app.AppEvent;
import net.thevpc.common.app.Application;

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

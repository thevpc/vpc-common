package net.vpc.common.swings.app.core;

import net.vpc.common.swings.app.AppValueEvent;
import net.vpc.common.swings.app.Application;

public class DefaultAppValueEvent<T> extends DefaultAppEvent implements AppValueEvent<T> {
    private T value;

    public DefaultAppValueEvent(Application application, Object source, T value) {
        super(application, source);
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

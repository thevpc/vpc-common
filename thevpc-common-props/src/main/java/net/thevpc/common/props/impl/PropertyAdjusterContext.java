package net.thevpc.common.props.impl;

import net.thevpc.common.props.Property;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.WritableValue;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdjusterContext {
    private List<Runnable> before = new ArrayList<>();
    private List<Runnable> after = new ArrayList<>();
    private PropertyEvent event;
    private boolean ignore;
    private boolean stop;

    public <T> T newValue() {
        return event.newValue();
    }

    public <T> WritableValue<T> writableValue() {
        return (WritableValue<T>) property();
    }
    public Property property() {
        return event().property();
    }

    public PropertyEvent event() {
        return event;
    }

    public PropertyAdjusterContext setEvent(PropertyEvent event) {
        this.event = event;
        return this;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public PropertyAdjusterContext doNothing() {
        setIgnore(true);
        return this;
    }

    public PropertyAdjusterContext doInstead(Runnable run) {
        setIgnore(true);
        getAfter().add(run);
        return this;
    }

    public PropertyAdjusterContext setIgnore(boolean ignore) {
        this.ignore = ignore;
        return this;
    }

    public boolean isStop() {
        return stop;
    }

    public PropertyAdjusterContext setStop(boolean stop) {
        this.stop = stop;
        return this;
    }

    public List<Runnable> getBefore() {
        return before;
    }

    public List<Runnable> getAfter() {
        return after;
    }
}

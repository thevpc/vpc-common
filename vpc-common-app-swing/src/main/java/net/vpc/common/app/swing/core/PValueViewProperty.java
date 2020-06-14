package net.vpc.common.app.swing.core;

import net.vpc.common.app.swing.actions.PropUndoableAction;
import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.app.Application;
import net.vpc.common.app.AppPropertiesNodeItem;

public class PValueViewProperty extends AbstractPropertiesNode implements AppPropertiesNodeItem {

    final Application application;
    final PValue value;
    final Object[] values;
    Object evaluated;

    public PValueViewProperty(String type,Application application, PValue wv) {
        this(type,application, wv, null);
    }

    public PValueViewProperty(String type,Application application, PValue value, Object[] values) {
        super(type);
        this.value = value;
        this.evaluated = value.get();
        this.values = values;
        this.application = application;
    }

    @Override
    public String name() {
        return value.name();
    }

    @Override
    public Object object() {
        return value.get();
    }

    @Override
    public Object[] getValues() {
        return values;
    }

    @Override
    public boolean isEditable() {
        return value instanceof WritablePValue;
    }

    @Override
    public Class getType() {
        try {
            return Class.forName(value.type().getName());
        } catch (ClassNotFoundException e) {
            return Object.class;
        }
    }

    @Override
    public void setValue(Object aValue) {
        if (value instanceof WritablePValue) {
            application.history().doAction(new PropUndoableAction(() -> aValue, () -> (WritablePValue) value, "Update " + value.name()));
            evaluated = value.get();
        }
    }

    @Override
    public Object getEvaluatedValue() {
        return evaluated;
    }

    @Override
    public String toString() {
        return String.valueOf(name());
    }

}
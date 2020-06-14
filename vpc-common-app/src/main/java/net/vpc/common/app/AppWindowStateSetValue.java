package net.vpc.common.app;

import net.vpc.common.props.PValue;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.props.impl.DelegateProperty;

public class AppWindowStateSetValue extends DelegateProperty<AppWindowStateSet> {

    public AppWindowStateSetValue(String name) {
        super(Props.of(name).valueOf(AppWindowStateSet.class, new AppWindowStateSet()));
    }

    @Override
    public WritablePValue<AppWindowStateSet> getBase() {
        return (WritablePValue<AppWindowStateSet>) super.getBase();
    }

    public void add(AppWindowState a) {
        getBase().set(getBase().get().add(a));
    }

    @Override
    public String toString() {
        return getBase().toString();
    }

    @Override
    public PValue<AppWindowStateSet> readOnly() {
        return getBase().readOnly();
    }

}

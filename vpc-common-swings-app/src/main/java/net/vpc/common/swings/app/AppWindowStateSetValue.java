package net.vpc.common.swings.app;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.prpbind.impl.DelegateProperty;

public class AppWindowStateSetValue extends DelegateProperty<AppWindowStateSet> {
    public AppWindowStateSetValue(String name) {
        super(Props.of(name).valueOf(AppWindowStateSet.class,new AppWindowStateSet()));
    }

    @Override
    public WritablePValue<AppWindowStateSet> getBase() {
        return (WritablePValue<AppWindowStateSet>) super.getBase();
    }

    public void add(AppWindowState a) {
        getBase().set(getBase().get().add(a));
    }

}

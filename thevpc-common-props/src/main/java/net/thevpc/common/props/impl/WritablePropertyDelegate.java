package net.thevpc.common.props.impl;
import net.thevpc.common.props.*;

public abstract class WritablePropertyDelegate extends PropertyDelegate implements WritableProperty {

    protected PropertyVetosImpl vetos;
    protected PropertyAdjustersImpl adjusters;
    protected Property base;

    public WritablePropertyDelegate(WritableProperty base) {
        super(base);
        this.base = base;
        adjusters = new PropertyAdjustersImpl(this);
        vetos = new PropertyVetosImpl(this);
        base.vetos().add(event->vetos.firePropertyUpdated(event));
        base.adjusters().add(context -> adjusters.adjust(context));
    }
    protected WritableProperty getBase() {
        return (WritableProperty) super.getBase();
    }


    @Override
    public PropertyVetos vetos() {
        return vetos;
    }

    public PropertyAdjusters adjusters() {
        return adjusters;
    }

    @Override
    public UserObjects userObjects() {
        return getBase().userObjects();
    }

    private class DelegatePropertyListener implements PropertyListener {

        @Override
        public void propertyUpdated(PropertyEvent event) {
            listeners.firePropertyUpdated(event);
        }
    }




}

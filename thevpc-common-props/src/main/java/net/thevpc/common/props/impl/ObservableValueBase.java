package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.util.Objects;

public abstract class ObservableValueBase<T> extends PropertyBase implements ObservableValue<T> {
    protected GetValueModel<T> model;
    public ObservableValueBase(String name, PropertyType type, GetValueModel<T> model) {
        super(name, type);
        if(model==null){
            throw new NullPointerException();
        }
        this.model=model;
    }

    protected GetValueModel<T> model(){
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ObservableValue)) return false;
        ObservableValue that = (ObservableValue) o;
        return Objects.equals(get(), that.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get());
    }

    @Override
    public String toString() {
        return fullPropertyName()+"="+get();
    }
    
}

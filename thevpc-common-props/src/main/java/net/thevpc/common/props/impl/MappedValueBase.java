package net.thevpc.common.props.impl;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.WritableValueModel;

import java.util.Objects;
import java.util.function.Function;

public class MappedValueBase<F, T> extends WritableValueBase<T>{

    private Function<F, T> mapper;
    private ObservableValue<F> value;

    public MappedValueBase(String name, PropertyType toType,ObservableValue<F> value, Function<F, T> mapper ) {
        super(name, toType, new WritableValueModel<T>() {
            @Override
            public void set(T v) {
                throw new IllegalArgumentException("Unsupported update");
            }

            @Override
            public T get() {
                return mapper.apply(value.get());
            }
        });
        if(value==null){
            throw new NullPointerException();
        }
        if(mapper==null){
            throw new NullPointerException();
        }
        this.value=value;
        this.mapper=mapper;
        this.value.onChange(e->{
            T o=mapper.apply(e.oldValue());
            T n=mapper.apply(e.newValue());
            if(!Objects.equals(o,n)){
                ((DefaultPropertyListeners)listeners).firePropertyUpdated(new PropertyEvent(
                        this,e.index(),o,n,e.eventPath(),e.eventType(),e.changeId(),e.immediate()
                ));
            }
        });
    }
    @Override
    public boolean isWritable() {
        return false;
    }

}

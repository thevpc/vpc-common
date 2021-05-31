package net.thevpc.common.props;

import net.thevpc.common.props.impl.WritableValueHelper;

import java.util.function.Function;
import java.util.function.Supplier;

public interface WritableValue<T>
        extends ObservableValue<T>,
        WritableProperty,
        WritableValueModel<T> {

    default T getOrCompute(Supplier<T> t){
        T v=get();
        if(v==null){
            v= t==null?null:t.get();
            if(v!=null){
                set(v);
            }
        }
        return v;
    }

    default void setUsingNonNull(Supplier<T> v) {
        if (v != null) {
            T va = v.get();
            if (va != null) {
                set(va);
            }
        }
    }

    default void setUsing(Supplier<T> v) {
        set(v.get());
    }

    void set(T v);

    default void bindTo(SetValueModel<T> other) {
        events().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                other.set(get());
            }
        });
    }

    default void bindSource(ObservableValue<T> source) {
        WritableValueHelper.helperBindSource(this, source);
    }

    default void bind(Property other, Path relativePath) {
        bind(other,Path.of(other.propertyName()),relativePath);
    }

    default void bind(Property other, Path rootPath, Path relativePath) {
        WritableValueHelper.helperBind(this, other, rootPath, relativePath);
    }

    default void bind(ObservableValue<T> source, SetValueModel<T> target) {
        set(source.get());
        this.bindSource(source);
        this.bindTarget(target);
    }

    default void bind(WritableValue<T> target) {
        bind(target, target);
    }

    default void setAndBind(WritableValue<T> other) {
        set(other.get());
        bindTarget(other);
    }

    default <T2> void setAndBindConvert(WritableValue<T2> other, Function<T, T2> map, Function<T2, T> mapBack) {
        WritableValueHelper.setAndBindConvert(this, other, map, mapBack);
    }


}

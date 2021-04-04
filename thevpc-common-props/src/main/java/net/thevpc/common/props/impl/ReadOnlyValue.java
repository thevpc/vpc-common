package net.thevpc.common.props.impl;

import java.util.function.Function;
import static net.thevpc.common.props.impl.WritableValueBase.helperBind;
import static net.thevpc.common.props.impl.WritableValueBase.helperBindConvert;
import static net.thevpc.common.props.impl.WritableValueBase.helperRemoveBindListeners;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.ObservableValue;

public class ReadOnlyValue<T> extends DelegateProperty<T> implements ObservableValue<T> {

    public ReadOnlyValue(ObservableValue<T> v) {
        super(v);
    }

    @Override
    public void bind(WritableValue<T> other) {
        helperBind(this, other);
    }

    @Override
    public <T2> void bindConvert(WritableValue<T2> other, Function<T, T2> map) {
        helperBindConvert(this, other, map);
    }

    @Override
    public <T2> void unbind(WritableValue<T2> other) {
        helperRemoveBindListeners(listeners(), other);
    }

    @Override
    public ObservableValue<T> readOnly() {
        return this;
    }

    @Override
    public ObservableValue<T> getBase() {
        return (ObservableValue<T>) super.getBase();
    }

//    public static <T> ReadOnlyPValue<T> of(PValue<T> v) {
//        if (v instanceof ReadOnlyPValue<?>) {
//            return (ReadOnlyPValue<T>) v;
//        }else{
//            if(ro==null){
//        }
//        return new ReadOnlyPValue<T>(v);
//    }
    @Override
    public T get() {
        return getBase().get();
    }
}

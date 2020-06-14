package net.vpc.common.props.impl;

import java.util.function.Function;
import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePValue;
import static net.vpc.common.props.impl.WritablePValueBase.helperBind;
import static net.vpc.common.props.impl.WritablePValueBase.helperBindConvert;
import static net.vpc.common.props.impl.WritablePValueBase.helperRemoveBindListeners;
import net.vpc.common.props.*;

public class ReadOnlyPValue<T> extends DelegateProperty<T> implements PValue<T> {

    public ReadOnlyPValue(PValue<T> v) {
        super(v);
    }

    @Override
    public void bind(WritablePValue<T> other) {
        helperBind(this, other);
    }

    @Override
    public <T2> void bindConvert(WritablePValue<T2> other, Function<T, T2> map) {
        helperBindConvert(this, other, map);
    }

    @Override
    public <T2> void unbind(WritablePValue<T2> other) {
        helperRemoveBindListeners(listeners(), other);
    }

    @Override
    public PValue<T> readOnly() {
        return this;
    }

    @Override
    public PValue<T> getBase() {
        return (PValue<T>) super.getBase();
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

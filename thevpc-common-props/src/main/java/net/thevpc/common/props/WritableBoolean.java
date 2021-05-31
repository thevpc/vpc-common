package net.thevpc.common.props;

public interface WritableBoolean extends WritableValue<Boolean>, ObservableBoolean {
    <T> void bindEquals(WritableValue<T> property, T value);

}

package net.thevpc.common.props;

public interface WritableNamedNode<T> extends NamedNode<T>, WritableValue<T> {


    PropertyVetos vetos();

    @Override
    WritableMap<String, WritableNamedNode<T>> children();
}

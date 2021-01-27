package net.thevpc.common.props;

public interface WritablePNamedNode<T> extends PNamedNode<T>, WritablePValue<T> {


    PropertyVetos vetos();

    @Override
    WritablePMap<String, WritablePNamedNode<T>> children();
}

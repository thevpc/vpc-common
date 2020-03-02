package net.vpc.common.prpbind;


public interface WritablePNamedNode<T> extends PNamedNode<T>,WritablePValue<T> {

    PNamedNode<T> readOnly();

    PropertyVetos vetos();

    @Override
    WritablePMap<String, WritablePNamedNode<T>> children();
}

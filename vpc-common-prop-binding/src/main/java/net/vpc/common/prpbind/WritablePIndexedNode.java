package net.vpc.common.prpbind;


public interface WritablePIndexedNode<T> extends PIndexedNode<T>,WritablePValue<T> {

    PIndexedNode<T> readOnly();

    @Override
    WritablePList<WritablePIndexedNode<T>> children();
}

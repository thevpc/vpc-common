package net.thevpc.common.props;


public interface WritablePIndexedNode<T> extends PIndexedNode<T>,WritablePValue<T> {


    @Override
    WritablePList<WritablePIndexedNode<T>> children();
}

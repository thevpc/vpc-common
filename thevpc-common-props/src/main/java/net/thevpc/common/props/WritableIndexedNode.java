package net.thevpc.common.props;


public interface WritableIndexedNode<T> extends IndexedNode<T>,WritableValue<T> {


    @Override
    WritableList<WritableIndexedNode<T>> children();
}

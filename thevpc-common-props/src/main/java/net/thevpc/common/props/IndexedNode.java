package net.thevpc.common.props;

import net.thevpc.common.props.impl.Helpers;

import java.util.List;
import java.util.function.Predicate;

public interface IndexedNode<T> extends ObservableValue<T> {
    List<WritableIndexedNode<T>> findChildren(Predicate<WritableIndexedNode<T>> filter, boolean deep);

    default WritableIndexedNode<T> findChild(Object[] path){
        return (WritableIndexedNode<T>) Helpers.IndexedNode_findChild((WritableIndexedNode<T>) IndexedNode.this,path);
    }

    ObservableList<? extends IndexedNode<T>> children();

    IndexedNode<T> readOnly();
}

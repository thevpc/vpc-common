package net.thevpc.common.props;

import java.util.List;
import java.util.function.Predicate;

public interface IndexedNode<T> extends ObservableValue<T> {
    List<WritableIndexedNode<T>> findChildren(Predicate<WritableIndexedNode<T>> filter, boolean deep);

    ObservableList<? extends IndexedNode<T>> children();

    IndexedNode<T> readOnly();
}

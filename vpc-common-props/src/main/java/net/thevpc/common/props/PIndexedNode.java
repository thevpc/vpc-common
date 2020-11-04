package net.thevpc.common.props;

import java.util.List;
import java.util.function.Predicate;

public interface PIndexedNode<T> extends PValue<T> {
    List<WritablePIndexedNode<T>> findChildren(Predicate<WritablePIndexedNode<T>> filter, boolean deep);

    PList<? extends PIndexedNode<T>> children();

    PIndexedNode<T> readOnly();
}

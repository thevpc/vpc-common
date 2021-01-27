package net.thevpc.common.props;

import java.util.Map;
import java.util.function.Predicate;

public interface PNamedNode<T> extends PValue<T> {
    Map<String,WritablePNamedNode<T>> findChildren(Predicate<WritablePNamedNode<T>> filter, boolean deep);

    PMap<String,? extends PNamedNode<T>> children();

    PNamedNode<T> readOnly();
}

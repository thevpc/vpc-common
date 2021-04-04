package net.thevpc.common.props;

import java.util.Map;
import java.util.function.Predicate;

public interface NamedNode<T> extends ObservableValue<T> {
    Map<String,WritableNamedNode<T>> findChildren(Predicate<WritableNamedNode<T>> filter, boolean deep);

    ObservableMap<String,? extends NamedNode<T>> children();

    NamedNode<T> readOnly();
}

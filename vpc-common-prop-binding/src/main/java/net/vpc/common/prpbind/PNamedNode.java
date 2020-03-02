package net.vpc.common.prpbind;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface PNamedNode<T> extends PValue<T> {
    Map<String,WritablePNamedNode<T>> findChildren(Predicate<WritablePNamedNode<T>> filter, boolean deep);

    PMap<String,? extends PNamedNode<T>> children();

}

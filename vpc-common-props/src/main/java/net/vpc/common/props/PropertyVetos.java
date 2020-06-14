package net.vpc.common.props;

import java.util.function.Predicate;

public interface PropertyVetos {

    void add(PropertyVeto listener);

    void removeIf(Predicate<PropertyVeto> filter);

    void remove(PropertyVeto listener);

    PropertyVeto[] getAll();
}

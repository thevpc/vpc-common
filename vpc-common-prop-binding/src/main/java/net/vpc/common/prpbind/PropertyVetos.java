package net.vpc.common.prpbind;

public interface PropertyVetos {
    void add(PropertyVeto listener);

    void remove(PropertyVeto listener);

    PropertyVeto[] getAll();
}

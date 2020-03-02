package net.vpc.common.prpbind;

public interface PropertyAdjusters {
    void add(PropertyAdjuster listener);

    void remove(PropertyAdjuster listener);

    PropertyAdjuster[] getAll();
}

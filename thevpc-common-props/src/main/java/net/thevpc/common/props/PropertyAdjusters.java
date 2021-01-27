package net.thevpc.common.props;

public interface PropertyAdjusters {
    void add(PropertyAdjuster listener);

    void remove(PropertyAdjuster listener);

    PropertyAdjuster[] getAll();
}

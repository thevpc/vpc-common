package net.vpc.common.props;

public interface PropertyAdjuster {
    Object adjustNewValue(PropertyEvent event);
}

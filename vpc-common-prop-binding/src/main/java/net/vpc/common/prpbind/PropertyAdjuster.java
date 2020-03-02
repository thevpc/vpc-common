package net.vpc.common.prpbind;

public interface PropertyAdjuster {
    Object adjustNewValue(PropertyEvent event);
}

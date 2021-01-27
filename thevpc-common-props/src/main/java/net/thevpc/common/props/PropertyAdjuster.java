package net.thevpc.common.props;

public interface PropertyAdjuster {
    Object adjustNewValue(PropertyEvent event);
}

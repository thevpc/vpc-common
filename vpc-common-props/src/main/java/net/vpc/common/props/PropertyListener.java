package net.vpc.common.props;

public interface PropertyListener {

    void propertyUpdated(PropertyEvent event);

    default int order() {
        return 0;
    }
}

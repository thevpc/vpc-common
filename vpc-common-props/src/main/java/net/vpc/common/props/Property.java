package net.vpc.common.props;

public interface Property extends WithListeners {

    PropertyVetos vetos();

    String name();

    PropertyType type();

    boolean isWritable();

    UserObjects userObjects();

    Property readOnly();
}

package net.vpc.common.prpbind;

public interface Property extends WithListeners {
    String getPropertyName();

    PropertyType getType();

    boolean isRefWritable();

    boolean isValueWritable();
}

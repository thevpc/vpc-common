package net.vpc.common.jeep;

public interface JAnnotationInstanceField {
    JAnnotationField getAnnotationField();

    String getName();

    Object getValue();

    Object getDefaultValue();
}

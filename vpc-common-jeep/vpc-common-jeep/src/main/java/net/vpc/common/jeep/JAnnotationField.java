package net.vpc.common.jeep;

public interface JAnnotationField {
    JAnnotationType getAnnotationType();

    String getName();
    Object getDefaultValue();
}

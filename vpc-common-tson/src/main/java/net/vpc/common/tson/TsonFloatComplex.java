package net.vpc.common.tson;

public interface TsonFloatComplex extends TsonNumber {
    float getReal();

    float getImag();

    TsonPrimitiveBuilder builder();
}

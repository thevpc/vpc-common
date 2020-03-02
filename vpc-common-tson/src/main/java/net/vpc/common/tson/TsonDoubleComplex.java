package net.vpc.common.tson;

public interface TsonDoubleComplex extends TsonNumber {
    double getReal();

    double getImag();

    TsonPrimitiveBuilder builder();
}

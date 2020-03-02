package net.vpc.common.tson;

public interface TsonFloat extends TsonNumber {
    float getValue();

    TsonPrimitiveBuilder builder();
}

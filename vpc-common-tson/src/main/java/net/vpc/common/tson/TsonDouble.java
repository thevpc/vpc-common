package net.vpc.common.tson;

public interface TsonDouble extends TsonNumber {
    double getValue();
    TsonPrimitiveBuilder builder();
}

package net.vpc.common.tson;

public interface TsonInt extends TsonNumber {
    int getValue();
    TsonPrimitiveBuilder builder();
}

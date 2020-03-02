package net.vpc.common.tson;

public interface TsonByte extends TsonNumber {
    byte getValue();

    TsonPrimitiveBuilder builder();
}

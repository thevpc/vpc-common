package net.vpc.common.tson;

public interface TsonShort extends TsonNumber {
    short getValue();
    TsonPrimitiveBuilder builder();
}

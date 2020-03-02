package net.vpc.common.tson;

public interface TsonLong extends TsonNumber {
    long getValue();

    TsonPrimitiveBuilder builder();
}

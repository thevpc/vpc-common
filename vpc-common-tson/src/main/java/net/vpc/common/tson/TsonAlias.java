package net.vpc.common.tson;

public interface TsonAlias extends TsonElement {
    String getName();
    TsonPrimitiveBuilder builder();
}

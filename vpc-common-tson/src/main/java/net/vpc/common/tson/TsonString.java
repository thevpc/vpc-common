package net.vpc.common.tson;

public interface TsonString extends TsonElement {
    String getValue();

    TsonPrimitiveBuilder builder();
}

package net.vpc.common.tson;

public interface TsonElementBase {
    TsonElementType getType();

    TsonElement build();

    String toString();

    String toString(boolean compact);

    String toString(TsonFormat format);
}

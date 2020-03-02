package net.vpc.common.tson;

public interface TsonFormat {
    String format(TsonElement element);

    String format(TsonDocument element);

    TsonFormatBuilder builder();
}

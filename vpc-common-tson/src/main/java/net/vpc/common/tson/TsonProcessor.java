package net.vpc.common.tson;

public interface TsonProcessor {
    TsonElement removeComments(TsonElement element);

    TsonElement resolveAliases(TsonElement element);
}

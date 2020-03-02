package net.vpc.common.tson;

public interface TsonElementToObject<T> {
    T toObject(TsonElement element,Class<T> to, TsonObjectContext context);
}

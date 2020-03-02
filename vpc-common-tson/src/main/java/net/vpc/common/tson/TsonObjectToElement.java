package net.vpc.common.tson;

public interface TsonObjectToElement<T> {
    TsonElementBase toElement(T object, TsonObjectContext context);
}

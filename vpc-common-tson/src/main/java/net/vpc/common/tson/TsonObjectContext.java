package net.vpc.common.tson;

public interface TsonObjectContext {
    <T> TsonElement elem(T any);

    <T> T obj(TsonElement element, Class<T> clazz);

}

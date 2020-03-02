package net.vpc.common.util;

import java.util.*;

/**
 * Created by vpc on 11/22/16.
 */
public class ClassMapList<T> {
    private Class<T> elementValueType;
    private ClassMap<List<T>> values;

    public ClassMapList(ClassMapList<T> other) {
        this(other.getKeyType(), other.getValueType());
        for (Map.Entry<Class, List<T>> e : other.entrySet()) {
            for (T item : e.getValue()) {
                add(e.getKey(), item);
            }
        }
    }

    public ClassMapList(Class keyType, Class<T> elementValueType) {
        values = new ClassMap<>(keyType, (Class) List.class);
        this.elementValueType = elementValueType;
    }

    public Class getKeyType() {
        return values.getKeyType();
    }

    public Class<T> getValueType() {
        return elementValueType;
    }

    public Set<Map.Entry<Class, List<T>>> entrySet() {
        return values.entrySet();
    }

    public List<T> add(Class classKey, T value) {
        List<T> list = getOrCreate(classKey);
        list.add(value);
        return list;
    }

    public List<T> getOrCreate(Class classKey) {
        List<T> list = values.getExact(classKey);
        if (list == null) {
            list = new ArrayList<T>();
            values.put(classKey, list);
        }
        return list;
    }

    public ClassMapList(Class keyType, Class<T> elementValueType, int initialCapacity) {
        values = new ClassMap<>(keyType, (Class) List.class, initialCapacity);
        this.elementValueType = elementValueType;
    }

    public ClassMapList(Class<T> elementValueType) {
        this(Object.class, elementValueType);
        this.elementValueType = elementValueType;
    }

    public Set<Class> keySet() {
        return values.keySet();
    }

    public List<T> getAll(Class classKey) {
        List<T> all=new ArrayList<>();
        for (List<T> ts : values.getAll(classKey)) {
            all.addAll(ts);
        }
        return all;
    }

    public List<T> getExact(Class classKey) {
        List<T> list = values.getExact(classKey);
        if (list == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    public boolean remove(Class classKey) {
        return values.remove(classKey) != null;
    }

    public boolean remove(Class classKey, T value) {
        List<T> a = values.getExact(classKey);
        if (a != null) {
            if (a.remove(value)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return values.size();
    }

    public ClassMapList<T> copy(){
        return new ClassMapList<T>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassMapList<?> that = (ClassMapList<?>) o;
        return Objects.equals(elementValueType, that.elementValueType) &&
                Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementValueType, values);
    }
}

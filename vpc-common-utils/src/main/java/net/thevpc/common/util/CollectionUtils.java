package net.thevpc.common.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtils {

    public static <T> List<T> head(List<T> anyList, int maxSize) {
        if (maxSize < 0) {
            maxSize = anyList.size() + maxSize;
        }
        if (anyList.size() > maxSize) {
            return anyList.subList(0, maxSize);
        }
        return anyList;
    }

    public static <T> List<T> tail(List<T> anyList, int maxSize) {
        if (anyList.size() > maxSize) {
            return anyList.subList(anyList.size() - maxSize, maxSize);
        }
        return anyList;
    }

    public static <T> List<List<T>> splitBy(Collection<T> anyList, int groupSize) {
        List<List<T>> grouped = new ArrayList<List<T>>();
        for (int i = 0; i < groupSize; i++) {
            grouped.add(new ArrayList<T>());
        }
        if (anyList != null) {
            int i = 0;
            for (T item : anyList) {
                grouped.get(i % groupSize).add(item);
                i++;
            }
        }
        return grouped;
    }

    public static <T> List<List<T>> groupBy(Collection<T> anyList, int groupSize) {
        List<List<T>> grouped = new ArrayList<List<T>>();
        List<T> curr = new ArrayList<T>();
        if (anyList != null) {
            for (T item : anyList) {
                if (curr.size() < groupSize) {
                    curr.add(item);
                } else {
                    grouped.add(curr);
                    curr = new ArrayList<T>();
                    curr.add(item);
                }
            }
        }
        if (curr.size() > 0) {
            grouped.add(curr);
        }
        return grouped;
    }

    public static <A, B> List<B> convert(List<A> list, Function<A, B> converter) {
        return new ImmutableConvertedList<A, B>(list, converter);
    }

    public static <T> List<T> filter(Collection<T> collection, CollectionFilter<T> filter) {
        ArrayList<T> ret = new ArrayList<T>();
        int i = 0;
        for (T t : collection) {
            if (filter.accept(t, i, collection)) {
                ret.add(t);
            }
            i++;
        }
        return ret;
    }

    public static <T> List<T> toList(Iterator<T> it) {
        List<T> all = new ArrayList<>();
        while (it.hasNext()) {
            all.add(it.next());
        }
        return all;
    }

    public static <T> List<T> toList(Iterable<T> it) {
        return toList(it.iterator());
    }

    public static <K, V> KeyValueList<K, V> unmodifiableMapList(KeyValueList<K, V> list) {
        return list == null ? null : new UnmodifiableKeyValueList<K, V>(list);
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> map) {
        return map == null ? null : Collections.unmodifiableMap(map);
    }

    public static <V> List<V> unmodifiableList(List<V> list) {
        return list == null ? null : Collections.unmodifiableList(list);
    }

    public static <V> Collection<V> unmodifiableCollection(Collection<V> list) {
        return list == null ? null : Collections.unmodifiableCollection(list);
    }

    public static <T> Collection<T> retainAll(Collection<T> values, Predicate<T> filter) {
        if (filter == null) {
            throw new NullPointerException("Filter could not be null");
        }
        for (Iterator<T> i = values.iterator(); i.hasNext();) {
            if (!filter.test(i.next())) {
                i.remove();
            }
        }
        return values;
    }

    public static <T> Iterator<T> concatIterators(Iterator<T> a, Iterator<T> b) {
        if (a == null && b == null) {
            return Collections.emptyIterator();
        } else if (a == null && b != null) {
            return b;
        } else if (a != null && b == null) {
            return a;
        } else {
            if (a instanceof QueueIterator) {
                QueueIterator<T> aa = (QueueIterator<T>) a;
                aa.add(b);
                return aa;
            } else {
                QueueIterator<T> aa = new QueueIterator<T>();
                aa.add(a);
                aa.add(b);
                return aa;
            }
        }
    }

    public static <T> Iterator<T> coalesceIterators(Iterator<T> a, final Iterator<T> b) {
        if (a == null && b == null) {
            return Collections.emptyIterator();
        } else if (a == null && b != null) {
            return b;
        } else if (a != null && b == null) {
            return a;
        } else {
            if (a instanceof CoalesceIterator) {
                CoalesceIterator<T> c = (CoalesceIterator<T>) a;
                c.add(b);
                return c;
            } else {
                CoalesceIterator<T> c = new CoalesceIterator<T>();
                c.add(a);
                c.add(b);
                return c;
            }
        }
    }

    public static <T> Collection<T> removeAll(Collection<T> values, Predicate<T> filter) {
        if (filter == null) {
            throw new NullPointerException("Filter could not be null");
        }
        for (Iterator<T> i = values.iterator(); i.hasNext();) {
            if (filter.test(i.next())) {
                i.remove();
            }
        }
        return values;
    }

    public static <T> Iterator<T> nullifyIfEmpty(Iterator<T> other) {
        if (other == null) {
            return null;
        }
        if (other instanceof PushBackIterator) {
            PushBackIterator<T> b = (PushBackIterator<T>) other;
            if (!b.isEmpty()) {
                return b;
            } else {
                return null;
            }
        }
        PushBackIterator<T> b = new PushBackIterator<>(other);
        if (!b.isEmpty()) {
            return b;
        } else {
            return null;
        }
    }

    /**
     * created a view on the List where each element is replaced by it converter
     *
     * @param from
     * @param converter
     * @param <F>
     * @param <T>
     * @return
     */
    public <F, T> List<T> convertList(final List<F> from, final Function<F, T> converter) {
        if (converter == null) {
            throw new NullPointerException("Null converter");
        }
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                F value = from.get(index);
                return converter.apply(value);
            }

            @Override
            public T remove(int index) {
                F removed = from.remove(index);
                if (removed == null) {
                    return null;
                }
                return converter.apply(removed);
            }

            @Override
            public int size() {
                return from.size();
            }
        };
    }

    public static <K, V> Map<K, V> mergeMaps(Map<K, V> source, Map<K, V> dest) {
        if (dest == null) {
            dest = new HashMap<>();
        }
        if (source != null) {
            for (Map.Entry<K, V> e : source.entrySet()) {
                if (e.getValue() != null) {
                    dest.put(e.getKey(), e.getValue());
                } else {
                    dest.remove(e.getKey());
                }
            }
        }
        return dest;
    }

    public static Set<String> toSet(String[] values0, boolean trim, boolean ignoreEmpty, boolean ignoreNull) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        if (values0 != null) {
            for (String a : values0) {
                if (a != null) {
                    if (trim) {
                        a = a.trim();
                    }
                    if (a.isEmpty()) {
                        a = null;
                    }
                }
                if (a == null && ignoreNull) {
                    continue;
                }
                if (a != null && a.isEmpty() && ignoreEmpty) {
                    continue;
                }
                set.add(a);
            }
        }
        return set;
    }

    

}

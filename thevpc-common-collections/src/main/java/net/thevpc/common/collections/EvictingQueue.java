package net.thevpc.common.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EvictingQueue<T> {

    private int max;
    private LinkedList<T> values = new LinkedList<>();

    public EvictingQueue(int max) {
        this.max = max;
    }

    public void clear() {
        values.clear();
    }

    public int size() {
        return values.size();
    }

    public T get(int pos) {
        return values.get(pos);
    }

    public void add(T t) {
        if (values.size() >= max) {
            values.removeFirst();
        }
        values.add(t);
    }

    public List<T> toList() {
        int size = size();
        List<T> a = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            a.add(get(i));
        }
        return a;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size();
            }

            @Override
            public T next() {
                T y = get(i++);
                return y;
            }
        };
    }

    public Stream<T> stream() {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED),
                false
        );
    }

}

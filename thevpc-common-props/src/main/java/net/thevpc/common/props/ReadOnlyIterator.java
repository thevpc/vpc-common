package net.thevpc.common.props;

import java.util.Iterator;

public class ReadOnlyIterator<T> implements Iterator<T> {

    public static <T> ReadOnlyIterator<T> of(Iterator<T> i){
        if(i instanceof ReadOnlyIterator<?>){
            return (ReadOnlyIterator<T>) i;
        }
        return new ReadOnlyIterator<>(i);
    }

    private Iterator<T> base;

    public ReadOnlyIterator(Iterator<T> base) {
        if(base==null){
            throw new NullPointerException("null iterator");
        }
        this.base = base;
    }

    @Override
    public boolean hasNext() {
        return base.hasNext();
    }

    @Override
    public T next() {
        return base.next();
    }
}

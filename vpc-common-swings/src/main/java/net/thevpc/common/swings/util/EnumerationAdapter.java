package net.thevpc.common.swings.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author Taha BEN Salah
 * %creationtime 9/2/12 1:52 PM
 */
public class EnumerationAdapter<T> implements Enumeration<T> {
    private Iterator<T> it;

    public EnumerationAdapter(Iterator<T> it) {
        this.it = it;
    }

    @Override
    public boolean hasMoreElements() {
        return it.hasNext();
    }

    @Override
    public T nextElement() {
        return it.next();
    }
}

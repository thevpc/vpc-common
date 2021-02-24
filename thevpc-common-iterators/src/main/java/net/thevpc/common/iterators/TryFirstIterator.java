/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iterators;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thevpc
 */
public class TryFirstIterator<T> implements Iterator<T> {

    private static Logger LOG = Logger.getLogger(TryFirstIterator.class.getName());
    private Iterator<T> index;
    private Iterator<T> other;
    private long readFromFirst;
    private T nextItem;
    private boolean hasNextItem;

    public TryFirstIterator(Iterator<T> index, Iterator<T> other) {
        this.index = index;
        this.other = other;
    }

    @Override
    public boolean hasNext() {
        if (index != null) {
            if (readFromFirst == 0) {
                try {
                    boolean v = index.hasNext();
                    if (v) {
                        hasNextItem = true;
                        nextItem = index.next();
                        readFromFirst++;
                    }
                    return v;
                } catch (RuntimeException ex) {
                    index = null;
                }
            } else {
                try {
                    if (index.hasNext()) {
                        return true;
                    }
                    index = null;
                } catch (RuntimeException ex) {
                    LOG.log(Level.SEVERE, "error evaluating Iterator 'hasNext()' : " + ex.toString(), ex);
                    other = null;
                    return false;
                }
            }
        }
        if (other != null) {
            return other.hasNext();
        }
        return false;
    }

    @Override
    public T next() {
        if (hasNextItem) {
            hasNextItem = false;
            T t = nextItem;
            nextItem = null;
            return t;
        }
        if (index != null) {
            return index.next();
        }
        return other.next();
    }

    @Override
    public void remove() {
        if (index != null) {
            index.remove();
        } else if (other != null) {
            other.remove();
        }
    }

    @Override
    public String toString() {
        return "IndexFirstIterator(" + other + ")";
    }
}

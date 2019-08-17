/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.util.Iterator;

/**
 *
 * @author vpc
 */
class IteratorSizeCounter<T> implements Iterator<T> {
    
    private Iterator<T> base;
    private int count;

    public IteratorSizeCounter(Iterator<T> base) {
        this.base = base;
    }

    @Override
    public boolean hasNext() {
        return base.hasNext();
    }

    @Override
    public T next() {
        T v = base.next();
        count++;
        return v;
    }

    public int getCount() {
        return count;
    }

    @Override
    public void remove() {
        base.remove();
        count--;
    }
    
    
}

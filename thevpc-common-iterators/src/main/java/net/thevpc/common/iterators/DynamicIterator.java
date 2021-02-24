/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author thevpc
 */
public class DynamicIterator<T> implements Iterator<T> {

    private LinkedList<Iterator<T>> its = new LinkedList<>();
    private LinkedList<Action<T>> actions = new LinkedList<>();

    public void add(Iterator<T> it) {
        its.add(it);
    }

    public void add(Action<T> it) {
        actions.add(it);
    }

    public static interface Action<T> {

        List<Iterator<T>> later();
    }

    @Override
    public boolean hasNext() {
        while (true) {
            while (true) {
                Iterator<T> it0 = its.peek();
                if (it0 != null) {
                    if (it0.hasNext()) {
                        return true;
                    }
                } else {
                    break;
                }
            }
            boolean ok = false;
            while (true) {
                Action<T> a = actions.poll();
                if (a != null) {
                    List<Iterator<T>> p = a.later();
                    if (p != null) {
                        for (Iterator<T> it : p) {
                            if (it != null) {
                                add(it);
                                ok = true;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            if (!ok) {
                return false;
            }
        }
    }

    @Override
    public T next() {
        return its.peek().next();
    }

    @Override
    public void remove() {
        its.peek().remove();
    }

}

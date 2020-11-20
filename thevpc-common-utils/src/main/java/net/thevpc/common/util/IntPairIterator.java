package net.thevpc.common.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class IntPairIterator implements Iterator<IntTuple2> {
    public  static Comparator<IntTuple2> COMPARATOR = new IntTuple2Comparator();
    private IntTuple2 current0;
    private IntTuple2 current;

    public IntPairIterator() {
        this(0, 0);
    }

    public IntPairIterator(int a, int b) {
        this.current0 = new IntTuple2(a, b);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public IntTuple2 next() {
        if (current == null) {
            current = current0;
            return current;
        } else {
            int a = current.getValue1();
            int b = current.getValue2();
            int sum = a + b;
            if (b == 0) {
                return current = new IntTuple2(0, sum + 1);
            } else {
                return current = new IntTuple2(a + 1, b - 1);
            }
        }
    }

    public List<IntTuple2> next(int count) {
        List<IntTuple2> a = new ArrayList<>(count);
        while (count > 0) {
            a.add(next());
            count--;
        }
        return a;
    }

    private static class IntTuple2Comparator implements Comparator<IntTuple2> {
        @Override
        public int compare(IntTuple2 o1, IntTuple2 o2) {
            int s1 = o1.getValue1() + o1.getValue2();
            int s2 = o2.getValue1() + o2.getValue2();
            int c = Integer.compare(s1, s2);
            if (c != 0) {
                return c;
            }
            return Integer.compare(o1.getValue1(), o2.getValue1());
        }
    }
}

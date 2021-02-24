package net.thevpc.common.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntIteratorByMax implements IntIterator {
    private int[] start;
    private int[] current;
    private int[] max;
    private boolean started;
    private boolean finished;
    private long maxElements;
    private long visitedElements;

    private IntIteratorByMax(int[] start, int[] max,long maxElements) {
        if (max == null || max.length == 0) {
            throw new IllegalArgumentException("Invalid init tuple");
        }
        if (start == null) {
            start = new int[max.length];
        }
        if (start.length != max.length) {
            throw new IllegalArgumentException("Invalid tuple dimension " + start.length + " <> " + max.length);
        }
        this.max = max;
        this.start = start;
        this.maxElements = maxElements;
    }

    public static IntIteratorByMax fromTo(int[] start, int[] max,long maxElements) {
        return new IntIteratorByMax(start, max,maxElements);
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public void next(int[] recipient) {
        if (current == null || finished) {
            throw new IllegalArgumentException("No more elements or you missed to call hasNext()");
        }
        System.arraycopy(current, 0, recipient, 0, current.length);
    }

    @Override
    public List<int[]> next(int count) {
        List<int[]> a = new ArrayList<>(count);
        while (count > 0 && hasNext()) {
            int[] next = next();
            a.add(next);
            count--;
        }
        return a;
    }

    @Override
    public boolean hasNext() {
        if (finished) {
            return false;
        }
        if (!started) {
            started = true;
            if(maxElements==0){
                finished=true;
                return false;
            }
            boolean ok = false;
            for (int i = 0; i < start.length; i++) {
                if (start[i] < max[i]) {
                    ok = true;
                }
            }
            if (ok) {
                this.current = Arrays.copyOf(start, start.length);
                visitedElements++;
                return true;
            } else {
                visitedElements++;
                finished = true;
                return false;
            }
        } else {
            int depth = 0;
            while (depth < current.length) {
                current[depth]++;
                if (current[depth] >= max[depth]) {
                    current[depth] = start[depth];
                    depth++;
                } else {
                    break;
                }
            }
            if (depth >= current.length || (maxElements>0 && visitedElements>maxElements)) {
                finished = true;
                return false;
            }
            visitedElements++;
            return true;
        }
    }

    @Override
    public int[] next() {
        if (current == null || finished) {
            throw new IllegalArgumentException("No more elements or you missed to call hasNext()");
        }
        return Arrays.copyOf(current, current.length);
    }

}

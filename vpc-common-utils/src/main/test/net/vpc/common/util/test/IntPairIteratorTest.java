package net.vpc.common.util.test;

import net.vpc.common.util.IntTuple2;
import net.vpc.common.util.IntPairIterator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class IntPairIteratorTest {

    @Test
    public void testOk() {
        IntPairIterator it = new IntPairIterator();
        List<IntTuple2> sorted = withSort(20, 20);
        List<IntTuple2> fast = it.next(sorted.size()/2);
        boolean error = false;
        for (int j = 0; j < fast.size(); j++) {
            IntTuple2 v1 = fast.get(j);
            IntTuple2 v2 = sorted.get(j);
            boolean ok = v1.equals(v2);
            System.out.printf("%-3d %-3d %-3d <==> %-3d %-3d %-3d %s%n", v1.getValue1(), v1.getValue2(), v1.getValue1() + v1.getValue2(), v2.getValue1(), v2.getValue2(), v2.getValue1() + v2.getValue2(), ok ? "" : "ERROR");
            if (!ok) {
                error = true;
            }
        }
        Assertions.assertFalse(error);
    }

    public static List<IntTuple2> withSort(int aa, int bb) {
        List<IntTuple2> a = new ArrayList<>();
        for (int i = 0; i < aa; i++) {
            for (int j = 0; j < bb; j++) {
                a.add(new IntTuple2(i, j));
            }
        }
        a.sort(IntPairIterator.COMPARATOR);
//        for (IntTuple2 i : a) {
//        }
        return a;
    }
}

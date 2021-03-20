package net.thevpc.common.iterators.test;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.thevpc.common.iterators.IntIterator;
import net.thevpc.common.iterators.IntIteratorBuilder;

public class IntTupleIteratorTest {

    public static List<int[]> withSort(int aa, int bb) {
        List<int[]> a = new ArrayList<>();
        for (int i = 0; i < aa; i++) {
            for (int j = 0; j < bb; j++) {
                a.add(new int[]{i, j});
            }
        }
        a.sort(IntIteratorBuilder.SUM_COMPARATOR);
//        for (IntTuple2 i : a) {
//        }
        return a;
    }

    @Test
    public void test2() {
//        System.out.println("--------------------------");
//        dump(IntIteratorBuilder.iterateBySum(2),10);
//        System.out.println("--------------------------");
//        List<int[]> next = IntIteratorBuilder.iter().to(6, 6, 6,6).depthFirst().next(100000);
//        next.sort(IntIteratorBuilder.SUM_COMPARATOR);

        dump(IntIteratorBuilder.iter().from(0, 0, 0, 0).to(4,4,1,4).breadthFirst());

//        dumpDiff(next.iterator(), IntIteratorBuilder.iter().to(1, 2, 3, 4).breadthFirst());
//        dump(next);
//        List<int[]> sorted = withSort(20, 20);
//        List<int[]> fast = it.next(sorted.size() / 2);
//        boolean error = false;
//        for (int j = 0; j < fast.size(); j++) {
//            int[] v1 = fast.get(j);
//            int[] v2 = sorted.get(j);
//            boolean ok = Arrays.equals(v1, v2);
//            System.out.printf("%-3d %-3d %-3d <==> %-3d %-3d %-3d %s%n", v1[0], v1[1], v1[0] + v1[1], v2[0], v2[1], v2[0] + v2[1], ok ? "" : "ERROR");
//            if (!ok) {
//                error = true;
//            }
//        }
//        Assertions.assertFalse(error);
    }

    protected void dump(Iterator<int[]> it) {
        while (it.hasNext()) {
            printf(it.next());
            System.out.println();
        }
    }

    protected void dumpDiff(Iterator<int[]> ione, Iterator<int[]> itwo) {
        while (true) {
            int[] v1 = null;
            int[] v2 = null;
            if (ione.hasNext()) {
                v1 = ione.next();
            }
            if (itwo.hasNext()) {
                v2 = itwo.next();
            }
            if (v1 == null || v2 == null) {
                break;
            }
            boolean ok = Arrays.equals(v1, v2);
            printf(v1);
            System.out.print(" <==> ");
            printf(v2);
            if (!ok) {
                System.out.print(" ERROR");
            }
            System.out.println();
        }
    }

    private static void printf(int[] a) {
        StringBuilder sb = new StringBuilder();
        List<Object> args = new ArrayList<>();
        for (int value : a) {
            sb.append("%-3d ");
            args.add(value);
        }
        sb.append("(%-3d)");
        args.add(sum(a));
        System.out.printf(sb.toString(), args.toArray(new Object[0]));
    }
    private static long sum(int[] a) {
        long x = a[0];
        for (int i = 1; i < a.length; i++) {
            x += a[i];
        }
        return x;
    }
    protected void dump(IntIterator it, int count) {
        for (int[] r : it.next(count)) {
            printf(r);
            System.out.println();
        }
    }

//    @Test
//    public void test3() {
//        int dim=3;
//        IntIteratorByMax it = IntIteratorByMax.ofDim(dim);
//        int[] r=new int[dim];
//        Arrays.fill(r,20);
//        List<int[]> sorted = withSort(r);
//        List<int[]> fast = it.next(sorted.size() / 2);
//        boolean error = false;
//        for (int j = 0; j < fast.size(); j++) {
//            int[] v1 = fast.get(j);
//            int[] v2 = sorted.get(j);
//            boolean ok = Arrays.equals(v1, v2);
//            printf(v1);
//            System.out.print(" <==> ");
//            printf(v2);
//            if(ok){
//                System.out.print(" ERROR");
//            }
//            System.out.println();
//            if (!ok) {
//                error = true;
//            }
//        }
//        Assertions.assertFalse(error);
//    }
//
//    public static List<int[]> withSort(int aa, int bb, int cc) {
//        List<int[]> a = new ArrayList<>();
//        for (int i = 0; i < aa; i++) {
//            for (int j = 0; j < bb; j++) {
//                for (int k = 0; k < cc; k++) {
//                    a.add(new int[]{i, j, k});
//                }
//            }
//        }
//        a.sort(IntIteratorByMax.COMPARATOR);
////        for (IntTuple2 i : a) {
////        }
//        return a;
//    }

    protected void dump(Iterable<int[]> it) {
        for (int[] r : it) {
            printf(r);
            System.out.println();
        }
    }
}

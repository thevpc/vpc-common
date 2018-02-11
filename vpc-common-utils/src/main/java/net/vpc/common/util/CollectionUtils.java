package net.vpc.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    public static <T> List<T> head(List<T> anyList, int maxSize) {
        if (anyList.size() > maxSize) {
            return anyList.subList(0, maxSize);
        }
        return anyList;
    }

    public static <T> List<T> tail(List<T> anyList, int maxSize) {
        if (anyList.size() > maxSize) {
            return anyList.subList(anyList.size() - maxSize, maxSize);
        }
        return anyList;
    }

    public static <T> List<List<T>> splitBy(Collection<T> anyList,int groupSize) {
        List<List<T>> grouped = new ArrayList<>();
        for (int i = 0; i < groupSize; i++) {
            grouped.add(new ArrayList<T>());
        }
        if (anyList != null) {
            int i=0;
            for (T item : anyList) {
                grouped.get(i % groupSize).add(item);
                i++;
            }
        }
        return grouped;
    }

    public static <T> List<List<T>> groupBy(Collection<T> anyList,int groupSize) {
        List<List<T>> grouped = new ArrayList<>();
        List<T> curr = new ArrayList<>();
        if (anyList != null) {
            for (T item : anyList) {
                if (curr.size() < groupSize) {
                    curr.add(item);
                } else {
                    grouped.add(curr);
                    curr = new ArrayList<>();
                    curr.add(item);
                }
            }
        }
        if (curr.size() > 0) {
            grouped.add(curr);
        }
        return grouped;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author vpc
 */
public class CollectionDiff<T> {

    private List<T> unchanged;
    private List<T> added;
    private List<T> removed;

    public static <T> CollectionDiff of(Collection<T> a, Collection<T> b) {
        List<T> unchanged = new ArrayList<>();
        List<T> added = new ArrayList<>();
        List<T> removed = new ArrayList<>();
        Map<T, ItemDiff<T>> collected = new HashMap<>();

        for (T t : a) {
            ItemDiff<T> c = collected.get(t);
            if (c == null) {
                c = new ItemDiff<>(t);
                collected.put(t, c);
            }
            c.was++;
        }
        for (T t : b) {
            ItemDiff<T> c = collected.get(t);
            if (c == null) {
                c = new ItemDiff<>(t);
                collected.put(t, c);
            }
            c.becomes++;
        }
        for (ItemDiff<T> value : collected.values()) {
            int d = value.becomes - value.was;
            int min = Math.min(value.becomes, value.was);
            if (min > 0) {
                for (int i = 0; i < min; i++) {
                    unchanged.add(value.value);
                }
            }
            if (d > 0) {
                for (int i = value.was; i < value.becomes; i++) {
                    added.add(value.value);
                }
            }
            if (d < 0) {
                for (int i = value.becomes; i < value.was; i++) {
                    removed.add(value.value);
                }
            }
        }
        return new CollectionDiff(unchanged, added, removed);
    }

    public static <T, K> CollectionDiff of(Collection<T> a, Collection<T> b, Function<T, K> idResolver) {
        if (idResolver == null) {
            throw new NullPointerException("idResolver cannot be null");
        }
        List<T> unchanged = new ArrayList<>();
        List<T> added = new ArrayList<>();
        List<T> removed = new ArrayList<>();
        Map<K, ItemDiff<T>> collected = new HashMap<>();

        for (T t : a) {
            K k = idResolver.apply(t);
            ItemDiff<T> c = collected.get(k);
            if (c == null) {
                c = new ItemDiff<>(t);
                collected.put(k, c);
            }
            c.was++;
        }
        for (T t : b) {
            K k = idResolver.apply(t);
            ItemDiff<T> c = collected.get(k);
            if (c == null) {
                c = new ItemDiff<>(t);
                collected.put(k, c);
            }
            c.becomes++;
        }
        for (ItemDiff<T> value : collected.values()) {
            int d = value.becomes - value.was;
            int min = Math.min(value.becomes, value.was);
            if (min > 0) {
                for (int i = 0; i < min; i++) {
                    unchanged.add(value.value);
                }
            }
            if (d > 0) {
                for (int i = value.was; i < value.becomes; i++) {
                    added.add(value.value);
                }
            }
            if (d < 0) {
                for (int i = value.becomes; i < value.was; i++) {
                    removed.add(value.value);
                }
            }
        }
        return new CollectionDiff(unchanged, added, removed);
    }

    private static class ItemDiff<T> {

        T value;
        int was;
        int becomes;

        public ItemDiff(T value) {
            this.value = value;
        }

    }

    public CollectionDiff(Collection<T> unchanged, Collection<T> added, Collection<T> removed) {
        this.unchanged = unchanged == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<T>(unchanged));
        this.added = added == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<T>(added));
        this.removed = removed == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<T>(removed));
    }

    public List<T> getUnchanged() {
        return unchanged;
    }

    public List<T> getAdded() {
        return added;
    }

    public List<T> getRemoved() {
        return removed;
    }

    @Override
    public String toString() {
        return "CollectionDiff{" + "unchanged=" + unchanged + ", added=" + added + ", removed=" + removed + '}';
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iterators;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author thevpc
 */
public class IteratorBuilder<T> {

    private final Iterator<T> it;

    private IteratorBuilder(Iterator<T> it) {
        if (it == null) {
            it = IteratorUtils.emptyIterator();
        }
        this.it = it;
    }

    public static <T> IteratorBuilder<T> ofCoalesce(List<Iterator<T>> t) {
        return new IteratorBuilder<>(
                IteratorUtils.coalesce(t)
        );
    }

    public static <T> IteratorBuilder<T> ofList(List<Iterator<T>> t) {
        return new IteratorBuilder<>(
                IteratorUtils.concat(t)
        );
    }

    public static <T> IteratorBuilder<T> of(Iterator<T> t) {
        return new IteratorBuilder<>(t);
    }

    public static <T> IteratorBuilder<T> ofLazy(Iterable<T> t) {
        return new IteratorBuilder<>(
                new LazyIterator(t)
        );
    }

    public static IteratorBuilder<File> ofFileDfs(File file) {
        return of(new FileDepthFirstIterator(file));
    }

    public static <T> IteratorBuilder<T> ofArray(T... t) {
        return of(t == null ? IteratorUtils.<T>emptyIterator() : Arrays.asList(t).iterator());
    }

    public static IteratorBuilder<File> ofFileList(File file) {
        return ofArray(file.listFiles());
    }

    public static IteratorBuilder<File> ofFileList(File file, boolean intcludeSelf) {
        if (intcludeSelf) {
            return ofArray(file).concat(ofArray(file.listFiles()));
        }
        return ofArray(file.listFiles());
    }

    public IteratorBuilder<T> filter(Predicate<T> t) {
        if (t == null) {
            return this;
        }
        return new IteratorBuilder<>(new FilteredIterator<>(it, t));
    }

    public IteratorBuilder<T> concat(IteratorBuilder<T> t) {
        return concat(t.it);
    }

    public IteratorBuilder<T> concat(Iterator<T> t) {
        if (t == null) {
            return this;
        }
        return new IteratorBuilder<>(IteratorUtils.concat(Arrays.asList(it, t)));
    }

    public <V> IteratorBuilder<V> map(Function<T, V> t) {
        return new IteratorBuilder<>(new ConvertedIterator<>(it, t, null));
    }

    public <V> IteratorBuilder<V> map(Function<T, V> t, String name) {
        return new IteratorBuilder<>(new ConvertedIterator<>(it, t, name));
    }

    public <V> IteratorBuilder<V> convert(Function<T, V> t) {
        return new IteratorBuilder<>(new ConvertedIterator<>(it, t, null));
    }

    public <V> IteratorBuilder<V> convert(Function<T, V> t, String name) {
        return new IteratorBuilder<>(new ConvertedIterator<>(it, t, name));
    }

    public <V> IteratorBuilder<V> mapMulti(Function<T, List<V>> t) {
        return new IteratorBuilder<>(new ConvertedToListIterator<>(it, t));
    }

    public <V> IteratorBuilder<V> convertMulti(Function<T, List<V>> t) {
        return new IteratorBuilder<>(new ConvertedToListIterator<>(it, t));
    }

    public <V> IteratorBuilder<T> sort(Comparator<T> t, boolean removeDuplicates) {
        return new IteratorBuilder<>(IteratorUtils.sort(it, t, true));
    }

    public <V> IteratorBuilder<T> distinct(Function<T, V> t) {
        if (t == null) {
            return new IteratorBuilder<>(IteratorUtils.distinct(it));
        } else {
            return new IteratorBuilder<>(IteratorUtils.distinct(it, t));
        }
    }

    public IteratorBuilder<T> safe(IteratorErrorHandlerType type) {
        return new IteratorBuilder<>(new ErrorHandlerIterator(type, it));
    }

    public IteratorBuilder<T> safeIgnore() {
        return safe(IteratorErrorHandlerType.IGNORE);
    }

    public IteratorBuilder<T> safePospone() {
        return safe(IteratorErrorHandlerType.POSPONE);
    }

    public IteratorBuilder<T> notNull() {
        return filter(IteratorUtils.NON_NULL);
    }

    public IteratorBuilder<String> notBlank() {
        return ((IteratorBuilder<String>) this).filter(IteratorUtils.NON_BLANK);
    }

    public Iterator<T> iterator() {
        return it;
    }

    public List<T> list() {
        List<T> all = new ArrayList<>();
        while (it.hasNext()) {
            all.add(it.next());
        }
        return all;
    }

    public Iterator<T> build() {
        return it;
    }

}

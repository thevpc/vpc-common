/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props.impl;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.thevpc.common.props.ObservableList;
import net.thevpc.common.props.PropertyListeners;
import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.PropertyVetos;
import net.thevpc.common.props.UserObjects;
import net.thevpc.common.props.WritableList;

/**
 *
 * @author vpc
 */
public abstract class WritableListAdapter<T> implements WritableList<T>{
    protected abstract WritableList<T> getAdaptee();

    @Override
    public void removeAll() {
        getAdaptee().removeAll();
    }

    @Override
    public void removeAll(Predicate<T> a) {
        getAdaptee().removeAll(a);
    }

    @Override
    public void add(int index, T v) {
        getAdaptee().add(index, v);
    }

    @Override
    public void add(T v) {
        getAdaptee().add(v);
    }

    @Override
    public void set(int index, T v) {
        getAdaptee().set(index, v);
    }

    @Override
    public T remove(int index) {
        return getAdaptee().remove(index);
    }

    @Override
    public boolean remove(T item) {
        return getAdaptee().remove(item);
    }

    @Override
    public PropertyVetos vetos() {
        return getAdaptee().vetos();
    }

    @Override
    public T get(int index) {
        return getAdaptee().get(index);
    }

    @Override
    public int size() {
        return getAdaptee().size();
    }

    @Override
    public int[] findAllIndexes(Predicate<T> a) {
        return getAdaptee().findAllIndexes(a);
    }

    @Override
    public List<T> findAll(Predicate<T> a) {
        return getAdaptee().findAll(a);
    }

    @Override
    public T findFirst(Predicate<T> a) {
        return getAdaptee().findFirst(a);
    }

    @Override
    public T findFirst(Predicate<T> a, int from) {
        return getAdaptee().findFirst(a,from);
    }

    @Override
    public T findFirst(Predicate<T> a, int from, int to) {
        return getAdaptee().findFirst(a,from,to);
    }

    @Override
    public int findFirstIndex(Predicate<T> a) {
        return getAdaptee().findFirstIndex(a);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from) {
        return getAdaptee().findFirstIndex(a,from);
    }

    @Override
    public int findFirstIndex(Predicate<T> a, int from, int to) {
        return getAdaptee().findFirstIndex(a,from,to);
    }

    @Override
    public List<T> toList() {
        return getAdaptee().toList();
    }

    @Override
    public ObservableList<T> readOnly() {
        return getAdaptee().readOnly();
    }

    @Override
    public String name() {
        return getAdaptee().name();
    }

    @Override
    public PropertyType type() {
        return getAdaptee().type();
    }

    @Override
    public boolean isWritable() {
        return getAdaptee().isWritable();
    }

    @Override
    public UserObjects userObjects() {
        return getAdaptee().userObjects();
    }

    @Override
    public PropertyListeners listeners() {
        return getAdaptee().listeners();
    }

    @Override
    public Iterator<T> iterator() {
        return getAdaptee().iterator();
    }

    @Override
    public boolean contains(T a) {
        return getAdaptee().contains(a);
    }
    
}

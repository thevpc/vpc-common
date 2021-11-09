/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import net.thevpc.common.props.*;

/**
 *
 * @author thevpc
 */
public abstract class WritableListAdapter<T> implements WritableList<T>{
    protected abstract WritableList<T> getAdaptee();

    @Override
    public void clear() {
        getAdaptee().clear();
    }

    @Override
    public void removeAll(Predicate<T> a) {
        getAdaptee().removeAll(a);
    }

    @Override
    public boolean add(int index, T v) {
        return getAdaptee().add(index, v);
    }

    @Override
    public boolean add(T v) {
        return getAdaptee().add(v);
    }

    @Override
    public void set(int index, T v) {
        getAdaptee().set(index, v);
    }

    @Override
    public T removeAt(int index) {
        return getAdaptee().removeAt(index);
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
    public String propertyName() {
        return getAdaptee().propertyName();
    }

    @Override
    public PropertyType propertyType() {
        return getAdaptee().propertyType();
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
    public PropertyListeners events() {
        return getAdaptee().events();
    }

    @Override
    public Iterator<T> iterator() {
        return getAdaptee().iterator();
    }

    @Override
    public boolean contains(T a) {
        return getAdaptee().contains(a);
    }

    @Override
    public boolean addAll(T... extra) {
        return getAdaptee().addAll(extra);
    }
    @Override
    public boolean removeAll(T... extra) {
        return getAdaptee().removeAll(extra);
    }

    @Override
    public T get() {
        return getAdaptee().get();
    }

    @Override
    public void set(T value) {
        getAdaptee().set(value);
    }

    @Override
    public boolean isEmpty() {
        return getAdaptee().isEmpty();
    }

    @Override
    public boolean setAll(T... extra) {
        return getAdaptee().setAll(extra);
    }

    @Override
    public boolean setCollection(Collection<? extends T> all) {
        return getAdaptee().setCollection(all);
    }

    @Override
    public boolean addCollection(Collection<? extends T> all) {
        return getAdaptee().addCollection(all);
    }

    @Override
    public boolean removeCollection(Collection<? extends T> all) {
        return getAdaptee().removeCollection(all);
    }
    

    @Override
    public PropertyAdjusters adjusters() {
        return getAdaptee().adjusters();
    }

    @Override
    public String toString() {
        return getAdaptee().toString();
    }
    
}

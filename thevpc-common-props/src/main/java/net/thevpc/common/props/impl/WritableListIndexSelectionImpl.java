package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WritableListIndexSelectionImpl<T> extends WritableListAdapter<T> implements WritableListIndexSelectionExt<T> {
    private ObservableListIndexSelectionExt<T> ro;
    private ObservableList<T> possibleValues;
    private WritableList<Integer> selectedIndices;
    private WritableList<T> selectedValues;
    private WritableValue<Predicate<T>> disablePredicate;
    private WritableValue<DisabledSelectionStrategy> disableSelectionStrategy;
    private WritableList<T> disabledValues;
    private List<ObjAndIndex<T>> selectedValuesAndIndices=new ArrayList<>();
    private WritableBoolean multipleSelection;
    //TODO Fix me then update ToggleControlGroup
    private WritableBoolean noSelection;
    private String propertyName;
    private int lastIndex = -1;
    private WritableValue<Predicate<T>> effectiveDisablePredicate;

    public WritableListIndexSelectionImpl(String name, PropertyType elementType, ObservableList<T> possibleValues) {
        this.propertyName = name;
        this.possibleValues = possibleValues;
        this.multipleSelection = Props.of("multipleSelection").booleanOf(true);
        this.noSelection = Props.of("noSelection").booleanOf(true);
        this.disabledValues = Props.of("disabledValues").listOf(elementType);
        this.disableSelectionStrategy = Props.of("disableSelectionStrategy").valueOf(DisabledSelectionStrategy.class,DisabledSelectionStrategy.IGNORE);
        this.disablePredicate = Props.of("disablePredicate").valueOf(
                PropertyType.of(Predicate.class,elementType)
        );
        this.effectiveDisablePredicate = Props.of("effectiveDisablePredicate").valueOf(
                PropertyType.of(Predicate.class,elementType),new EffPredicate()
        );
        this.disabledValues.onChange(()->this.effectiveDisablePredicate.set(new EffPredicate()));
        this.disablePredicate.onChange(()->this.effectiveDisablePredicate.set(new EffPredicate()));

        WritableListIndexSelectionImpl<T> a = this;
        this.selectedIndices = new WritableListBase<Integer>("indices", PropertyType.of(Integer.class)) {
            @Override
            protected boolean addImpl(int index, Integer v) {
                ObjAndIndex<T> q = findByIndex(v);
                if (q != null) {
                    _add(index, q);
                    return true;
                }
                return false;
            }

            @Override
            protected Integer setImpl(int index, Integer v) {
                Integer old = get(index);
                _set(index, new ObjAndIndex<>(
                        possibleValues.get(v),
                        v
                ));
                return old;
            }

            @Override
            protected int indexOfImpl(Integer v) {
                return selectedValuesAndIndices.stream().filter(x -> x.index == v)
                        .map(x->x.index)
                        .findFirst().orElse(-1);
            }

            @Override
            protected Integer removeAtImpl(int index) {
                Integer old = get(index);
                ObjAndIndex<T> q = findByIndex(index);
                if (q != null) {
                    _remove(index);
                    return old;
                }
                return null;
            }

            @Override
            protected Integer getImpl(int index) {
                return _get(index).index;
            }

            @Override
            protected int sizeImpl() {
                return selectedValuesAndIndices.size();
            }
        };

        this.selectedValues = new WritableListBase<T>("values", elementType) {
            @Override
            protected boolean addImpl(int index, T v) {
                ObjAndIndex<T> q = findByValue(v);
                if (q != null) {
                    _add(index, q);
                    return true;
                }
                return false;
            }

            @Override
            protected T setImpl(int index, T v) {
                ObjAndIndex<T> old = _get(index);
                ObjAndIndex<T> newValue = findByValue(v);
                if (newValue != null) {
                    _set(index, newValue);
                }
                return old.value;
            }

            @Override
            protected int indexOfImpl(T v) {
                int i=0;
                for (ObjAndIndex<T> x : selectedValuesAndIndices) {
                    if(Objects.equals(x.value,v)){
                        return i;
                    }
                    i++;
                }
                return -1;
//                return selectedValuesAndIndices.stream().filter(x -> Objects.equals(x.value,v))
//                        .map(x->x.index)
//                        .findFirst().orElse(-1);
            }

            @Override
            protected T removeAtImpl(int index) {
                T old = get(index);
                ObjAndIndex<T> q = findByIndex(index);
                if (q != null) {
                    _remove(index);
                    return old;
                }
                return null;
            }

            @Override
            protected T getImpl(int index) {
                return _get(index).value;
            }

            @Override
            protected int sizeImpl() {
                return selectedValuesAndIndices.size();
            }
        };
        possibleValues.onChange(c -> {
            switch (c.eventType()) {
                case ADD: {
                    break;
                }
                case UPDATE: {
                    T o = c.oldValue();
                    WritableListIndexSelectionImpl.this.remove(o);
                    break;
                }
                case REMOVE: {
                    T o = c.oldValue();
                    WritableListIndexSelectionImpl.this.remove(o);
                    break;
                }
            }
        });
        multipleSelection.onChange(event -> {
            boolean multi = event.newValue();
            if (!multi) {
                int s = indices().size();
                if (s > 0) {
                    removeAllIndicesBut(s - 1);
                }
            }
        });
    }

    private boolean _add(int index, ObjAndIndex<T> a) {
        if (a != null) {
            if (!selectedValuesAndIndices.contains(a)) {
                if (effectiveDisablePredicate.get().test(a.value)) {
                    DisabledSelectionStrategy s = disableSelectionStrategy.get();
                    if(s==null){
                        s=DisabledSelectionStrategy.IGNORE;
                    }
                    switch (s){
                        case IGNORE:{
                            return false;
                        }
                        case SELECT_NEXT:{
                            int q = PropsHelper.bestSelectableIndex(possibleValues,
                                    effectiveDisablePredicate.get(), a.index, lastIndex);
                            if(q>=0){
                                return _add(index,new ObjAndIndex<>(
                                        possibleValues.get(q),
                                        q
                                ));
                            }
                            return false;
                        }
                    }
                }

                if (!multipleSelection.get()) {
                    while (selectedValuesAndIndices.size() > 0) {
                        _remove(0);
                    }
                    if(index>selectedValuesAndIndices.size()){
                        index=selectedValuesAndIndices.size();
                    }
                }
                selectedValuesAndIndices.add(index, a);
                lastIndex=a.index;
                ((DefaultPropertyListeners) selectedIndices.events())
                        .firePropertyUpdated(new PropertyEvent(
                                selectedIndices, index, null, a.index, Path.of(propertyName()).append("indices"), PropertyUpdate.ADD, true
                        ));
                ((DefaultPropertyListeners) selectedValues.events())
                        .firePropertyUpdated(new PropertyEvent(
                                selectedValues, index, null, a.value, Path.of(propertyName()).append("values"), PropertyUpdate.ADD, true
                        ));
                return true;
            }
        }
        return false;
    }

    private boolean _set(int index, ObjAndIndex<T> a) {
        ObjAndIndex<T> old = selectedValuesAndIndices.get(index);
        if (!Objects.equals(old, a)) {
            if (selectedValuesAndIndices.contains(a)) {
                _remove(index);
            } else {
                if (effectiveDisablePredicate().get().test(a.value)) {
                    DisabledSelectionStrategy s = disableSelectionStrategy.get();
                    if(s==null){
                        s=DisabledSelectionStrategy.IGNORE;
                    }
                    switch (s){
                        case IGNORE:{
                            return false;
                        }
                        case SELECT_NEXT:{
                            int q = PropsHelper.bestSelectableIndex(possibleValues,
                                    effectiveDisablePredicate.get(), a.index, lastIndex);
                            if(q>=0){
                                return _set(index,new ObjAndIndex<>(
                                        possibleValues.get(q),
                                        q
                                ));
                            }
                            return false;
                        }
                    }
                }
                selectedValuesAndIndices.set(index, a);
                lastIndex=a.index;
                if (!Objects.equals(old.index, a.index)) {
                    ((DefaultPropertyListeners) selectedIndices.events())
                            .firePropertyUpdated(new PropertyEvent(
                                    selectedIndices, index, old.index, a.index, Path.of(propertyName()).append("indices"), PropertyUpdate.UPDATE, true
                            ));
                }
                if (!Objects.equals(old.value, a.value)) {
                    ((DefaultPropertyListeners) selectedValues.events())
                            .firePropertyUpdated(new PropertyEvent(
                                    selectedValues, index, old.value, a.value, Path.of(propertyName()).append("values"), PropertyUpdate.UPDATE, true
                            ));
                }
            }
        }
        return false;
    }

    private ObjAndIndex<T> _get(int index) {
        return selectedValuesAndIndices.get(index);
    }

    private void _remove(int index) {
        //disable clearing the selection if noSelection is false
        if (!noSelection().get() && index == 0 && selectedValuesAndIndices.size() == 1) {
            return;
        }
        if (index<0 || index>=selectedValuesAndIndices.size()) {
            return;
        }
        ObjAndIndex<T> old = selectedValuesAndIndices.get(index);
        if(lastIndex>=index){
            lastIndex=index-1;
        }
        selectedValuesAndIndices.remove(index);
        ((DefaultPropertyListeners) selectedIndices.events())
                .firePropertyUpdated(new PropertyEvent(
                        selectedIndices, index, old.index, null, Path.of(propertyName()).append("indices"), PropertyUpdate.REMOVE, true
                ));
        ((DefaultPropertyListeners) selectedValues.events())
                .firePropertyUpdated(new PropertyEvent(
                        selectedValues, index, old.value, null, Path.of(propertyName()).append("values"), PropertyUpdate.REMOVE, true
                ));
    }

    private ObjAndIndex<T> findByIndex(int i) {
        if (i >= 0 && i < possibleValues.size()) {
            return new ObjAndIndex<>(possibleValues.get(i), i);
        }
        return null;
    }

    private ObjAndIndex<T> findByValue(T v) {
        int i = possibleValues.findFirstIndexOf(v);
        if (i < 0) {
            return null;
        }
        return new ObjAndIndex<>(v, i);
    }

    private void removeAllIndicesBut(int index) {
        for (int i = index + 1; i < indices().size(); i++) {
            indices().removeAt(index + 1);
        }
        for (int i = 0; i < index; i++) {
            indices().removeAt(0);
        }
    }

    public WritableList<Integer> indices() {
        return selectedIndices;
    }

    @Override
    protected WritableList<T> getAdaptee() {
        return selectedValues;
    }

    public WritableValue<Predicate<T>> disablePredicate() {
        return disablePredicate;
    }

    public WritableList<T> disabledValues() {
        return disabledValues;
    }

    @Override
    public ObservableListIndexSelectionExt<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyListIndexSelectionExt<T>(this);
        }
        return (ObservableListIndexSelectionExt<T>) ro;
    }

    @Override
    public String toString() {
        String p = isWritable() ? "Writable" : "ReadOnly";
        return p + "ListIndexSelection(" + propertyName() + "){"
                + ", noSelection=" + noSelection().get()
                + ", multipleSelection=" + multipleSelection().get()
                + ", values=[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + ", indices=[" + indices().stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + ", disabled=[" + disabledValues().stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + "}";
    }

    @Override
    public WritableBoolean multipleSelection() {
        return multipleSelection;
    }

    @Override
    public WritableBoolean noSelection() {
        return noSelection;
    }

    private static class ObjAndIndex<T> {
        T value;
        int index;

        public ObjAndIndex(T value, int index) {
            this.value = value;
            this.index = index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, index);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ObjAndIndex<?> that = (ObjAndIndex<?>) o;
            return index == that.index && Objects.equals(value, that.value);
        }
    }

    @Override
    public String propertyName() {
        return propertyName;
    }

    @Override
    public ObservableValue<Predicate<T>> effectiveDisablePredicate() {
        return effectiveDisablePredicate.readOnly();
    }

    public WritableValue<DisabledSelectionStrategy> disableSelectionStrategy() {
        return disableSelectionStrategy;
    }

    private class EffPredicate implements Predicate<T> {
        @Override
        public boolean test(T e) {
            return (WritableListIndexSelectionImpl.this.disablePredicate().get() != null && WritableListIndexSelectionImpl.this.disablePredicate().get().test(e))
                    || disabledValues.contains(e);
        }
    }
}

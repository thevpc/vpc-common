package net.thevpc.common.props;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import net.thevpc.common.props.impl.AbstractProperty;
import net.thevpc.common.props.impl.PDispatcherImpl;
import net.thevpc.common.props.impl.PropertyAdjustersImpl;
import net.thevpc.common.props.impl.WritableIndexedNodeImpl;
import net.thevpc.common.props.impl.WritableLiMapImpl;
import net.thevpc.common.props.impl.WritableListImpl;
import net.thevpc.common.props.impl.WritableMapImpl;
import net.thevpc.common.props.impl.WritableNamedNodeImpl;
import net.thevpc.common.props.impl.WritableStackImpl;
import net.thevpc.common.props.impl.WritableValueImpl;

public class Props {

    public static PropsBuilder of(String name) {
        return new PropsBuilder(name);
    }

    public static class PropsBuilder {

        protected List<PropertyAdjuster> adjusters = new ArrayList<>();
        protected List<PropertyVeto> vetos = new ArrayList<>();
        protected String name;

        public PropsBuilder(String name) {
            this.name = name;
        }

        public PropsBuilder veto(PropertyVeto v) {
            if (v != null) {
                vetos.add(v);
            }
            return this;
        }

        public PropsBuilder adjust(PropertyAdjuster v) {
            if (v != null) {
                adjusters.add(v);
            }
            return this;
        }

        public <T> WritableDispatcher<T> dispatcherOf(Class<T> type) {
            PDispatcherImpl<T> p = new PDispatcherImpl<T>(name, PropertyType.of(type));
            prepare(p);
            return p;
        }

        public <T> WritableValue<T> valueOf(Class<T> type, T value) {
            WritableValueImpl<T> p = new WritableValueImpl<T>(name, PropertyType.of(type), value);
            prepare(p);
            return p;
        }

        public <T> WritableIndexedNode<T> inodeOf(Class<T> type, T value) {
            WritableIndexedNodeImpl<T> p = new WritableIndexedNodeImpl<>(name, PropertyType.of(type));
            p.set(value);
            prepare(p);
            return p;
        }

        public <T> WritableNamedNode<T> nnodeOf(Class<T> type, T value) {
            WritableNamedNodeImpl<T> p = new WritableNamedNodeImpl<>(name, PropertyType.of(type));
            p.set(value);
            prepare(p);
            return p;
        }

        public <T> WritableList<T> listOf(PropertyType elementType) {
            WritableListImpl<T> p = new WritableListImpl<>(name, elementType);
            prepare(p);
            return p;
        }

        public <T> WritableList<T> listOf(Class<T> elementType) {
            WritableListImpl<T> p = new WritableListImpl<>(name, PropertyType.of(elementType));
            prepare(p);
            return p;
        }

        public <K, V> WritableMap<K, V> mapOf(Class<K> kType, Class<V> vType) {
            WritableMapImpl<K, V> p = new WritableMapImpl<>(name, PropertyType.of(kType), PropertyType.of(vType), new LinkedHashMap<>());
            prepare(p);
            return p;
        }

        public <K, V> WritableLiMap<K, V> lmapOf(Class<K> kType, Class<V> vType, Function<V, K> valueToKey) {
            WritableLiMapImpl<K, V> p = new WritableLiMapImpl<>(name, PropertyType.of(kType), PropertyType.of(vType), valueToKey, new LinkedHashMap<>());
            prepare(p);
            return p;
        }

        public <K, V> WritableLiMap<K, V> lmap2Of(Class<K> kType, Class<V> vType, Function<V, ObservableValue<K>> nameSupplier) {
            WritableLiMapImpl<K, V> createdList = new WritableLiMapImpl<>(this.name, PropertyType.of(kType), PropertyType.of(vType), (Function<V, K>) v -> nameSupplier.apply(v).get(), new LinkedHashMap<>());
            createdList.listeners().add(new PropertyListener() {
                @Override
                public void propertyUpdated(PropertyEvent event) {
                    if (event.getPath().equals("/")) {
                        K index = event.getOldValue();
                        V oldValue = event.getOldValue();
                        if (oldValue != null) {
                            ObservableValue<K> n = nameSupplier.apply(oldValue);
                            n.listeners().removeIf(x -> x instanceof NameUpdateLmap2Adjuster && ((NameUpdateLmap2Adjuster) x).p == createdList);
                        }
                        V newValue = event.getNewValue();
                        if (newValue != null) {
                            ObservableValue<K> n = nameSupplier.apply(newValue);
                            n.listeners().add(new NameUpdateLmap2Adjuster<>(createdList));
                        }
                    }
                }
            });
            prepare(createdList);
            return createdList;
        }

        public <K, V> WritableMap<K, V> mapOf(PropertyType kType, PropertyType vType) {
            WritableMapImpl<K, V> p = new WritableMapImpl<>(name, kType, vType, new LinkedHashMap<>());
            prepare(p);
            return p;
        }

        public <T> WritableList<T> linkedListOf(Class<T> elementType) {
            WritableListImpl<T> p = new WritableListImpl<>(name, PropertyType.of(elementType), new LinkedList<T>());
            prepare(p);
            return p;
        }

        public <T> WritableStack<T> stackOf(Class<T> elementType) {
            WritableStackImpl<T> p = new WritableStackImpl<>(name, PropertyType.of(elementType));
            prepare(p);
            return p;
        }

        public void prepare(AbstractProperty p) {
            for (PropertyAdjuster adjuster : adjusters) {
                p.adjusters().add(adjuster);
            }
            ((PropertyAdjustersImpl) p.adjusters()).readOnly(true);
            for (PropertyVeto adjuster : vetos) {
                p.vetos().add(adjuster);
            }
        }

        private static class NameUpdateLmap2Adjuster<K, V> implements PropertyListener {

            private final WritableLiMapImpl<K, V> p;

            public NameUpdateLmap2Adjuster(WritableLiMapImpl<K, V> p) {
                this.p = p;
            }

            @Override
            public void propertyUpdated(PropertyEvent event) {
                K k1 = event.getOldValue();
                V removed = p.remove(k1);
                if (removed != null) {
                    p.add(removed);
                }
            }
        }
    }
}

package net.vpc.common.props;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import net.vpc.common.props.impl.AbstractProperty;
import net.vpc.common.props.impl.PDispatcherImpl;
import net.vpc.common.props.impl.PropertyAdjustersImpl;
import net.vpc.common.props.impl.WritablePIndexedNodeImpl;
import net.vpc.common.props.impl.WritablePLMapImpl;
import net.vpc.common.props.impl.WritablePListImpl;
import net.vpc.common.props.impl.WritablePMapImpl;
import net.vpc.common.props.impl.WritablePNamedNodeImpl;
import net.vpc.common.props.impl.WritablePStackImpl;
import net.vpc.common.props.impl.WritablePValueImpl;

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

        public <T> WritablePDispatcher<T> dispatcherOf(Class<T> type) {
            PDispatcherImpl<T> p = new PDispatcherImpl<T>(name, PropertyType.of(type));
            prepare(p);
            return p;
        }

        public <T> WritablePValue<T> valueOf(Class<T> type, T value) {
            WritablePValueImpl<T> p = new WritablePValueImpl<T>(name, PropertyType.of(type), value);
            prepare(p);
            return p;
        }

        public <T> WritablePIndexedNode<T> inodeOf(Class<T> type, T value) {
            WritablePIndexedNodeImpl<T> p = new WritablePIndexedNodeImpl<>(name, PropertyType.of(type));
            p.set(value);
            prepare(p);
            return p;
        }

        public <T> WritablePNamedNode<T> nnodeOf(Class<T> type, T value) {
            WritablePNamedNodeImpl<T> p = new WritablePNamedNodeImpl<>(name, PropertyType.of(type));
            p.set(value);
            prepare(p);
            return p;
        }

        public <T> WritablePList<T> listOf(PropertyType elementType) {
            WritablePListImpl<T> p = new WritablePListImpl<>(name, elementType);
            prepare(p);
            return p;
        }

        public <T> WritablePList<T> listOf(Class<T> elementType) {
            WritablePListImpl<T> p = new WritablePListImpl<>(name, PropertyType.of(elementType));
            prepare(p);
            return p;
        }

        public <K, V> WritablePMap<K, V> mapOf(Class<K> kType, Class<V> vType) {
            WritablePMapImpl<K, V> p = new WritablePMapImpl<>(name, PropertyType.of(kType), PropertyType.of(vType), new HashMap<>());
            prepare(p);
            return p;
        }

        public <K, V> WritablePLMap<K, V> lmapOf(Class<K> kType, Class<V> vType, Function<V, K> valueToKey) {
            WritablePLMapImpl<K, V> p = new WritablePLMapImpl<>(name, PropertyType.of(kType), PropertyType.of(vType), valueToKey, new HashMap<>());
            prepare(p);
            return p;
        }

        public <K, V> WritablePLMap<K, V> lmap2Of(Class<K> kType, Class<V> vType, Function<V, PValue<K>> nameSupplier) {
            WritablePLMapImpl<K, V> createdList = new WritablePLMapImpl<>(this.name, PropertyType.of(kType), PropertyType.of(vType), (Function<V, K>) v -> nameSupplier.apply(v).get(), new HashMap<>());
            createdList.listeners().add(new PropertyListener() {
                @Override
                public void propertyUpdated(PropertyEvent event) {
                    if (event.getPath().equals("/")) {
                        K index = event.getOldValue();
                        V oldValue = event.getOldValue();
                        if (oldValue != null) {
                            PValue<K> n = nameSupplier.apply(oldValue);
                            n.listeners().removeIf(x -> x instanceof NameUpdateLmap2Adjuster && ((NameUpdateLmap2Adjuster) x).p == createdList);
                        }
                        V newValue = event.getNewValue();
                        if (newValue != null) {
                            PValue<K> n = nameSupplier.apply(newValue);
                            n.listeners().add(new NameUpdateLmap2Adjuster<>(createdList));
                        }
                    }
                }
            });
            prepare(createdList);
            return createdList;
        }

        public <K, V> WritablePMap<K, V> mapOf(PropertyType kType, PropertyType vType) {
            WritablePMapImpl<K, V> p = new WritablePMapImpl<>(name, kType, vType, new HashMap<>());
            prepare(p);
            return p;
        }

        public <T> WritablePList<T> linkedListOf(Class<T> elementType) {
            WritablePListImpl<T> p = new WritablePListImpl<>(name, PropertyType.of(elementType), new LinkedList<T>());
            prepare(p);
            return p;
        }

        public <T> WritablePStack<T> stackOf(Class<T> elementType) {
            WritablePStackImpl<T> p = new WritablePStackImpl<>(name, PropertyType.of(elementType));
            prepare(p);
            return p;
        }

        protected void prepare(AbstractProperty p) {
            for (PropertyAdjuster adjuster : adjusters) {
                p.adjusters().add(adjuster);
            }
            ((PropertyAdjustersImpl) p.adjusters()).readOnly(true);
            for (PropertyVeto adjuster : vetos) {
                p.vetos().add(adjuster);
            }
        }

        private static class NameUpdateLmap2Adjuster<K, V> implements PropertyListener {

            private final WritablePLMapImpl<K, V> p;

            public NameUpdateLmap2Adjuster(WritablePLMapImpl<K, V> p) {
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

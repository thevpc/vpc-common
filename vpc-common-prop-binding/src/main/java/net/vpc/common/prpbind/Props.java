package net.vpc.common.prpbind;

import net.vpc.common.prpbind.impl.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    }
}

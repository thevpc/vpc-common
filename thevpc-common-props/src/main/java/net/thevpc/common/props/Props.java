package net.thevpc.common.props;

import net.thevpc.common.props.configurators.MaxEntriesConfigurator;
import net.thevpc.common.props.configurators.OptionsConfigurator;
import net.thevpc.common.props.configurators.SetConfigurator;
import net.thevpc.common.props.impl.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Props {

    public static PropsBuilder of(String name) {
        return new PropsBuilder(name);
    }


    public static <T> void configureSet(WritableList<T> list) {
        SetConfigurator.configureSet(list);
    }

    public static <T, O> void configureOptions(WritableList<T> list, O options, OptionsConfigurator.OptionsApplier<T, O> optionsApplier) {
        OptionsConfigurator.configureOptions(list, options, optionsApplier);
    }

    public static <T, O> void configureObservableOptions(WritableList<T> list, ObservableValue<O> options, OptionsConfigurator.OptionsApplier<T, O> optionsApplier) {
        OptionsConfigurator.configureObservableOptions(list, options, optionsApplier);
    }

    public static <T> void configureObservableMaxEntries(WritableList<T> list, ObservableValue<Integer> maxMessageEntries) {
        MaxEntriesConfigurator.configureMaxEntries(list, maxMessageEntries);
    }

    public static <T> void configureMaxEntries(WritableList<T> list, int maxMessageEntries) {
        MaxEntriesConfigurator.configureMaxEntries(list, maxMessageEntries);
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

        public <T> WritableBoolean booleanOf(boolean value) {
            return (WritableBoolean) valueOf(boolean.class, value);
        }

        public <T> WritableBoolean booleanObjectOf(Boolean value) {
            return (WritableBoolean) valueOf(Boolean.class, value);
        }

        public <T> WritableString stringOf(String value) {
            return (WritableString) valueOf(String.class, value);
        }

        public <T> WritableInt intOf(int value) {
            return (WritableInt) valueOf(int.class, value);
        }

        public <T> WritableInt intObjectOf(Integer value) {
            return (WritableInt) valueOf(Integer.class, value);
        }

        public <T> WritableDouble doubleOf(double value) {
            return (WritableDouble) valueOf(double.class, value);
        }

        public <T> WritableDouble doubleObjectOf(Double value) {
            return (WritableDouble) valueOf(Double.class, value);
        }

        public <T> WritableFloat floatOf(float value) {
            return (WritableFloat) valueOf(float.class, value);
        }

        public <T> WritableFloat floatObjectOf(Float value) {
            return (WritableFloat) valueOf(Float.class, value);
        }

        public <T> WritableLong longOf(long value) {
            return (WritableLong) valueOf(long.class, value);
        }

        public <T> WritableLong longObjectOf(Long value) {
            return (WritableLong) valueOf(Long.class, value);
        }

        public <T> WritableValue<T> valueOf(Class<T> type) {
            return valueOf(type, null);
        }

        public <T> WritableValue<T> valueOf(PropertyType type) {
            return valueOf(type,null);
        }

        public <T> WritableValue<T> valueOf(PropertyType type, T value) {
            switch (type.getName()) {
                case "int": {
                    WritableInt p = new WritableIntImpl(name, false, value == null ? 0 : ((Number) value).intValue());
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "long": {
                    WritableLong p = new WritableLong(name, false, value == null ? 0L : ((Number) value).longValue());
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "float": {
                    WritableFloat p = new WritableFloat(name, false, value == null ? 0f : ((Number) value).floatValue());
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "double": {
                    WritableDouble p = new WritableDouble(name, false, value == null ? 0.0 : ((Number) value).doubleValue());
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "boolean": {
                    WritableBoolean p = new WritableBooleanImpl(name, false, value != null && ((Boolean) value));
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "java.lang.Double": {
                    WritableDouble p = new WritableDouble(name, true, (Double) value);
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "java.lang.Integer": {
                    WritableInt p = new WritableIntImpl(name, true, (Integer) value);
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "java.lang.Float": {
                    WritableFloat p = new WritableFloat(name, true, (Float) value);
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "java.lang.Long": {
                    WritableLong p = new WritableLong(name, true, (Long) value);
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "java.lang.Boolean": {
                    WritableBoolean p = new WritableBooleanImpl(name, true, ((Boolean) value));
                    prepare(p);
                    return (WritableValue<T>) p;
                }
                case "java.lang.String": {
                    WritableString p = new WritableStringImpl(name, ((String) value));
                    prepare(p);
                    return (WritableValue<T>) p;
                }
            }
            WritableValueImpl<T> p = new WritableValueImpl<T>(name, type, value);
            prepare(p);
            return p;
        }

        public <T> WritableValue<T> valueOf(Class<T> type, T value) {
            return valueOf(PropertyType.of(type), value);
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

        public <T> WritableList<T> boundListOf(PropertyType elementType, int maxEntries) {
            WritableList<T> list = listOf(elementType);
            configureMaxEntries(list, maxEntries);
            return list;
        }

        public <T> WritableList<T> boundListOf(PropertyType elementType, ObservableValue<Integer> maxEntries) {
            WritableList<T> list = listOf(elementType);
            configureObservableMaxEntries(list, maxEntries);
            return list;
        }

        public <T> WritableList<T> setOf(PropertyType elementType) {
            WritableList<T> list = listOf(elementType);
            configureSet(list);
            return list;
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
            createdList.onChange(new PropertyListener() {
                @Override
                public void propertyUpdated(PropertyEvent event) {
                    if (event.eventPath().equals("/")) {
                        K index = event.oldValue();
                        V oldValue = event.oldValue();
                        if (oldValue != null) {
                            ObservableValue<K> n = nameSupplier.apply(oldValue);
                            n.events().removeIf(x -> x instanceof NameUpdateLmap2Adjuster && ((NameUpdateLmap2Adjuster) x).p == createdList);
                        }
                        V newValue = event.newValue();
                        if (newValue != null) {
                            ObservableValue<K> n = nameSupplier.apply(newValue);
                            n.onChange(new NameUpdateLmap2Adjuster<>(createdList));
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

        public void prepare(WritableProperty p) {
            for (PropertyAdjuster adjuster : adjusters) {
                p.adjusters().add(adjuster);
            }
            //((PropertyAdjustersImpl) p.adjusters()).readOnly(true);
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
                K k1 = event.oldValue();
                V removed = p.remove(k1);
                if (removed != null) {
                    p.add(removed);
                }
            }
        }
    }
}

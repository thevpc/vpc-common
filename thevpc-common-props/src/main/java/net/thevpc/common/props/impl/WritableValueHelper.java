package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

public class WritableValueHelper {
    public static Property getChildProperty(Property parent, String name) {
        if ((parent instanceof ChildPropertyResolver)) {
            try {
                Property b = ((ChildPropertyResolver) parent).resolveChildProperty(name);
                if (b!=null) {
                    return b;
                }
            } catch (Exception ex) {
                // act if it is not parent list
            }
        }else if ((parent instanceof ObservableList)) {
            ObservableList<?> parent1 = (ObservableList<?>) parent;
            try {
                int i = Integer.parseInt(name);
                Object b = parent1.get(i);
                if (b instanceof Property) {
                    return (Property) b;
                }
            } catch (Exception ex) {
                // act if it is not parent list
            }
            for (Object o : parent1) {
                if(o instanceof Property){
                    if(((Property) o).propertyName().equals(name)){
                        return (Property) o;
                    }
                }
            }
        } else if ((parent instanceof ObservableMap)) {
            try {
                for (MapEntry<?, ?> mapEntry : ((ObservableMap<?, ?>) parent).entrySet()) {
                    String k = String.valueOf(mapEntry.getKey());
                    if (k.equals(name)) {
                        Object v = mapEntry.getValue();
                        if (v instanceof Property) {
                            return (Property) v;
                        }
                    }
                }
                Integer i = PropsUtils.parseInt(name);
                if(i!=null) {
                    Object b = ((ObservableList<?>) parent).get(i);
                    if (b instanceof Property) {
                        return (Property) b;
                    }
                }
                for (MapEntry<?, ?> mapEntry : ((ObservableMap<?, ?>) parent).entrySet()) {
                    String k = String.valueOf(mapEntry.getKey());
                    Object v = mapEntry.getValue();
                    if (v instanceof Property) {
                        Property vv = (Property) v;
                        if(vv.propertyName().equals(name)) {
                            return vv;
                        }
                    }
                }
            } catch (Exception ex) {
                // act if it is not parent list
            }
        } else if ((parent instanceof ObservableValue)) {
            Object x = ((ObservableValue) parent).get();
            if (x == null) {
                //
                return null;
            }
            if (x instanceof Property) {
                parent = (Property) x;
            } else {
                throw new IllegalArgumentException("not parent property");
            }
        } else {
            //
        }
        boolean processed = false;
        Class clazz = parent.getClass();
        while (clazz != null && !clazz.equals(Object.class)) {
            Method mm = null;
            try {
                mm = clazz.getDeclaredMethod(name);
            } catch (Exception e) {
                //throw new IllegalArgumentException(e);
            }
            if (mm != null && mm.getParameterCount() == 0) {
                if (Property.class.isAssignableFrom(mm.getReturnType())) {
                    mm.setAccessible(true);
                    try {
                        parent = (Property) mm.invoke(parent);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                    processed = true;
                    break;
                }
            }
            if (!processed) {
                Field ff=null;
                try {
                    ff = clazz.getDeclaredField(name);
                } catch (Exception e) {
                    //throw new IllegalArgumentException(e);
                }
                if (ff!=null && Property.class.isAssignableFrom(ff.getType())) {
                    try {
                        ff.setAccessible(true);
                        parent = (Property) ff.get(parent);
                        processed = true;
                        break;
                    } catch (Exception e) {
                        //throw new IllegalArgumentException(e);
                    }

                }
            }
            clazz=clazz.getSuperclass();
        }

        if (!processed) {
            throw new IllegalArgumentException("missing property " + name + " for " + parent.getClass().getName());
        }
        return parent;
    }

    public static Property getChildProperty(Property parent, Path relativePath) {
        Path split = relativePath;
        Property a = parent;
        while (!split.isEmpty()) {
            String nn = split.first();
            split = split.skipFirst();
            a = getChildProperty(a, nn);
        }
        return a;
    }

    public static <T> void helperBind(WritableValue<T> first, Property root, Path rootPath, Path relativePath) {
        Property cp = root.getChildProperty(relativePath);
        if (cp != null) {
            first.set(((ObservableValue<T>) cp).get());
        }
        Path fullPath = rootPath.append(relativePath);
        root.events().addPropagated(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.eventType() == PropertyUpdate.UPDATE) {
                    if (event.eventPath().equals(fullPath)) {
                        first.set(event.newValue());
                    }
                }
            }
        });
        first.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                Object nv = event.newValue();
                Property a = root.getChildProperty(relativePath);
                if (a instanceof WritableValue) {
                    ((WritableValue) a).set(nv);
                } else if (a != null) {
                    throw new IllegalArgumentException("property " + fullPath + " is not writable");
                }
            }
        });
    }

    public static <T> void helperBindTarget(ObservableValue<T> me, SetValueModel<T> other) {
        me.unbind(other);
        me.onChange(new BindPropertyListener(other));
        other.set(me.get());
    }

    public static <T, T2> void setAndBindConvert(WritableValue<T> base, WritableValue<T2> other, Function<T, T2> map, Function<T2, T> mapBack) {
        T2 t2 = other.get();
        base.set(mapBack.apply(t2));
        base.bindConvert(other, map);
    }

    public static <T> void helperBindSource(WritableValue<T> me, ObservableValue<T> source) {
        source.unbind(me);
        source.onChange(new BindPropertyListener(me));
        me.set(source.get());
    }

    public static <T, T2> void helperBindConvert(ObservableValue<T> me, WritableValue<T2> other, Function<T, T2> map) {
        me.unbind(other);
        me.onChange(new BindPropertyConvertListener<>(other, map, null));
        T t = me.get();
        other.set(map.apply(t));
    }

    public static <T2> void helperRemoveBindListeners(PropertyListeners listeners, SetValueModel<T2> other) {
        listeners.removeIf(x -> {
            if (x instanceof BindPropertyListener && ((BindPropertyListener) x).isTarget(other)) {
                return true;
            }
            if (x instanceof BindPropertyConvertListener && ((BindPropertyConvertListener) x).isTarget(other)) {
                return true;
            }
            return false;
        });
    }

    static class BindPropertyListener<T> implements PropertyListener {

        private SetValueModel<T> target;

        public BindPropertyListener(SetValueModel<T> source) {
            this.target = source;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            target.set(event.newValue());
        }

        public boolean isTarget(SetValueModel<T> source) {
            return source == this.target;
        }
    }

    static class BindPropertyConvertListener<T, T2> implements PropertyListener {

        private SetValueModel<T2> target;
        private Function<T, T2> map;
        private Function<T2, T> mapBack;

        public BindPropertyConvertListener(WritableValue<T2> target, Function<T, T2> map, Function<T2, T> mapBack) {
            this.target = target;
            this.map = map;
            this.mapBack = mapBack;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            T t = event.newValue();
            target.set(map.apply(t));
        }

        public boolean isTarget(SetValueModel<T> source) {
            return source == this.target;
        }
    }

}

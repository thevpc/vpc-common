package net.thevpc.common.prs.factory;

import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)  alias vpc
 * %creationtime 2009/08/15 17:43:18
 */
public final class ExtensionDescriptor<T> implements Cloneable {

    FactoryImpl factory;
    OwnedClass id;
    String group;
    boolean customizable = true;
    ImplementationDescriptor<T> startupImpl;
    ImplementationDescriptor<T> defaultImpl;
    ImplementationDescriptor<T> impl;
    Vector<ImplementationDescriptor<T>> implementations = new Vector<ImplementationDescriptor<T>>();
    Vector<ImplementationFactoryDescriptor<T>> subFactories = new Vector<ImplementationFactoryDescriptor<T>>();
    Hashtable<Class, OwnedClass> alternatives = new Hashtable<Class, OwnedClass>();

    private class OwnedClass implements Comparable<OwnedClass> {

        private Class<? extends T> clazz;
        private Object owner;

        private OwnedClass(Class<? extends T> clazz, Object owner) {
            this.clazz = clazz;
            this.owner = owner;
        }

        public int compareTo(OwnedClass o) {
            return clazz.getName().compareTo(o.clazz.getName());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            OwnedClass that = (OwnedClass) o;

            if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) {
                return false;
            }
            if (owner != null ? !owner.equals(that.owner) : that.owner != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = clazz != null ? clazz.hashCode() : 0;
            result = 31 * result + (owner != null ? owner.hashCode() : 0);
            return result;
        }
    }

    public ExtensionDescriptor(Class<? extends T> cls, Object owner, String group, ImplementationDescriptor<T> defaultImpl) {
        this.id = new OwnedClass(cls, owner);
        this.group = group;
        this.defaultImpl = defaultImpl;
        this.impl = defaultImpl;
        if (defaultImpl != null) {
            defaultImpl.extension = this;
            implementations.add(defaultImpl);
        }
    }

    public void addAlternative(Class alternative, Object owner) {
        if (!alternatives.containsKey(alternative)) {
            alternatives.put(alternative, new OwnedClass(alternative, owner));
            if (factory != null) {
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {
                        e = new FactoryEvent(factory, this, null, null, null, Factory.IMPLEMENTATION_ADDED, null, alternative, owner);
                    }
                    listener.alternativeAdded(e);
                }
            }
        }
    }

    public void removeAlternative(Class alternative) {
        ExtensionDescriptor<T>.OwnedClass ownedClass = alternatives.remove(alternative);
        if (ownedClass != null) {
            if (factory != null) {
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {
                        e = new FactoryEvent(factory, this, null, null, null, Factory.IMPLEMENTATION_ADDED, null, alternative, ownedClass.owner);
                    }
                    listener.alternativeRemoved(e);
                }
            }
        }
    }

    public void remove(ImplementationDescriptor<T> impl) {
        if (impl != null && implementations.contains(impl)) {
            implementations.remove(impl);
            impl.extension = null;
            if (factory != null) {
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {
                        e = new FactoryEvent(factory, this, null, impl, null, Factory.IMPLEMENTATION_REMOVED, null, impl, impl.getOwner());
                    }
                    listener.implementationRemoved(e);
                }
            }
            if (defaultImpl != null && defaultImpl.equals(impl)) {
                ImplementationDescriptor<T> oldDefaultImpl = defaultImpl;
                defaultImpl = null;
                if (factory != null) {
                    FactoryEvent e = null;
                    for (FactoryListener listener : factory.getListeners()) {
                        if (e == null) {
                            e = new FactoryEvent(factory, this, null, impl, null, Factory.DEFAULT_IMPLEMENTATION_CHANGED, oldDefaultImpl, impl, impl.getOwner());
                        }
                        listener.implementationDefaultChanged(e);
                    }
                }
            }
            if (startupImpl != null && startupImpl.equals(impl)) {
//                ImplementationDescriptor<T> oldDefaultImpl = startupImpl;
                startupImpl = null;
//                if (factory != null) {
//                    FactoryEvent e = null;
//                    for (FactoryListener listener : factory.getListeners()) {
//                        if (e == null) {
//                            e = new FactoryEvent(factory, this, null, impl, null, Factory.DEFAULT_IMPLEMENTATION_CHANGED, oldDefaultImpl, impl, impl.getOwner());
//                        }
//                        listener.implementationDefaultChanged(e);
//                    }
//                }
            }
        }
    }

    public void add(ImplementationDescriptor<T> impl) {
        if (impl != null && !implementations.contains(impl)) {
            implementations.add(impl);
            impl.extension = this;
            if (factory != null) {
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {

                        e = new FactoryEvent(factory, this, null, impl, null, Factory.IMPLEMENTATION_ADDED, null, impl, impl.getOwner());
                    }
                    listener.implementationAdded(e);
                }
            }
        }
    }

    public void add(ImplementationFactoryDescriptor<T> impl) {
        if (impl != null && !subFactories.contains(impl)) {
            subFactories.add(impl);
            impl.extension = this;
            if (factory != null) {
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {

                        e = new FactoryEvent(factory, this, impl, null, null, Factory.EXTENSION_FACTORY_ADDED, null, impl, impl.getOwner());
                    }
                    listener.extensionFactoryAdded(e);
                }
            }
        }
    }

    public void remove(ImplementationFactoryDescriptor<T> impl) {
        if (impl != null && subFactories.contains(impl)) {
            subFactories.remove(impl);
            impl.extension = null;
            if (factory != null) {
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {

                        e = new FactoryEvent(factory, this, impl, null, null, Factory.EXTENSION_FACTORY_REMOVED, null, impl, impl.getOwner());
                    }
                    listener.extensionFactoryRemoved(e);
                }
            }
        }
    }

    public ImplementationDescriptor getImpl() {
        return impl;
    }

    public ImplementationDescriptor<T> getValidImpl() {
        ImplementationDescriptor<T> r = impl;
        if (r != null) {
            return r;
        }
        r = getStartupImpl();
        if (r != null) {
            return r;
        }
        r= getDefaultImpl();
        if (r != null) {
            return r;
        }
        if(implementations.size()>0){
            return implementations.get(0);
        }
        return null;
    }

    public ImplementationDescriptor<T> getDefaultImpl() {
        return defaultImpl;
    }

    public ImplementationDescriptor<T> getStartupImpl() {
        return startupImpl;
    }

    public void setStartupImpl(ImplementationDescriptor<T> startupImpl) {
        this.startupImpl = startupImpl;
    }

    public void setDefaultImpl(ImplementationDescriptor<T> defaultImpl) {
        ImplementationDescriptor old = this.defaultImpl;
        this.defaultImpl = defaultImpl;
        if (factory != null) {
            if (old != impl && (old == null || !old.equals(defaultImpl))) {
                add(this.defaultImpl);
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {
                        e = new FactoryEvent(factory, this, null, defaultImpl, null, Factory.DEFAULT_IMPLEMENTATION_CHANGED, old, defaultImpl, defaultImpl.getOwner());
                    }
                    listener.implementationDefaultChanged(e);
                }
            }
        }
    }

    public void setImpl(String impl) {
        if (impl != null) {
            for (ImplementationDescriptor<T> ci : implementations) {
                if (ci.getImplementationType().getName().equals(impl)) {
                    setImpl(ci);
                    return;
                }
            }
        } else {
            setImpl((ImplementationDescriptor<T>) null);
        }
    }

    public void setImpl(Class impl) {
        if (impl == null) {
            setImpl((ImplementationDescriptor<T>) null);
        }
        for (ImplementationDescriptor<T> ci : implementations) {
            if (ci.getImplementationType() == impl || (impl != null && impl.equals(ci.getImplementationType()))) {
                setImpl(ci);
                return;
            }
        }
        throw new NoSuchElementException(impl == null ? null : impl.getName());
    }

    public void setImpl(ImplementationDescriptor<T> impl) {
        ImplementationDescriptor old = this.impl;
        if (old != impl && (old == null || !old.equals(impl))) {
            if (impl == null) {
                this.impl = null;
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {
                        e = new FactoryEvent(factory, this, null, impl, null, Factory.IMPLEMENTATION_CHANGED, old, null, null);
                    }
                    listener.implementationSelectionChanged(e);
                }
            } else {
                add(impl);
                impl = implementations.get(implementations.indexOf(impl));//
                this.impl = impl;
                FactoryEvent e = null;
                for (FactoryListener listener : factory.getListeners()) {
                    if (e == null) {
                        e = new FactoryEvent(factory, this, null, impl, null, Factory.IMPLEMENTATION_CHANGED, old, impl, impl.getOwner());
                    }
                    listener.implementationSelectionChanged(e);
                }
            }
        }
    }

    public Class getId() {
        return id.clazz;
    }

    public Object getOwner() {
        return id.owner;
    }

    public String getGroup() {
        return group;
    }

    public FactoryImpl getFactory() {
        return factory;
    }

    public ImplementationDescriptor<T>[] getImplementations() {
        return implementations.toArray(new ImplementationDescriptor[implementations.size()]);
    }

    @Override
    public ExtensionDescriptor clone() {
        HashMap<Class, ImplementationDescriptor<T>> oldOnes = new HashMap<Class, ImplementationDescriptor<T>>();
        if (defaultImpl != null && !oldOnes.containsKey(defaultImpl.implementationType)) {
            oldOnes.put(defaultImpl.implementationType, defaultImpl);
        }
        if (impl != null && !oldOnes.containsKey(impl.implementationType)) {
            oldOnes.put(impl.implementationType, impl);
        }
        for (ImplementationDescriptor<T> ii : implementations) {
            if (ii != null && !oldOnes.containsKey(ii.implementationType)) {
                oldOnes.put(ii.implementationType, ii);
            }
        }
        HashMap<Class, ImplementationDescriptor<T>> newOnes = new HashMap<Class, ImplementationDescriptor<T>>();
        ExtensionDescriptor<T> newConf = new ExtensionDescriptor<T>(id.clazz, id.owner, group, findClone(defaultImpl, oldOnes, newOnes));
        newConf.impl = findClone(impl, oldOnes, newOnes);
        newConf.startupImpl = findClone(startupImpl, oldOnes, newOnes);
        newConf.implementations.clear();
        for (ImplementationDescriptor<T> ii : implementations) {
            if (ii != null) {
                newConf.add(findClone(ii, oldOnes, newOnes));
            }
        }
        newConf.alternatives.clear();
        newConf.alternatives.putAll(this.alternatives);
        newConf.subFactories = (Vector<ImplementationFactoryDescriptor<T>>) subFactories.clone();
        return newConf;
    }

    private ImplementationDescriptor<T> findClone(ImplementationDescriptor<T> i, HashMap<Class, ImplementationDescriptor<T>> oldOnes, HashMap<Class, ImplementationDescriptor<T>> newOnes) {
        if (i == null) {
            return i;
        }
        ImplementationDescriptor<T> someImpl = newOnes.get(i.implementationType);
        if (someImpl == null) {
            someImpl = i.clone();
            newOnes.put(someImpl.implementationType, someImpl);
        }
        return someImpl;
    }

    @Override
    public String toString() {
        return getId().getName() + "=" + getImpl() + " % " + getImplementations().length;
    }

    public boolean isCustomizable() {
        return customizable;
    }

    public void setCustomizable(boolean customizable) {
        this.customizable = customizable;
    }

    public ImplementationFactoryDescriptor<T>[] getImplementationFactories() {
        return (ImplementationFactoryDescriptor<T>[]) subFactories.toArray(new ImplementationFactoryDescriptor[subFactories.size()]);
    }
}

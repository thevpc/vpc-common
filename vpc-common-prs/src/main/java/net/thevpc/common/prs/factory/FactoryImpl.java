/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.prs.factory;

import net.thevpc.common.prs.plugin.ExtensionFactory;
import net.thevpc.common.prs.plugin.PluginDescriptor;
import net.thevpc.common.prs.plugin.ExtensionFactoryType;

import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 3 dec. 2006 19:01:09
 */
public class FactoryImpl implements Factory {

    private Map<Class, ExtensionDescriptor> confs = new HashMap<Class, ExtensionDescriptor>();
    private Factory parent;
    private FactoryOwnerFilter ownerFilter;
    List<FactoryListener> listeners;
    List<FactoryVetoListener> vetoListeners;

    public FactoryImpl(Factory parent) {
        this.parent = parent;
    }

    public synchronized void addFactoryListener(FactoryListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<FactoryListener>();
        }
        listeners.add(listener);
    }

    public synchronized void removeFactoryListener(FactoryListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public synchronized void addFactoryVetoListener(FactoryVetoListener listener) {
        if (vetoListeners == null) {
            vetoListeners = new Vector<FactoryVetoListener>();
        }
        vetoListeners.add(listener);
    }

    public synchronized void removeFactoryVetoListener(FactoryVetoListener listener) {
        if (vetoListeners != null) {
            vetoListeners.remove(listener);
        }
    }

    public <T> ExtensionDescriptor<T> getExtension(Class<? extends T> id) {
        ExtensionDescriptor<T> c = id == null ? null : confs.get(id);
        if (c == null) {
            throw new NoSuchElementException(id == null ? "NULL" : id.getName());
        }
        return c;
    }

    public boolean containsConfiguration(Class id) {
        return confs.containsKey(id);
    }

    public <T> void registerExtension(ExtensionDescriptor<T> configuration) {
        configuration.factory = this;
        confs.put(configuration.getId(), configuration);
        for (Class alternative : configuration.alternatives.keySet()) {
            confs.put(alternative, configuration);
        }
    }

    public <T> void registerImplementation(Class<? extends T> interfaceClass, Class<? extends T> implementationClass, Object owner, boolean setAsCurrentImpl) {
        registerImplementation(interfaceClass, new ImplementationDescriptor<T>(implementationClass, owner), setAsCurrentImpl);
    }

    public <T> void registerExtensionFactory(Class<? extends ExtensionFactory<T>> extensionFactoryClass, Object owner) {
        ExtensionFactoryType annotation = extensionFactoryClass.getAnnotation(ExtensionFactoryType.class);
        if (annotation == null) {
            throw new IllegalArgumentException(extensionFactoryClass.getName() + " must provide @ExtensionFactoryType annotation");
        }
        if (annotation.type().isEmpty() || annotation.type().equals(Object.class.getName())) {
            throw new IllegalArgumentException(extensionFactoryClass.getName() + " must @ExtensionFactoryType annotation with a valid type");
        }
        Class<T> factClazz;
        try {
            factClazz = (Class<T>) extensionFactoryClass.getClassLoader().loadClass(annotation.type());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        getExtension(factClazz).add(new ImplementationFactoryDescriptor<T>(extensionFactoryClass, owner));
    }

    @Override
    public <T> void registerImplementation(Class<? extends T> interfaceClass, ImplementationDescriptor<T> implementationClass, boolean setAsCurrentImpl) {
        if (setAsCurrentImpl) {
            //will be added
            getExtension(interfaceClass).setImpl((ImplementationDescriptor) implementationClass);
        } else {
            getExtension(interfaceClass).add((ImplementationDescriptor) implementationClass);
        }
    }

    @Override
    public <T> ImplementationDescriptor<T> getImplementation(Class<T> interfaceClass) throws NoSuchElementException {
        ExtensionDescriptor ccfg = getExtension(interfaceClass);
        ImplementationDescriptor clz = ccfg.getValidImpl();
        if (clz == null || clz.implementationType == null) {
            if (parent != null) {
                return parent.getImplementation(interfaceClass);
            } else {
                clz = ccfg.getDefaultImpl();
                if (clz != null && clz.implementationType == null) {
                    return clz;
                }
            }
        } else {
            return clz;
        }
        throw new NoSuchElementException("implementation for " + interfaceClass.getName());
    }

    @Override
    public <T> T newInstance(Class<T> interfaceClass) {
        ImplementationDescriptor<T> implementation = getImplementation(interfaceClass);
        return (T) instantiate(implementation.implementationType, implementation.owner);
    }

    @Override
    public <T> T newInstance(Class<T> interfaceClass, Class<T> defaultImpl, Object owner) {
        ExtensionDescriptor<T> cfg = interfaceClass == null ? null : confs.get(interfaceClass);
        if (cfg != null) {
            ImplementationDescriptor<T> clz = cfg.getValidImpl();
            if (clz == null || clz.implementationType == null) {
                if (parent != null) {
                    ImplementationDescriptor<T> clz2 = parent.getImplementation(interfaceClass);
                    return (T) instantiate(clz2.implementationType, clz2.owner);
                } else {
                    clz = cfg.getDefaultImpl();
                    if (clz != null && clz.implementationType == null) {
                        return (T) instantiate(clz.implementationType, clz.owner);
                    }
                }
            } else {
                return (T) instantiate(clz.implementationType, clz.owner);
            }
        }
        return instantiate(defaultImpl, owner);
    }

    public <T> T instantiate(Class<T> impl, Object owner) {
        T instance = null;
        try {
            instance = impl.newInstance();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        fireObjectCreated(instance, owner);
        return instance;
    }

    protected synchronized void fireObjectCreated(Object instance, Object owner) throws FactoryVetoException {
        if (vetoListeners != null) {
            FactoryEvent e = null;
            for (FactoryVetoListener listener : vetoListeners) {
                if (e == null) {
                    e = new FactoryEvent(this, null, null, null, instance, OBJECT_CREATED, null, instance, owner);
                }
                listener.instanceCreated(e);
            }
        }
        if (listeners != null) {
            FactoryEvent e = null;
            for (FactoryListener listener : listeners) {
                if (e == null) {
                    e = new FactoryEvent(this, null, null, null, instance, OBJECT_CREATED, null, instance, owner);
                }
                listener.instanceCreated(e);
            }
        }
    }

    public ExtensionDescriptor[] getExtensions() {
        List<ExtensionDescriptor> all = new ArrayList<ExtensionDescriptor>(confs.size());
        Set<Class> visited = new HashSet<Class>();
        //remove duplicates due to alternatives
        for (ExtensionDescriptor configuration : confs.values()) {
            if (!visited.contains(configuration.getId())) {
                visited.add(configuration.getId());
                all.add(configuration);
            }
        }
        return all.toArray(new ExtensionDescriptor[all.size()]);
    }

    public Collection<FactoryListener> getListeners() {
        return listeners == null ? Collections.EMPTY_LIST : Collections.unmodifiableCollection(listeners);
    }

    public void registerExtensions(ExtensionDescriptor[] extensions) {
        for (ExtensionDescriptor extension : extensions) {
            ImplementationDescriptor[] implementations = extension.getImplementations();
            if (implementations.length > 0) {
                extension.setDefaultImpl(implementations[0]);
                extension.setStartupImpl(implementations[0]);
            }
            if (!this.containsConfiguration(extension.getId())) {
                this.registerExtension(extension.clone()); // clone is mandatory
            }
        }
    }

    public <T> List<T> createImplementations(Class<? extends T> cls) {
        ExtensionDescriptor<T> configuration = null;
        try {
            configuration = getExtension(cls);
        } catch (NoSuchElementException e) {
            return Collections.EMPTY_LIST;
        }
        ArrayList<T> tt = new ArrayList<T>();
        for (ImplementationDescriptor<T> ii : configuration.getImplementations()) {
            if (ownerFilter == null || ownerFilter.acceptOwner(ii.getOwner())) {
                try {
                    tt.add((T) instantiate(ii.getImplementationType(), ii.getOwner()));
                } catch (FactoryVetoException e) {
                    //ignore
                } catch (Exception e) {
                    PluginDescriptor pluginInfo = (PluginDescriptor) ii.getOwner();
                    pluginInfo.getLog().error("Unable to create " + cls.getSimpleName() + " (" + ii.getImplementationType().getName() + ")", e);
                }
            }
        }
        for (ImplementationFactoryDescriptor<T> ii : configuration.getImplementationFactories()) {
            if (ownerFilter == null || ownerFilter.acceptOwner(ii.getOwner())) {
                try {
                    ExtensionFactory fact = instantiate(ii.getImplementationFactoryType(), ii.getOwner());
                    for (Object o : fact.createExtensions()) {
                        try {
                            T oo = (T) o;
                            fireObjectCreated(oo, ii.getOwner());
                            tt.add(oo);
                        } catch (FactoryVetoException e) {
                            //ignore
                        } catch (Exception e) {
                            PluginDescriptor pluginInfo = (PluginDescriptor) ii.getOwner();
                            pluginInfo.getLog().error("Unable to initialize " + cls.getSimpleName() + " (" + o + ")", e);
                        }
                    }

                } catch (Exception e) {
                    PluginDescriptor pluginInfo = (PluginDescriptor) ii.getOwner();
                    pluginInfo.getLog().error("Unable to create " + cls.getSimpleName() + " (" + ii.getImplementationFactoryType() + ")", e);
                }
            }
        }
        return tt;
    }

    public FactoryOwnerFilter getOwnerFilter() {
        return ownerFilter;
    }

    public void setOwnerFilter(FactoryOwnerFilter ownerFilter) {
        this.ownerFilter = ownerFilter;
    }
}

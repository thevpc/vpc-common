package net.vpc.common.prs.factory;

import net.vpc.common.prs.plugin.ExtensionFactory;

import java.util.NoSuchElementException;
import java.util.Collection;

public interface Factory {
    String CONFIGURATION_CHANGED = "CONFIGURATION_CHANGED";
    String DEFAULT_IMPLEMENTATION_CHANGED = "DEFAULT_IMPLEMENTATION_CHANGED";
    String IMPLEMENTATION_CHANGED = "IMPLEMENTATION_CHANGED";
    String IMPLEMENTATION_ADDED = "IMPLEMENTATION_ADDED";
    String IMPLEMENTATION_REMOVED = "IMPLEMENTATION_REMOVED";
    String EXTENSION_FACTORY_ADDED = "EXTENSION_FACTORY_ADDED";
    String EXTENSION_FACTORY_REMOVED = "EXTENSION_FACTORY_REMOVED";
    String OBJECT_CREATED = "OBJECT_CREATED";

    void addFactoryListener(FactoryListener listener);

    void removeFactoryListener(FactoryListener listener);

    public void addFactoryVetoListener(FactoryVetoListener listener) ;

    public void removeFactoryVetoListener(FactoryVetoListener listener) ;

    boolean containsConfiguration(Class id);

    void registerExtensions(ExtensionDescriptor[] extensions);
    <T> void registerExtension(ExtensionDescriptor<T> extension);

    <T> void registerExtensionFactory(Class<? extends ExtensionFactory<T>> extensionFactoryClass, Object owner);

    <T> void registerImplementation(Class<? extends T> interfaceClass, Class<? extends T> implementationClass, Object owner,boolean setAsCurrentImpl);

    <T> void registerImplementation(Class<? extends T> interfaceClass, ImplementationDescriptor<T> implementationClass, boolean setAsCurrentImpl);

    public <T> ImplementationDescriptor<T> getImplementation(Class<T> interfaceClass) throws NoSuchElementException;

    public <T> T newInstance(Class<T> interfaceClass);

    public <T> T newInstance(Class<T> interfaceClass, Class<T> defaultImpl, Object owner);

    public <T> T instantiate(Class<T> impl,Object owner);

    public ExtensionDescriptor[] getExtensions();

    public Collection<FactoryListener> getListeners();

    public void setOwnerFilter(FactoryOwnerFilter ownerFilter);
}

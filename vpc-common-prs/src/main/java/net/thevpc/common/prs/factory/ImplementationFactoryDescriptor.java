package net.thevpc.common.prs.factory;

import net.thevpc.common.prs.plugin.ExtensionFactory;

public class ImplementationFactoryDescriptor<T>{
    ExtensionDescriptor extension;
    private Class<? extends ExtensionFactory> implementationFactoryType;
    private Object owner;

    public ImplementationFactoryDescriptor(Class<? extends ExtensionFactory> implementationFactoryType, Object owner) {
        this.implementationFactoryType = implementationFactoryType;
        this.owner = owner;
    }

    public ExtensionDescriptor getExtension() {
        return extension;
    }

    public Class<? extends ExtensionFactory> getImplementationFactoryType() {
        return implementationFactoryType;
    }

    public Object getOwner() {
        return owner;
    }
}

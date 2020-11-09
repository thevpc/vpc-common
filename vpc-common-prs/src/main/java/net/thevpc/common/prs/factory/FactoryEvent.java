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

import java.util.EventObject;

public class FactoryEvent extends EventObject {
    private ExtensionDescriptor configuration;
    private ImplementationDescriptor implementation;
    private ImplementationFactoryDescriptor extensionFactoryDescriptor;
    private String property;
    private Object oldValue;
    private Object newValue;
    private Object instance;
    private Object owner;

    public FactoryEvent(Factory source, ExtensionDescriptor configuration, ImplementationFactoryDescriptor extensionFactoryDescriptor, ImplementationDescriptor implementation, Object instance, String property, Object oldValue, Object newValue, Object owner) {
        super(source);
        this.configuration = configuration;
        this.implementation = implementation;
        this.extensionFactoryDescriptor = extensionFactoryDescriptor;
        this.instance = instance;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.owner = owner;
    }

    public ExtensionDescriptor getConfiguration() {
        return configuration;
    }

    public ImplementationDescriptor getImplementation() {
        return implementation;
    }

    public String getProperty() {
        return property;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getInstance() {
        return instance;
    }

    public Object getOwner() {
        return owner;
    }
}

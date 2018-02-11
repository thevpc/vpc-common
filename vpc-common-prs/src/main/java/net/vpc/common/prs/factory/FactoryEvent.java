/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.prs.factory;

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

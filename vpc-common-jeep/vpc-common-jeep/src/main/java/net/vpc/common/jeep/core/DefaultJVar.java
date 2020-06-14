/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.impl.vars.AbstractJVar;
import net.vpc.common.jeep.JContext;

/**
 *
 * @author vpc
 */
public class DefaultJVar extends AbstractJVar {
    
    private String name;
    private JType type;
    private JType undefinedType;
    private Object value;
    private boolean defined = false;
    private boolean readOnly = false;

    public DefaultJVar(String name, Object value, JContext context) {
        this(name,
                context.types().typeOf(value),
                context.types().typeOf(value),
                value);
    }
    
    public DefaultJVar(String name, JType type, JType undefinedType, Object value) {
        this.name = name;
        this.type = type;
        this.undefinedType = undefinedType;
        this.value = value;
    }

    public JType undefinedType() {
        return undefinedType;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    

    public String name() {
        return name;
    }

    public JType type() {
        return type;
    }

    public Object getValue(JContext context) {
        return value;
    }

    public DefaultJVar setValue(Object value, JContext context) {
        this.value = value;
        defined = true;
        return this;
    }

    public boolean isDefinedValue() {
        return defined;
    }

    public boolean isUndefinedValue() {
        return !defined;
    }

    public DefaultJVar setUndefinedValue() {
        this.value = null;
        defined = false;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultVarDefinition{" + "name=" + name + ", type=" + type + ", undefinedType=" + undefinedType + ", value=" + value + ", defined=" + defined + ", readOnly=" + readOnly + '}';
    }
    
    
}

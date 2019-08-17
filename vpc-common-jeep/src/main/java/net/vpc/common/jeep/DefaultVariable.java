/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 *
 * @author vpc
 */
public class DefaultVariable extends AbstractVariable{
    
    private String name;
    private Class type;
    private Class undefinedType;
    private Object value;
    private boolean defined = false;
    private boolean readOnly = false;

    public DefaultVariable(String name, Object value) {
        this(name,value.getClass(),value.getClass(),value);
    }
    
    public DefaultVariable(String name, Class type,Class undefinedType,Object value) {
        this.name = name;
        this.type = type;
        this.undefinedType = undefinedType;
        this.value = value;
    }

    public Class getUndefinedType() {
        return undefinedType;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public Object getValue(ExpressionEvaluator evaluator) {
        return value;
    }

    public DefaultVariable setValue(Object value, ExpressionEvaluator evaluator) {
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

    public DefaultVariable setUndefinedValue() {
        this.value = null;
        defined = false;
        return this;
    }

    @Override
    public String toString() {
        return "DefaultVarDefinition{" + "name=" + name + ", type=" + type + ", undefinedType=" + undefinedType + ", value=" + value + ", defined=" + defined + ", readOnly=" + readOnly + '}';
    }
    
    
}

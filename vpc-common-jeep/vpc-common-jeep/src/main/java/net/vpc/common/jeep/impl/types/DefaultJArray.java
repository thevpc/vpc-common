package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.JArray;
import net.vpc.common.jeep.JEvalException;
import net.vpc.common.jeep.JType;

import java.lang.reflect.Array;

public class DefaultJArray implements JArray {
    private Object[] array;
    private JType componentType;

    public DefaultJArray(Object[] array, JType componentType) {
        this.array = array;
        this.componentType = componentType;
    }

    @Override
    public JType type() {
        return componentType.toArray();
    }

    @Override
    public JType componentType() {
        return componentType;
    }

    @Override
    public int length() {
        return array.length;
    }

    @Override
    public Object get(int index) {
        return array[index];
    }

    @Override
    public void set(int index, Object value) {
        array[index]=value;
    }

    @Override
    public Object value() {
        return this;
    }

    @Override
    public Object get(String name) {
        switch (name){
            case "length":return length();
        }
        throw new JEvalException("Field not found "+type()+"."+name);
    }

    @Override
    public void set(String name, Object o) {
        throw new JEvalException("Modifiable Field not found "+type()+"."+name);
    }
}

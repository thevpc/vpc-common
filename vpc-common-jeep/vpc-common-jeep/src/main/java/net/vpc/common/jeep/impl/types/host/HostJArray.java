package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.JArray;
import net.vpc.common.jeep.JEvalException;
import net.vpc.common.jeep.JType;

import java.lang.reflect.Array;

public class HostJArray implements JArray {
    private Object array;
    private JType componentType;

    public HostJArray(Object array,JType componentType) {
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
        return Array.getLength(array);
    }

    @Override
    public Object get(int index) {
        return Array.get(array,index);
    }

    @Override
    public void set(int index, Object value) {
        Array.set(array,index,value);
    }

    @Override
    public Object value() {
        return array;
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
package net.vpc.common.jeep.core.types;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.core.JStaticObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultJStaticObject implements JStaticObject {
    private Map<String,Object> values=new LinkedHashMap<>();
    private JType type;

    public DefaultJStaticObject(JType type) {
        this.type = type;
    }

    @Override
    public JType type() {
        return type;
    }

    @Override
    public Object get(String name) {
        return values.get(name);
    }

    @Override
    public void set(String name, Object o) {
        values.put(name,o);
    }
}

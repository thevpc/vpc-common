package net.vpc.common.jeep.core.types;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JRawField;
import net.vpc.common.jeep.core.JObject;
import net.vpc.common.jeep.util.JTypeUtils;
import net.vpc.common.jeep.util.JeepReflectUtils;

import java.lang.reflect.Modifier;

public class DefaultJField extends AbstractJField implements JRawField {
    private int modifiers;
    private String name;
    private JType declaringType;
    private JType genericType;
    private JType type;

    public DefaultJField() {
    }

    public int getModifiers() {
        return modifiers;
    }

    public DefaultJField setModifiers(int modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    public DefaultJField setName(String name) {
        this.name = name;
        return this;
    }



    public DefaultJField setDeclaringType(JType declaringType) {
        this.declaringType = declaringType;
        return this;
    }

    public JType getType() {
        return type;
    }

    public DefaultJField setGenericType(JType genericType) {
        this.genericType = genericType;
        this.type= JTypeUtils.buildRawType(genericType,declaringType());
        return this;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public JType type() {
        return type;
    }

    @Override
    public Object get(Object instance) {
        if(isStatic()){
            return declaringType.staticObject().get(name);
        }else if(instance instanceof JObject){
            return ((JObject)instance).get(name());
        }else{
            return JeepReflectUtils.getInstanceFieldValue(instance,name);
        }
    }

    @Override
    public void set(Object instance, Object value) {
        if(isStatic()){
            declaringType().staticObject().set(name(),value);
        }else if(instance instanceof JObject){
            ((JObject)instance).set(name(),value);
        }else{
            JeepReflectUtils.setInstanceFieldValue(instance,name,value);
        }
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(modifiers);
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    @Override
    public JType declaringType() {
        return declaringType;
    }

    @Override
    public JType genericType() {
        return genericType;
    }

    @Override
    public int modifiers() {
        return modifiers;
    }
}

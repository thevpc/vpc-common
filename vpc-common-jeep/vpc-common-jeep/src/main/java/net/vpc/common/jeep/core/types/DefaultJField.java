package net.vpc.common.jeep.core.types;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JRawField;
import net.vpc.common.jeep.JTypes;
import net.vpc.common.jeep.core.JObject;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.impl.types.DefaultJAnnotationInstanceList;
import net.vpc.common.jeep.impl.types.DefaultJModifierList;
import net.vpc.common.jeep.impl.types.JAnnotationInstanceList;
import net.vpc.common.jeep.impl.types.JModifierList;
import net.vpc.common.jeep.util.JTypeUtils;
import net.vpc.common.jeep.util.JeepReflectUtils;

import java.lang.reflect.Modifier;

public class DefaultJField extends AbstractJField implements JRawField {
    private String name;
    private JType declaringType;
    private JType genericType;
    private JType type;
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();
    private JModifierList modifiers = new DefaultJModifierList();

    public DefaultJField() {
    }

    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public boolean isFinal() {
        return ((JTypesSPI)getTypes()).isFinalField(this);
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
        this.type= JTypeUtils.buildRawType(genericType, getDeclaringType());
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
            return declaringType.getStaticObject().get(name);
        }else if(instance instanceof JObject){
            return ((JObject)instance).get(name());
        }else{
            return JeepReflectUtils.getInstanceFieldValue(instance,name);
        }
    }

    @Override
    public void set(Object instance, Object value) {
        if(isStatic()){
            getDeclaringType().getStaticObject().set(name(),value);
        }else if(instance instanceof JObject){
            ((JObject)instance).set(name(),value);
        }else{
            JeepReflectUtils.setInstanceFieldValue(instance,name,value);
        }
    }

    @Override
    public boolean isPublic() {
        return ((JTypesSPI)getTypes()).isPublicField(this);
    }

    @Override
    public boolean isStatic() {
        return ((JTypesSPI)getTypes()).isStaticField(this);
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    @Override
    public JType genericType() {
        return genericType;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    @Override
    public JTypes getTypes() {
        return getDeclaringType().getTypes();
    }
}

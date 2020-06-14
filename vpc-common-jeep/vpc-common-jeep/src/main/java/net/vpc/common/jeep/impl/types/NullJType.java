package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JStaticObject;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.impl.types.host.AbstractJType;

import java.lang.reflect.Modifier;

public class NullJType extends AbstractJType {
    private String name;
    public NullJType(JTypes types) {
        super(types);
        name="null";
    }

//    @Override
//    public JType[] actualTypeArguments() {
//        return new JType[0];
//    }

    @Override
    public JType rawType() {
        return this;
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JStaticObject staticObject() {
        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String simpleName() {
        return name();
    }

//    @Override
//    public JType toArray(int count) {
//        return getRegisteredOrRegister(new NullJType(dim+count, types()));
//    }

//    @Override
//    public Object newArray(int... len) {
//        if (len.length == 0) {
//            throw new IllegalArgumentException("zero len");
//        }
//        int len0 = len[0];
//        JType jType = componentType();
//        return Array.newInstance(Object.class,len);
//    }



    @Override
    public JType getSuperType() {
        return null;
    }

    @Override
    public JType[] interfaces() {
        return new JType[0];
    }
    @Override
    public JType toPrimitive() {
        return this;
    }

    @Override
    public JConstructor defaultConstructorOrNull() {
        return null;
    }

    @Override
    public JConstructor defaultConstructor() {
        return null;
    }

    @Override
    public JConstructor[] declaredConstructors() {
        return new JConstructor[0];
    }

    @Override
    public JField declaredFieldOrNull(String fieldName) {
        return null;
    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        return this;
//    }

    @Override
    public JMethod[] declaredMethods() {
        return new JMethod[0];
    }

    @Override
    public JMethod declaredMethodOrNull(JSignature sig) {
        return null;
    }

    @Override
    public JConstructor declaredConstructorOrNull(JSignature sig) {
        return null;
    }


    @Override
    public Object defaultValue() {
        return null;
    }

    @Override
    public JType declaringType() {
        return null;
    }

    @Override
    public String packageName() {
        return null;
    }

    @Override
    public String[] getExports() {
        return new String[0];
    }

    @Override
    public JField[] declaredFields() {
        return new JField[0];
    }

    @Override
    public JType[] declaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public JDeclaration declaration() {
        return null;
    }

    @Override
    public boolean isNullable() {
        return true;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        return this;
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count),types()
        );
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public int modifiers() {
        return Modifier.PUBLIC;
    }

    @Override
    public boolean isInterface() {
        return false;
    }
}

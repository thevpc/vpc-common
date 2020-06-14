package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JStaticObject;
import net.vpc.common.jeep.impl.functions.JSignature;

public class JRawTypeDelegate implements JRawType {
    private JRawType base;

    public JRawTypeDelegate(JRawType base) {
        this.base = base;
    }

    @Override
    public String gname() {
        return base.gname();
    }

    @Override
    public JType parametrize(JType... parameters) {
        JType p = base.parametrize(parameters);
        if (p == base) {
            return this;
        }
        return p;
    }

    @Override
    public JDeclaration declaration() {
        return base.declaration();
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return base.typeParameters();
    }

    @Override
    public JType rawType() {
        return base.rawType();
    }

    @Override
    public JStaticObject staticObject() {
        return base.staticObject();
    }

    @Override
    public String name() {
        return base.name();
    }

    @Override
    public JTypeName typeName() {
        return base.typeName();
    }

    @Override
    public JTypes types() {
        return base.types();
    }

    @Override
    public String dname() {
        return base.dname();
    }

    @Override
    public String simpleName() {
        return base.simpleName();
    }

    @Override
    public Object cast(Object o) {
        return base.cast(o);
    }

    @Override
    public JType boxed() {
        JType b = base.boxed();
        if (b == base) {
            return this;
        }
        return b;
    }

    @Override
    public int modifiers() {
        return base.modifiers();
    }

    @Override
    public boolean isPublic() {
        return base.isPublic();
    }

    @Override
    public boolean isStatic() {
        return base.isStatic();
    }

    @Override
    public boolean isNullable() {
        return base.isNullable();
    }

    @Override
    public boolean isPrimitive() {
        return base.isPrimitive();
    }

    @Override
    public boolean isAssignableFrom(JType other) {
        if(other.name().equals("null")){
            if(isNullable()){
                return true;
            }
        }
        return base.isAssignableFrom(other);
    }

    @Override
    public boolean isInstance(Object instance) {
        return base.isInstance(instance);
    }

    @Override
    public boolean isArray() {
        return base.isArray();
    }

    @Override
    public JType toArray(int count) {
        return base.toArray(count);
    }

    @Override
    public JType getSuperType() {
        return null;
    }

    @Override
    public JType firstCommonSuperType(JType other) {
        return null;
    }

    @Override
    public JType toPrimitive() {
        return null;
    }

    @Override
    public JField matchedField(String fieldName) {
        return null;
    }

//    @Override
//    public JMethod findMethodMatchOrNull(JSignature signature, JContext context) {
//        return null;
//    }
//
//    @Override
//    public JConstructor findConstructorMatch(JSignature signature, JContext context) {
//        return null;
//    }
//
//    @Override
//    public JConstructor findConstructorMatchOrNull(JSignature signature, JContext context) {
//        return null;
//    }
//
//    @Override
//    public JMethod findMethodMatch(JSignature signature, JContext context) {
//        return null;
//    }

    @Override
    public JConstructor addConstructor(JSignature signature, String[] argNames, JInvoke handler, int modifiers, boolean redefine) {
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public JField addField(String name, JType type, int modifiers, boolean redefine) {
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public JMethod addMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, int modifiers, boolean redefine) {
       throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public JMethod declaredMethod(String sig) {
        return null;
    }

    @Override
    public JMethod declaredMethod(JSignature sig) {
        return null;
    }

    @Override
    public JMethod declaredMethodOrNull(JSignature sig) {
        return null;
    }

    @Override
    public JConstructor declaredConstructorOrNull(String sig) {
        return null;
    }

    @Override
    public JConstructor declaredConstructor(String sig) {
        return null;
    }

    @Override
    public JType[] interfaces() {
        return new JType[0];
    }

    @Override
    public JConstructor declaredConstructor(JSignature sig) {
        return null;
    }

    @Override
    public JConstructor declaredConstructor(JType... parameterTypes) {
        return null;
    }

    @Override
    public JConstructor[] publicConstructors() {
        return new JConstructor[0];
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
    public JField declaredField(String fieldName) {
        return null;
    }

    @Override
    public JField[] declaredFields() {
        return new JField[0];
    }

    @Override
    public JField declaredFieldOrNull(String fieldName) {
        return null;
    }

    @Override
    public JField publicField(String name) {
        return null;
    }

    @Override
    public JMethod[] publicMethods() {
        return new JMethod[0];
    }

    @Override
    public JMethod[] declaredMethods() {
        return new JMethod[0];
    }

    @Override
    public JMethod[] declaredMethods(String name) {
        return new JMethod[0];
    }

    @Override
    public JField[] declaredFieldsWithParents() {
        return new JField[0];
    }

    @Override
    public JType[] declaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public JType declaredInnerType(String name) {
        return null;
    }

    @Override
    public JType declaredInnerTypeOrNull(String name) {
        return null;
    }

    @Override
    public JMethod declaredMethodOrNull(String sig) {
        return null;
    }

    @Override
    public JMethod[] declaredMethods(String[] names, int callArgumentsCount, boolean includeParents) {
        return new JMethod[0];
    }

    @Override
    public JMethod[] declaredMethods(boolean includeParents) {
        return new JMethod[0];
    }

    @Override
    public JType[] parents() {
        return new JType[0];
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
    public boolean isRawType() {
        return false;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        return null;
    }

    @Override
    public boolean isVar() {
        return false;
    }

    @Override
    public JTypeVariable toVar() {
        return null;
    }

    @Override
    public boolean isInterface() {
        return base.isInterface();
    }

}

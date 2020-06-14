package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JStaticObject;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.impl.types.host.AbstractJType;
import net.vpc.common.jeep.util.JTypeUtils;

import java.lang.reflect.Modifier;
import java.util.*;

public abstract class AbstractJTypeVariable extends AbstractJType implements JTypeVariable {
    public AbstractJTypeVariable(JTypes types) {
        super(types);
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JType rawType() {
        return this;
    }

    @Override
    public JStaticObject staticObject() {
        return null;
    }

    @Override
    public String simpleName() {
        return name();
    }

    @Override
    public boolean isNullable() {
        JType[] jTypes = upperBounds();
        for (JType jType : jTypes) {
            if (jType.isNullable()) {
                return true;
            }
        }
        return jTypes.length == 0;
    }

    @Override
    public JType getSuperType() {
        JType jType = JTypeUtils.firstCommonSuperType(upperBounds());
        if (jType == null) {
            jType = JTypeUtils.forObject(types());
        }
        return jType;
    }

    @Override
    public JType[] interfaces() {
        LinkedHashSet<JType> infs = new LinkedHashSet<>();
        for (JType ub : upperBounds()) {
            infs.addAll(Arrays.asList(ub.interfaces()));
        }
        return infs.toArray(new JType[0]);
    }

    @Override
    public JConstructor[] declaredConstructors() {
        //how to do this by overcoming Java spec?
        return new JConstructor[0];
    }

    @Override
    public JField[] declaredFields() {
        List<JField> a = new ArrayList<>();
        for (JType jType : upperBounds()) {
            a.addAll(Arrays.asList(jType.declaredFields()));
        }
        return a.toArray(new JField[0]);
    }

    @Override
    public JMethod[] declaredMethods() {
        List<JMethod> a = new ArrayList<>();
        for (JType jType : upperBounds()) {
            a.addAll(Arrays.asList(jType.declaredMethods()));
        }
        return a.toArray(new JMethod[0]);
    }

    @Override
    public JType[] declaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public Object defaultValue() {
        JType[] upperBounds = upperBounds();
        for (int i = 1, upperBoundsLength = upperBounds.length; i < upperBoundsLength; i++) {
            if (!Objects.equals(upperBounds[i].defaultValue(), upperBounds[i - 1].defaultValue())) {
                return null;
            }
        }
        if (upperBounds.length >= 1) {
            return upperBounds[0].defaultValue();
        }
        return null;
    }

    @Override
    public JType declaringType() {
        JDeclaration d = declaration();
        while (d != null) {
            if (d instanceof JType) {
                return (JType) d;
            }
            d = d.declaration();
        }
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
    public boolean isPrimitive() {
        JType[] jTypes = upperBounds();
        for (JType jType : jTypes) {
            if (!jType.isPrimitive()) {
                return false;
            }
        }
        return jTypes.length == 1;
    }

    @Override
    public boolean isVar() {
        return true;
    }

    @Override
    public JTypeVariable toVar() {
        return this;
    }

    @Override
    public JConstructor defaultConstructor() {
        return null;
    }

    @Override
    public JConstructor declaredConstructorOrNull(JSignature sig) {
        return null;
    }

//    @Override
//    public JType[] actualTypeArguments() {
//        return new JType[0];
//    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        return this;
//    }

    @Override
    public JConstructor defaultConstructorOrNull() {
        return null;
    }

    @Override
    public boolean isWildcard() {
        return name().equalsIgnoreCase("?");
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public int modifiers() {
        return Modifier.PUBLIC;
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
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count),
                types()
        );
    }

    @Override
    public String getVName() {
        JDeclaration d = declaration();
        return name()+":"+d;
    }
}

package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.impl.types.host.HostJRawType;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class JTypesHostHelper {
    private JTypesSPI types;

    public JTypesHostHelper(JTypes types) {
        this.types = (JTypesSPI) types;
    }
    public JType forName(Type ctype) {
        return (JType) forNameOrVar(ctype, null);
    }

    public JType forName(Type ctype, JDeclaration declaration) {
        return (JType) forNameOrVar(ctype, declaration);
    }

    public JType forNameOrVar(Type ctype, JDeclaration declaration) {
        if (ctype instanceof Class) {
            Class clazz = (Class) ctype;
            if (clazz.isArray()) {
                return forName(clazz.getComponentType(), declaration).toArray();
            }
            JType found = types.getRegisteredOrAlias(getCanonicalTypeName(ctype));
            if (found != null) {
                return found;
            }
            //each context should have its proper types repository...
            //so wont inherit parent's
//            if (parent != null) {
//                found = parent.forName(ctype, declaration);
//                if (found != null) {
//                    return found;
//                }
//            }
            found = createRawType0((Class) ctype);
            types.registerType(found);
            return found;
        } else if (ctype instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) ctype;
            Type rawType = pt.getRawType();
            JType rootRaw = forNameOrVar(rawType, declaration);
            return typesSPI().createParameterizedType0(
                    rootRaw,
                    forNameOrVar(pt.getActualTypeArguments(), declaration),
                    rootRaw.getDeclaringType()
            );
        } else if (ctype instanceof TypeVariable) {
            TypeVariable tv = (TypeVariable) ctype;
            return types.createVarType0(
                    tv.getName(),
                    new JType[0],
                    forNameOrVar(tv.getBounds(),declaration),
                    declaration
            );
        } else if (ctype instanceof WildcardType) {
            WildcardType tv = (WildcardType) ctype;
            return types.createVarType0(
                    "?",
                    forNameOrVar(tv.getLowerBounds(),declaration),
                    forNameOrVar(tv.getUpperBounds(),declaration),
                    declaration
            );
        } else if (ctype instanceof GenericArrayType) {
            Type c = ((GenericArrayType) ctype).getGenericComponentType();
            return forNameOrVar(c,declaration).toArray();
        } else {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    protected HostJRawType createRawType0(Class ctype) {
        return new HostJRawType(ctype, (JTypes) types);
    }

    public JTypes types() {
        return (JTypes) types;
    }
    public JTypesSPI typesSPI() {
        return (JTypesSPI) types;
    }

    public JType[] forName(Type[] clazz) {
        return forName(clazz, null);
    }

    public JType[] forName(Type[] names, JDeclaration declaration) {
        JType[] jTypes = new JType[names.length];
        for (int i = 0; i < jTypes.length; i++) {
            jTypes[i] = forName(names[i], declaration);
        }
        return jTypes;
    }

    public JType[] forNameOrVar(Type[] names, JDeclaration declaration) {
        JType[] jTypes = new JType[names.length];
        for (int i = 0; i < jTypes.length; i++) {
            jTypes[i] = forNameOrVar(names[i], declaration);
        }
        return jTypes;
    }

    protected JType forName0(String name){
        switch (name) {
            case "Object":
            case "object":
                return forName(Object.class);
            case "Date":
                return forName(java.util.Date.class);
            case "Character":
                return forName(Character.class);
            case "String":
            case "string":
                return forName(String.class);
            case "stringb":
                return forName(StringBuilder.class);
            case "Int":
            case "Integer":
                return forName(Integer.class);
            case "Long":
                return forName(Long.class);
            case "Double":
                return forName(Double.class);
            case "Float":
                return forName(Float.class);
            case "Short":
                return forName(Short.class);
            case "Byte":
                return forName(Byte.class);
            case "Boolean":
                return forName(Boolean.class);
            case "Void":
                return forName(Void.class);

            case "char":
                return forName(char.class);
            case "int":
                return forName(int.class);
            case "long":
                return forName(long.class);
            case "double":
                return forName(double.class);
            case "float":
                return forName(float.class);
            case "short":
                return forName(short.class);
            case "byte":
                return forName(byte.class);
            case "bool":
            case "boolean":
                return forName(boolean.class);
            case "void":
                return forName(void.class);
            case "date":
                return forName(LocalDate.class);
            case "datetime":
                return forName(LocalDateTime.class);
            case "time":
                return forName(LocalTime.class);
        }
        ClassLoader hostClassLoader=types.hostClassLoader();
        Class<?> t = null;
        try {
            //i should replace this witch
            if (hostClassLoader == null) {
                return forName(Class.forName(name));
            } else {
                return forName(Class.forName(name, false, hostClassLoader));
            }
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }

    public String getCanonicalTypeName(Type ctype) {
        if (ctype instanceof Class) {
            return ((Class) ctype).getCanonicalName();
        }
        return ctype.getTypeName();
    }

}

package net.vpc.common.util;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

public final class TypeName<C> implements Serializable {
    private static final long serialVersionUID = 1;
    private final String typeName;
    private final TypeName[] parameters;

    public TypeName(String name, TypeName... parameters) {
        if (name.contains("<")) {
            if (parameters.length != 0) {
                throw new IllegalArgumentException("Could not use <> names with effective parameters");
            }
            throw new IllegalArgumentException("Not Supported yet");
        } else {
            this.typeName = name;
            this.parameters = parameters;
        }
    }

    public String getTypeName() {
        return typeName;
    }

    public Class getTypeClass() {
        return toCls(typeName, null);
    }

    public Class getTypeClass(ClassLoader cl) {
        return toCls(typeName, cl);
    }

    public int getParametersCount() {
        return parameters.length;
    }

    public TypeName[] getParameters() {
        return Arrays.copyOf(parameters, parameters.length);
    }

    public boolean isAssignableFrom(TypeName cls) {
        return getTypeClass().isAssignableFrom(cls.getTypeClass());
    }

    private Class toCls(String typeName, ClassLoader cl) {
        try {
            return Class.forName(typeName, true, cl == null ? Thread.currentThread().getContextClassLoader() : cl);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean isInterface() {
        return getTypeClass().isInterface();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeName that = (TypeName) o;
        return Objects.equals(typeName, that.typeName) &&
                Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(typeName);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

    public TypeName[] getInterfaces() {
        Class[] interfaces = getTypeClass().getInterfaces();
        TypeName[] typeReferences = new TypeName[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            typeReferences[i] = of(interfaces[i]);//TODO params?
        }
        return typeReferences;
    }

    public TypeName getSuperclass() {
        Class superclass = getTypeClass().getSuperclass();
        if (superclass == null) {
            return null;
        }
        return of(superclass);
    }

    //    public static TypeName of(Type type, Type... args) {
//        if (type instanceof ParameterizedType) {
//            TypeName[] ee=new TypeName[args.length];
//            for (int i = 0; i < ee.length; i++) {
//                ee[i]=of(args[i]);
//            }
//            ParameterizedType ptype = (ParameterizedType) type;
//            return new TypeName(ptype.getRawType().toString(),ee);
//        }
//        return new TypeName(type.toString()) ;
//    }
    public static TypeName of(Type type, TypeName... args) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            return new TypeName(ptype.getRawType().toString(), args);
        }
        return new TypeName(type.toString());
    }
//    public static TypeName of(Type type, Type... args) {
//        if (type instanceof ParameterizedType) {
//            TypeName[] ee=new TypeName[args.length];
//            for (int i = 0; i < ee.length; i++) {
//                ee[i]=of(args[i]);
//            }
//            ParameterizedType ptype = (ParameterizedType) type;
//            return new TypeName(ptype.getRawType().toString());
//        }
//        return new TypeName(type.toString()) ;
//    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(typeName);
        if (parameters.length > 0) {
            sb.append("<");
            for (TypeName parameter : parameters) {
                if (sb.charAt(sb.length() - 1) != '<') {
                    sb.append(",");
                }
                sb.append(parameter.getTypeName());
            }
            sb.append(">");
        }
        return sb.toString();
    }
}

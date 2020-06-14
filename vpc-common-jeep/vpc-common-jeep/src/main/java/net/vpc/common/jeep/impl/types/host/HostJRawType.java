package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JStaticObject;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.JRawType;
import net.vpc.common.jeep.impl.types.JTypesHostHelper;
import net.vpc.common.jeep.util.JeepPlatformUtils;

import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class HostJRawType extends AbstractJRawType implements JRawType {
    private Class hostType;
    //    private JType rawType;
    private String name;
    private String gname;
    private String simpleName;
    private JConstructor defaultConstructor;
    private LinkedHashMap<String, JField> fields;
    private LinkedHashMap<String, JType> innerTypes;
    private LinkedHashMap<JSignature, JMethod> methods;
    private LinkedHashMap<JSignature, JConstructor> constructors;
    private JTypeVariable[] typeParameters = new JTypeVariable[0];
    private String[] exports = new String[0];
    private int modifiers;

    private JStaticObject so = new JStaticObject() {
        @Override
        public JType type() {
            return HostJRawType.this;
        }

        @Override
        public Object get(String name) {
            try {
                Field field = ((Class) hostType).getField(name);
                JeepPlatformUtils.setAccessibleWorkaround(field);
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Field not accessible " + name() + "." + name);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Field not found " + name() + "." + name);
            }
        }

        @Override
        public void set(String name, Object o) {
            try {
                Field field = ((Class) hostType).getField(name);
                JeepPlatformUtils.setAccessibleWorkaround(field);
                field.set(null, o);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Field not accessible " + name() + "." + name);
            } catch (NoSuchFieldException e) {
                throw new IllegalArgumentException("Field not found " + name() + "." + name);
            }
        }
    };

    public HostJRawType(Class hostType, JTypes types) {
        super(types);
        this.hostType = hostType;
        Class aclazz = (Class) hostType;
        this.name = aclazz.getCanonicalName();
        this.simpleName = aclazz.getSimpleName();
        this.gname = name;
        TypeVariable[] typeParameters = aclazz.getTypeParameters();
        JTypeVariable[] r = new JTypeVariable[typeParameters.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = (JTypeVariable) htypes().forNameOrVar(typeParameters[i], this);
            if (i == 0) {
                gname += "<" + r[i].name();
            } else {
                gname += "," + r[i].name();
            }
            if (i == r.length - 1) {
                gname += ">";
            }
        }
        this.typeParameters = r;
        Set<String> exportsList = new LinkedHashSet<>();
        for (JTypesResolver resolver : types.resolvers()) {
            String[] strings = resolver.resolveTypeExports(aclazz);
            if (strings != null) {
                for (String string : strings) {
                    if (string != null) {
                        exportsList.add(string);
                    }
                }
            }
        }
        this.exports = exportsList.toArray(new String[0]);
        this.modifiers = hostType.getModifiers();
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
    public int modifiers() {
        return modifiers;
    }

    @Override
    public String gname() {
        return gname;
    }

    protected Map<String, JField> _fields() {
        if (fields == null) {
            fields = new LinkedHashMap<>();
//            if (hostType instanceof Class) {
            for (Field declaredField : ((Class) hostType).getDeclaredFields()) {
                HostJField f = new HostJField(declaredField, null, this);
                fields.put(f.name(), f);
            }
//            } else {
//                for (Field declaredField : rawClass().getDeclaredFields()) {
//                    Type gt = declaredField.getGenericType();
//                    JType y = types().forNameOrVar(gt, this);
//                    HostJField f = new HostJField(declaredField, y, this);
//                    fields.put(f.name(), f);
//                }
//            }
        }
        return fields;
    }

//    @Override
//    public final JType toArray(int count) {
//        if (hostType instanceof Class) {
//            Class t = (Class) hostType;
//            for (int i = 0; i < count; i++) {
//                t = Array.newInstance(t, 0).getClass();
//            }
//            return getRegisteredOrRegister(new HostJRawType(t, types()));
//        } else {
//            HostJRawType h = new HostJRawType(types(), rawType, actualTypeArguments, arrayDimension + count);
//            return getRegisteredOrRegister(h);
//        }
//    }

//    @Override
//    public Object newArray(int... len) {
//        if (arrayRootClazz == null) {
//            if (hostType instanceof Class) {
//                return Array.newInstance((Class<?>) hostType, len);
//            }else{
//                return Array.newInstance(rawClass(), len);
//            }
//        } else if (arrayRootClazz instanceof Class) {
//            return Array.newInstance((Class<?>) arrayRootClazz, len);
//        }else{
//            return Array.newInstance(rawClass(), len);
//        }
//    }

//    private JType resolveDefaultImplClass() {
//        //this should be handled in more customizable way!!
//        if (List.class.equals(hostType)) {
//            return types().forName(ArrayList.class);
//        }
//        if (Set.class.equals(hostType)) {
//            return types().forName(HashSet.class);
//        }
//        if (Map.class.equals(hostType)) {
//            return types().forName(HashMap.class);
//        }
//        return null;
//    }

    private synchronized Map<JSignature, JMethod> _methods() {
        if (methods == null) {
            methods = new LinkedHashMap<>();
            for (Method item : hostType.getDeclaredMethods()) {
                HostJRawMethod f = new HostJRawMethod(item, this);
                methods.put(f.signature().toNoVarArgs(), f);
            }
        }
        return methods;
    }

    private synchronized Map<String, JType> _innerTypes() {
        if (innerTypes == null) {
            innerTypes = new LinkedHashMap<>();
            for (Class item : hostType.getDeclaredClasses()) {
                HostJRawType f = (HostJRawType) htypes().forName(item);
                innerTypes.put(f.simpleName(), f);
            }
        }
        return innerTypes;
    }


    private synchronized LinkedHashMap<JSignature, JConstructor> _constructors() {
        if (constructors == null) {
            constructors = new LinkedHashMap<>();
//            if (hostType instanceof Class) {
            for (Constructor item : hostType.getDeclaredConstructors()) {
                HostJRawConstructor f = new HostJRawConstructor(item, this);
                constructors.put(f.signature().toNoVarArgs(), f);
            }
//            }
        }
        return constructors;
    }

    @Override
    public Object cast(Object o) {
        return hostType.cast(o);
    }

    @Override
    public JType boxed() {
        return htypes().forName(JeepPlatformUtils.toBoxingType(hostType));
    }

    @Override
    public boolean isPrimitive() {
        return hostType.isPrimitive();
    }

    @Override
    public JType toPrimitive() {
        if (isPrimitive()) {
            return this;
        }
        Class p = JeepPlatformUtils.REF_TO_PRIMITIVE_TYPES.get((Class) hostType);
        return p == null ? null : htypes().forName(p);
    }

    @Override
    public JMethod declaredMethodOrNull(JSignature sig) {
        return _methods().get(sig.toNoVarArgs());
    }

//    @Override
//    public JType toArray(int count) {
//        Class t = clazz;
//        for (int i = 0; i < count; i++) {
//            t = Array.newInstance(t, 0).getClass();
//        }
//        return types.forName(t);
//    }

    @Override
    public synchronized JField declaredFieldOrNull(String fieldName) {
        return _fields().get(fieldName);
    }

    @Override
    public JConstructor declaredConstructorOrNull(JSignature sig) {
        sig = sig.setName(name());
        JConstructor f = _constructors().get(sig.toNoVarArgs());
        if (f != null) {
            return f;
        }
        return null;
    }

    @Override
    public JConstructor defaultConstructorOrNull() {
        if (defaultConstructor == null) {
            JConstructor defaultConstructor = declaredConstructor(JSignature.of(name(), new JType[0]));
            if (defaultConstructor.isPublic()) {
                this.defaultConstructor = defaultConstructor;
            }
        }
        return this.defaultConstructor;
    }

    @Override
    public JType declaredInnerTypeOrNull(String name) {
        return _innerTypes().get(name);
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return typeParameters;
    }

    @Override
    public JType rawType() {
        return this;
    }

    @Override
    public JStaticObject staticObject() {
        return so;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String simpleName() {
        return simpleName;
    }

    @Override
    public boolean isNullable() {
        return !isPrimitive();
    }

    @Override
    public JType getSuperType() {
        if (hostType instanceof Class) {
            //TODO FIX ME
//            Type s = ((Class) hostType).getGenericSuperclass();
//            return s == null ? null : htypes().forName(s, this);
            Class s = ((Class) hostType).getSuperclass();
            return s == null ? null : htypes().forName(s, this);

        } else {
            JType rc = rawType();
            JType superclass = rc.getSuperType();
            if (superclass == null) {
                return superclass;
            } else {
                //should i update vars?
                return superclass;
            }
        }
    }

    @Override
    public JConstructor addConstructor(JSignature signature, String[] argNames, JInvoke handler, int modifiers, boolean redefine) {
        throw new IllegalArgumentException("Not supported yet");
    }

    @Override
    public JField addField(String name, JType type, int modifiers, boolean redefine) {
        throw new IllegalArgumentException("Not supported yet");
    }

    @Override
    public JMethod addMethod(JSignature signature, String[] argNames, JType returnType, JInvoke handler, int modifiers, boolean redefine) {
        throw new IllegalArgumentException("Not supported yet");
    }

//    @Override
//    public boolean isAssignableFrom(JType other) {
//        if (other instanceof HostJRawType) {
//            if (clazz instanceof Class && ((HostJRawType) other).clazz instanceof Class) {
//                return ((Class) clazz).isAssignableFrom((Class) ((HostJRawType) other).clazz);
//            }
//        }
//        return false;
//    }

    @Override
    public JType[] interfaces() {
        if (hostType instanceof Class) {
//            Type[] interfaces = ((Class) hostType).getGenericInterfaces();
//            JType[] ii = new JType[interfaces.length];
//            for (int i = 0; i < ii.length; i++) {
//                ii[i] = htypes().forName(interfaces[i], this);
//            }
//            return ii;
            Class[] interfaces = hostType.getInterfaces();
            JType[] ii = new JType[interfaces.length];
            for (int i = 0; i < ii.length; i++) {
                ii[i] = htypes().forName(interfaces[i], this);
            }
            return ii;
        } else {
            JType rc = rawType();
            JType[] superInterfaces = rc.interfaces();
            //should i update vars?
            return superInterfaces;
        }
    }

    @Override
    public JConstructor[] declaredConstructors() {
        return _constructors().values().toArray(new JConstructor[0]);
    }

    @Override
    public JField[] declaredFields() {
        return _fields().values().toArray(new JField[0]);
    }

    @Override
    public JMethod[] declaredMethods() {
        return _methods().values().toArray(new JMethod[0]);
    }

    @Override
    public JType[] declaredInnerTypes() {
        return _innerTypes().values().toArray(new JType[0]);
    }

    @Override
    public Object defaultValue() {
        switch (name()) {
            case "char":
                return '\0';
            case "boolean":
                return false;
            case "byte":
                return (byte) 0;
            case "short":
                return (short) 0;
            case "int":
                return 0;
            case "long":
                return 0L;
            case "float":
                return 0.0f;
            case "double":
                return 0.0;
        }
        return null;
    }

    @Override
    public JType declaringType() {
        Class dc = this.hostType.getDeclaringClass();
        if (dc == null) {
            return null;
        }
        return htypes().forName(dc, this);
    }

    protected JTypesHostHelper htypes(){
        return new JTypesHostHelper(types());
    }

    @Override
    public String packageName() {
        Package p = this.hostType.getPackage();
        if (p == null) {
            return null;
        }
        return p.getName();
    }

    @Override
    public String[] getExports() {
        return exports;
    }

    public Type hostType() {
        return hostType;
    }

    @Override
    public boolean isInterface() {
        return hostType.isInterface();
    }
}

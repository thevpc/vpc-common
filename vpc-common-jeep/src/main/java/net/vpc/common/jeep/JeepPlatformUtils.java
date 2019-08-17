package net.vpc.common.jeep;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author vpc
 */
public class JeepPlatformUtils {

    final static Map<Class, Class> PRIMITIVE_TO_REF_TYPES = new HashMap<Class, Class>();
    final static Map<Class, Class> REF_TO_PRIMITIVE_TYPES = new HashMap<Class, Class>();
    static Map<String, ExpressionEvaluatorConverter> cached_createTypeImplicitConversions = new HashMap<>();
    static Map<GetMatchingMethodKey, Method> cached_getMatchingMethod = new HashMap<>();

    static {
//        DEFAULT_VALUES_BY_TYPE.put(Short.TYPE, (short) 0);
//        DEFAULT_VALUES_BY_TYPE.put(Long.TYPE, 0L);
//        DEFAULT_VALUES_BY_TYPE.put(Integer.TYPE, 0);
//        DEFAULT_VALUES_BY_TYPE.put(Double.TYPE, 0.0);
//        DEFAULT_VALUES_BY_TYPE.put(Float.TYPE, 0.0f);
//        DEFAULT_VALUES_BY_TYPE.put(Byte.TYPE, (byte) 0);
//        DEFAULT_VALUES_BY_TYPE.put(Character.TYPE, (char) 0);
//        DEFAULT_VALUES_BY_TYPE.put(Boolean.TYPE, Boolean.FALSE);

        PRIMITIVE_TO_REF_TYPES.put(Short.TYPE, Short.class);
        PRIMITIVE_TO_REF_TYPES.put(Long.TYPE, Long.class);
        PRIMITIVE_TO_REF_TYPES.put(Integer.TYPE, Integer.class);
        PRIMITIVE_TO_REF_TYPES.put(Double.TYPE, Double.class);
        PRIMITIVE_TO_REF_TYPES.put(Float.TYPE, Float.class);
        PRIMITIVE_TO_REF_TYPES.put(Byte.TYPE, Byte.class);
        PRIMITIVE_TO_REF_TYPES.put(Character.TYPE, Character.class);
        PRIMITIVE_TO_REF_TYPES.put(Boolean.TYPE, Boolean.class);

        REF_TO_PRIMITIVE_TYPES.put(Short.class, Short.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Long.class, Long.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Integer.class, Integer.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Double.class, Double.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Float.class, Float.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Byte.class, Byte.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Character.class, Character.TYPE);
        REF_TO_PRIMITIVE_TYPES.put(Boolean.class, Boolean.TYPE);
    }

    public static Method getMatchingMethod(Class cls, String methodName, Class... parameterTypes) {
        return getMatchingMethod(true, cls, methodName, parameterTypes);
    }

    private static class GetMatchingMethodKey {

        boolean accessibleOnly;
        Class cls;
        String methodName;
        Class[] parameterTypes;

        public GetMatchingMethodKey(boolean accessibleOnly, Class cls, String methodName, Class[] parameterTypes) {
            this.accessibleOnly = accessibleOnly;
            this.cls = cls;
            this.methodName = methodName;
            this.parameterTypes = parameterTypes;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + (this.accessibleOnly ? 1 : 0);
            hash = 73 * hash + Objects.hashCode(this.cls);
            hash = 73 * hash + Objects.hashCode(this.methodName);
            hash = 73 * hash + Arrays.deepHashCode(this.parameterTypes);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GetMatchingMethodKey other = (GetMatchingMethodKey) obj;
            if (this.accessibleOnly != other.accessibleOnly) {
                return false;
            }
            if (!Objects.equals(this.methodName, other.methodName)) {
                return false;
            }
            if (!Objects.equals(this.cls, other.cls)) {
                return false;
            }
            if (!Arrays.deepEquals(this.parameterTypes, other.parameterTypes)) {
                return false;
            }
            return true;
        }

    }

    public static Method getMatchingMethod(boolean accessibleOnly, Class cls, String methodName, Class... parameterTypes) {
        GetMatchingMethodKey k = new GetMatchingMethodKey(accessibleOnly, cls, methodName, parameterTypes);
        Method old = cached_getMatchingMethod.get(k);
        if (old == null) {
            if (cached_getMatchingMethod.containsKey(k)) {
                return null;
            }
        } else {
            return old;
        }
        if(methodName.isEmpty() || !Character.isJavaIdentifierStart(methodName.charAt(0))){
            cached_getMatchingMethod.put(k, null);
            return null;
        }
        try {
//            System.out.println("\t DeclaredMethod? "+cls.getName()+"."+methodName);
            Method method = cls.getDeclaredMethod(methodName, parameterTypes);
            setAccessibleWorkaround(method);
            cached_getMatchingMethod.put(k, method);
            return method;
        } catch (NoSuchMethodException e) { // NOPMD - Swallow the exception
        }
        // search through all methods
        Method[] methods = accessibleOnly ? cls.getMethods() : cls.getDeclaredMethods();//cls.getMethods();
        List<MethodObject<Method>> m = new ArrayList<>();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                m.add(new MethodObject<>(
                        new ArgumentTypes(
                                methods[i].getParameterTypes(),
                                methods[i].isVarArgs()
                        ), methods[i]
                ));
            }
        }
        MethodObject<Method> rr = getMatchingMethod(m.toArray(new MethodObject[m.size()]), parameterTypes);
        if (rr == null) {
            cached_getMatchingMethod.put(k,null);
            return null;
        }
        setAccessibleWorkaround(rr.getMethod());
            cached_getMatchingMethod.put(k,rr.getMethod());
        return rr.getMethod();
    }

    public static Class toBoxingType(Class type) {
        Class t = PRIMITIVE_TO_REF_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static Class toPrimitiveTypeOrNull(Class type) {
        if (type.isPrimitive()) {
            return type;
        }
        Class t = REF_TO_PRIMITIVE_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return null;
    }

    public static Class toPrimitiveType(Class type) {
        Class t = REF_TO_PRIMITIVE_TYPES.get(type);
        if (t != null) {
            return t;
        }
        return type;
    }

    public static boolean isAssignableFrom(Class parent, Class child) {
        if (parent.isAssignableFrom(child)) {
            return true;
        }
        if (toBoxingType(parent).isAssignableFrom(toBoxingType(child))) {
            return true;
        }
        Class c1 = toPrimitiveTypeOrNull(parent);
        Class c2 = toPrimitiveTypeOrNull(child);
        if (c1 != null && c2 != null) {
            if (Boolean.TYPE.equals(c1)) {
                return c2.equals(Boolean.TYPE);
            }
            if (Byte.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE);
            }
            if (Short.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE);
            }
            if (Character.TYPE.equals(c1)) {
                return c2.equals(Character.TYPE);
            }
            if (Integer.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE);
            }
            if (Long.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE);
            }
            if (Float.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE);
            }
            if (Double.TYPE.equals(c1)) {
                return c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE) || c2.equals(Float.TYPE) || c2.equals(Double.TYPE);
            }
        }
        return false;
    }

    public static int getAssignationCost(Class parent, Class child) {
        if (parent.equals(child)) {
            return 0;
        }
        if (parent.isAssignableFrom(child)) {
            return 2;
        }
        if (toBoxingType(parent).isAssignableFrom(toBoxingType(child))) {
            return 1;
        }
        if (ExpressionNode.class.isAssignableFrom(parent)) {
            return 2;
        }
        Class c1 = toPrimitiveTypeOrNull(parent);
        Class c2 = toPrimitiveTypeOrNull(child);
        if (c1 != null && c2 != null) {
            if (c1.equals(c2)) {
                return 1;
            }
            if (Boolean.TYPE.equals(c1)) {
                if (c2.equals(Boolean.TYPE)) {
                    return 2;
                }
            }
            if (Byte.TYPE.equals(c1)) {
                if (c2.equals(Byte.TYPE)) {
                    return 2;
                }
            }
            if (Short.TYPE.equals(c1)) {
                if (c2.equals(Byte.TYPE) || c2.equals(Short.TYPE)) {
                    return 2;
                }
            }
            if (Character.TYPE.equals(c1)) {
                if (c2.equals(Character.TYPE)) {
                    return 2;
                }
            }
            if (Integer.TYPE.equals(c1)) {
                if (c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE)) {
                    return 2;
                }
            }
            if (Long.TYPE.equals(c1)) {
                if (c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE)) {
                    return 2;
                }
            }
            if (Float.TYPE.equals(c1)) {
                if (c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE)) {
                    return 2;
                }
            }
            if (Double.TYPE.equals(c1)) {
                if (c2.equals(Byte.TYPE) || c2.equals(Short.TYPE) || c2.equals(Character.TYPE) || c2.equals(Integer.TYPE) || c2.equals(Long.TYPE) || c2.equals(Float.TYPE) || c2.equals(Double.TYPE)) {
                    return 2;
                }
            }
        }
        return -1;
    }

    public static Class firstCommonSuperType(Class type1, Class type2) {
        if (type1 == null || type2 == null) {
            return Object.class;
        }
        if (type1.equals(Object.class) || type2.equals(Object.class)) {
            return Object.class;
        }
        if (type1.equals(type2)) {
            return type1;
        }
        if (type1.isAssignableFrom(type2)) {
            return type1;
        }
        if (type2.isAssignableFrom(type1)) {
            return type2;
        }
        return firstCommonSuperType(type1.getSuperclass(), type2);
    }

    //    public static void test(Object ... any){
//        
//    }
//    
//    public static void main(String[] args) {
//        Method all = getMatchingMethod(JeepPlatformUtils.class, "test",String.class);
//        System.out.println(all);
//    }
    public static <T> MethodObject<T> getMatchingMethod(MethodObject<T>[] available, Class... parameterTypes) {
        class MCall implements Comparable<MCall> {

            Class[] input;
            MethodObject<T> av;
            int[] cost;

            public MCall(Class[] input, MethodObject<T> av, int[] cost) {
                this.input = input;
                this.av = av;
                this.cost = cost;
            }

            @Override
            public int compareTo(MCall o) {
                int x = Integer.compare(cost.length, o.cost.length);
                if (x != 0) {
                    return x;
                }
                for (int i = 0; i < cost.length; i++) {
                    int val1 = cost[i];
                    int val2 = o.cost[i];
                    x = Integer.compare(val1, val2);
                    if (x != 0) {
                        return x;
                    }
                }
                return 0;
            }
        }

        //List<MCall> valid = new ArrayList<MCall>();
        MCall best = null;
        for (int i = 0; i < available.length; i++) {
            MethodObject<T> a = available[i];
            Class[] atypes = a.getSignature().getTypes();
            Class[] itypes = parameterTypes;
            if (atypes.length == itypes.length && !a.getSignature().isVarArgs()) {
                boolean ok = true;
                int[] cost = new int[atypes.length];
                for (int j = 0; j < atypes.length; j++) {
                    Class c1 = atypes[j];
                    Class c2 = itypes[j];
                    cost[j] = getAssignationCost(c1, c2);
                    if (cost[j] < 0) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    //check each type
                    MCall newCall = new MCall(parameterTypes, a, cost);
                    if (best == null || best.compareTo(newCall) > 0) {
                        best = newCall;
                    }
                }
            } else if ((atypes.length - 1) <= itypes.length && a.getSignature().isVarArgs()) {
                boolean ok = true;
                Class[] r = new Class[atypes.length];
                int[] cost = new int[atypes.length];
                for (int j = 0; j < atypes.length - 1; j++) {
                    Class c1 = atypes[j];
                    Class c2 = itypes[j];
                    cost[j] = getAssignationCost(c1, c2);
                    r[j] = c2;
                    if (cost[j] < 0) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    Class ll = atypes[atypes.length - 1].getComponentType();
                    int y = 0;
                    for (int j = atypes.length - 1; j < itypes.length; j++) {
                        Class itype = itypes[j];
                        int c = getAssignationCost(ll, itype);
                        if (c < 0) {
                            ok = false;
                        } else {
                            y += c;
                        }
                    }
                    if (ok) {
                        cost[atypes.length - 1] = y;
                        r[atypes.length - 1] = atypes[atypes.length - 1];
                    }
                }
                if (ok) {
                    MCall newCall = new MCall(r, a, cost);
                    if (best == null || best.compareTo(newCall) > 0) {
                        best = newCall;
                    }
                }
            }
        }
        return best == null ? null : best.av;
    }

    static void setAccessibleWorkaround(AccessibleObject o) {
        if (o == null || o.isAccessible()) {
            return;
        }
        try {
            o.setAccessible(true);
        } catch (SecurityException e) { // NOPMD
            // ignore in favor of subsequent IllegalAccessException
        }
    }
}

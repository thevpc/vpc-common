package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.util.JTypeUtils;

import java.util.*;

public abstract class AbstractJType implements JType {
    private JTypes types;
    private LinkedHashMap<String, JMethod[]> methodsByName = new LinkedHashMap<>();

    public AbstractJType(JTypes types) {
        this.types = types;
    }

    @Override
    public JTypeName typeName() {
        return types().parseName(name());
    }


    @Override
    public JTypes types() {
        return types;
    }

    public JTypesSPI types2() {
        return (JTypesSPI) types();
    }

    /**
     * simple name including declaring type
     * @return simple name including declaring type
     */
    @Override
    public String dname(){
        JType d = declaringType();
        if(d==null){
            return name();
        }else{
            return d.dname()+'.'+name();
        }
    }

    @Override
    public Object cast(Object o) {
        if (o == null) {
            return o;
        }
        JType y = types().typeOf(o);
        if (isAssignableFrom(y)) {
            return o;
        }
        throw new ClassCastException("Cannot cast " + y + " to " + this);
    }

    @Override
    public JType boxed() {
        return this;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean isInstance(Object instance) {
        if(instance==null){
            return !isPrimitive();
        }
        return isAssignableFrom(types().typeOf(instance));
    }

    @Override
    public boolean isAssignableFrom(JType other) {
        if (name().equals(other.name())) {
            return true;
        }
        if (isPrimitive() || other.isPrimitive()) {
            if(name().equals("void") || other.name().equals("void")){
                return false;
            }
        }
        if(rawType().name().equals(other.rawType().name())){
            if(isRawType() || other.isRawType()) {
                return true;
            }else if(this instanceof JParameterizedType && other instanceof JParameterizedType) {
                JParameterizedType t1 = (JParameterizedType) this;
                JParameterizedType t2 = (JParameterizedType) other;
                JType[] actualTypeArguments1 = t1.actualTypeArguments();
                JType[] actualTypeArguments2 = t2.actualTypeArguments();
                for (int i = 0; i < actualTypeArguments1.length; i++) {
                    JType s1 = actualTypeArguments1[i];
                    JType s2 = actualTypeArguments2[i];
                    if(!s1.isAssignableFrom(s2)){
                        return false;
                    }
                }
                return true;
            }else{
                throw new JFixMeLaterException();
            }
        }
        for (JType parent : other.parents()) {
            if (isAssignableFrom(parent)) {
                return true;
            }
        }
        if(JTypeUtils.isNullType(other)){
            if(isNullable()){
                return true;
            }
        }
        if(JTypeUtils.isObjectType(this)){
            return true;
        }
        return false;
    }

    @Override
    public JType toArray() {
        return toArray(1);
    }

    @Override
    public JType firstCommonSuperType(JType other) {
        return JTypeUtils.firstCommonSuperType(this, other, types());
    }

    @Override
    public JType toPrimitive() {
        return null;
    }

    @Override
    public JConstructor declaredConstructor(JType... parameterTypes) {
        return declaredConstructor(JSignature.of(name(), parameterTypes));
    }

    @Override
    public JConstructor[] publicConstructors() {
        List<JConstructor> m = new ArrayList<>();
        for (JConstructor value : declaredConstructors()) {
            if (value.isPublic()) {
                m.add(value);
            }
        }
        return m.toArray(new JConstructor[0]);
    }

    @Override
    public JField matchedField(String fieldName) {
        JField f = declaredFieldOrNull(fieldName);
        if (f != null) {
            return f;
        }
        JType s = getSuperType();
        if (s != null) {
            f = s.matchedField(fieldName);
            if (f != null) {
                return f;
            }
        }
        return null;
    }

    @Override
    public synchronized JField declaredField(String fieldName) {
        JField f = declaredFieldOrNull(fieldName);
        if (f == null) {
            throw new JEvalException("Field " + name() + "." + fieldName + " not found");
        }
        return f;
    }

    @Override
    public JField publicField(String name) {
        JField f = declaredFieldOrNull(name);
        if (f != null && f.isPublic()) {
            return f;
        }
        return null;
    }

    @Override
    public JMethod[] publicMethods() {
        List<JMethod> m = new ArrayList<>();
        for (JMethod value : declaredMethods()) {
            if (value.isPublic()) {
                m.add(value);
            }
        }
        return m.toArray(new JMethod[0]);
    }

    @Override
    public JMethod[] declaredMethods(String name) {
        JMethod[] jMethods = methodsByName.get(name);
        if (jMethods == null) {
            List<JMethod> n = new ArrayList<>();
            for (JMethod value : declaredMethods()) {
                if (value.name().equals(name)) {
                    n.add(value);
                }
            }
            methodsByName.put(name, jMethods = n.toArray(new JMethod[0]));
        }
        return jMethods;
    }

    @Override
    public JMethod declaredMethodOrNull(String sig) {
        return declaredMethodOrNull(JSignature.of(types(), sig));
    }

    @Override
    public JMethod[] declaredMethods(boolean includeParents) {
        if(!includeParents){
            return declaredMethods();
        }
        LinkedHashMap<JSignature, JMethod> all = new LinkedHashMap<>();
        for (JMethod jMethod : declaredMethods()) {
            JSignature sig = jMethod.signature();
            all.put(sig, jMethod);
        }
        if (includeParents) {
            for (JType parent : parents()) {
                JMethod[] jMethods = parent.declaredMethods(true);
                for (JMethod jMethod : jMethods) {
                    JSignature sig = jMethod.signature();
                    if (!all.containsKey(sig)) {
                        all.put(sig, jMethod);
                    }
                }
            }
        }
        return all.values().toArray(new JMethod[0]);
    }

    @Override
    public JMethod[] declaredMethods(String[] names, int callArgumentsCount, boolean includeParents) {
        Set<String> namesSet = new HashSet<>(Arrays.asList(names));
        LinkedHashMap<JSignature, JMethod> all = new LinkedHashMap<>();
        for (JMethod jMethod : declaredMethods()) {
            if (namesSet.contains(jMethod.name())) {
                JSignature sig = jMethod.signature();
                if (sig.acceptArgsCount(callArgumentsCount)) {
                    all.put(sig, jMethod);
                }
            }
        }
        if (includeParents) {
            for (JType parent : parents()) {
                JMethod[] jMethods = parent.declaredMethods(names, callArgumentsCount, true);
                for (JMethod jMethod : jMethods) {
                    JSignature sig = jMethod.signature();
                    if (!all.containsKey(sig)) {
                        all.put(sig, jMethod);
                    }
                }
            }
        }
        return all.values().toArray(new JMethod[0]);
    }



//    @Override
//    public JMethod findMethodMatchOrNull(JSignature signature, JContext context) {
//        JMethod[] possibleMethods = declaredMethods(new String[]{signature.name()}, signature.argsCount(), true);
//        return (JMethod) context.functions().resolveBestMatch(possibleMethods, null, signature.argTypes());
//    }

//    @Override
//    public JConstructor findConstructorMatchOrNull(JSignature signature, JContext context) {
//        JConstructor[] possibleMethods = declaredConstructors();
//        return (JConstructor) context.functions().resolveBestMatch(possibleMethods, null, signature.argTypes());
//    }

//    @Override
//    public JMethod findMethodMatch(JSignature signature, JContext context) {
//        JMethod a = findMethodMatchOrNull(signature,context);
//        if (a == null) {
//            throw new IllegalArgumentException("Method not found : " + name() + "." + signature);
//        }
//        return a;
//    }

//    @Override
//    public JConstructor findConstructorMatch(JSignature signature, JContext context) {
//        JConstructor a = findConstructorMatchOrNull(signature,context);
//        if (a == null) {
//            throw new IllegalArgumentException("Constructor not found : " + name() + "." + signature);
//        }
//        return a;
//    }


    @Override
    public JMethod declaredMethod(String sig) {
        return declaredMethod(JSignature.of(types(), sig));
    }

    @Override
    public JMethod declaredMethod(JSignature sig) {
        JMethod m = declaredMethodOrNull(sig);
        if (m == null) {
            throw new JEvalException("Method " + name() + "." + sig + " not found");
        }
        return m;
    }

    @Override
    public JConstructor declaredConstructorOrNull(String sig) {
        return declaredConstructorOrNull(JSignature.of(types(), sig));
    }

    @Override
    public JConstructor declaredConstructor(String sig) {
        return declaredConstructor(JSignature.of(types(), sig));
    }

    @Override
    public JConstructor declaredConstructor(JSignature sig) {
        JConstructor c = declaredConstructorOrNull(sig);
        if (c == null) {
            throw new JEvalException("Constructor " + sig + " not found");
        }
        return c;
    }

    @Override
    public JType[] parents() {
        List<JType> parents = new ArrayList<>();
        JType s = getSuperType();
        if (s != null) {
            parents.add(s);
        }
        for (JType i : interfaces()) {
            parents.add(i);
        }
        return parents.toArray(new JType[0]);
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractJType that = (AbstractJType) o;
        return Objects.equals(name(), that.name());
    }

    @Override
    public String toString() {
        if (isPrimitive()) {
            return name();
        }
        return "class " + name();
    }

    @Override
    public boolean isRawType(){
        return rawType()==this;
    }

    @Override
    public JField[] declaredFieldsWithParents() {
        LinkedHashSet<JField> fields=new LinkedHashSet<>();
        fields.addAll(Arrays.asList(declaredFields()));
        for (JType parent : parents()) {
            fields.addAll(Arrays.asList(parent.declaredFieldsWithParents()));
        }
        return  fields.toArray(new JField[0]);
    }

    @Override
    public boolean isVar() {
        return false;
    }


    @Override
    public JTypeVariable toVar() {
        throw new ClassCastException();
    }

    @Override
    public JConstructor defaultConstructor() {
        JConstructor d = defaultConstructorOrNull();
        if (d == null) {
            throw new JEvalException("Default Constructor not found for " + name());
        }
        return d;
    }

    @Override
    public JMethod declaredMethodOrNull(JSignature sig) {
        for (JMethod s : declaredMethods(sig.name())) {
            if(s.signature().equals(sig)){
                return s;
            }
        }
        return null;
    }

    @Override
    public JField declaredFieldOrNull(String fieldName) {
        for (JField jField : declaredFields()) {
            if(jField.name().equals(fieldName)){
                return jField;
            }
        }
        return null;
    }

    @Override
    public JConstructor declaredConstructorOrNull(JSignature sig) {
        for (JConstructor s : declaredConstructors()) {
            if(s.signature().equals(sig)){
                return s;
            }
        }
        return null;
    }



    @Override
    public JType replaceParameter(String name, JType param) {
//        JType[] jTypes = actualTypeArguments();
//        JType[] y = new JType[jTypes.length];
//        boolean modified = false;
//        for (int i = 0; i < y.length; i++) {
//            y[i] = jTypes[i].replaceParameter(name, param);
//            modified |= (!y[i].name().equals(jTypes[i].name()));
//        }
//        if (modified) {
//            return new JParameterizedTypeImpl(rawType(),y,types());
//        } else {
            return this;
//        }
    }

//    @Override
//    public JType[] actualTypeArguments() {
//        return new JType[0];
//    }


    @Override
    public JDeclaration declaration() {
        return declaringType();
    }

    @Override
    public JConstructor defaultConstructorOrNull() {
        JConstructor defaultConstructor = declaredConstructor(JSignature.of(name(), new JType[0]));
        if (defaultConstructor.isPublic()) {
            return defaultConstructor;
        }
        return null;
    }

    @Override
    public JType declaredInnerType(String name) {
        JType d = declaredInnerTypeOrNull(name);
        if (d == null) {
            throw new JEvalException("inner type not found : " + name()+"."+name);
        }
        return d;
    }

    @Override
    public JType declaredInnerTypeOrNull(String name) {
        for (JType jType : declaredInnerTypes()) {
            if(jType.rawType().name().equals(name)){
                return jType;
            }
        }
        return null;
    }
}

package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.types.DefaultTypeName;
import net.vpc.common.jeep.core.types.JTypeNameBounded;
import net.vpc.common.jeep.impl.JTypesSPI;

import java.util.*;

public class DefaultJTypes implements JTypes, JTypesSPI {
    private final ClassLoader hostClassLoader;
    private final JTypes parent;
    private final Map<String, JType> typesMap = new HashMap<>();
    private final Map<String, JType> aliases = new HashMap<>();
    private final List<JTypesLoader> loaders = new ArrayList<>();
    private final List<JTypesResolver> resolvers = new ArrayList<>();

    public DefaultJTypes() {
        this((JTypes) null,null);
    }

    public DefaultJTypes(JContext context, ClassLoader hostClassLoader) {
        this(
                context.parent()==null?null:context.parent().types(),
                hostClassLoader
        );
    }
    public DefaultJTypes(JTypes parent, ClassLoader hostClassLoader) {
        this.parent = parent;
        this.hostClassLoader = hostClassLoader != null ? hostClassLoader : Thread.currentThread().getContextClassLoader();
//        if (parent == null) {
            //register null type...
        JType nullType0 = createNullType0();
        if(nullType0!=null) {
            registerType(nullType0);
        }
//        }
    }

    @Override
    public ClassLoader hostClassLoader() {
        return hostClassLoader;
    }

    public JTypesResolver[] resolvers() {
        LinkedHashSet<JTypesResolver> all = new LinkedHashSet<>(resolvers);
        if (parent != null) {
            all.addAll(Arrays.asList(parent.resolvers()));
        }
        return all.toArray(new JTypesResolver[0]);
    }

    @Override
    public void addResolver(JTypesResolver r) {
        if (r != null) {
            resolvers.add(r);
        }
    }

    @Override
    public JTypesLoader[] loaders() {
        return loaders.toArray(new JTypesLoader[0]);
    }

    @Override
    public void addLoader(JTypesLoader loader) {
        loaders.add(loader);
    }

    @Override
    public void removeLoader(JTypesLoader loader) {
        loaders.remove(loader);
    }

    @Override
    public JTypeName parseName(String name) {
        return DefaultTypeName.of(name);
    }

    @Override
    public JType[] forName(String[] names) {
        JType[] jTypes = new JType[names.length];
        for (int i = 0; i < jTypes.length; i++) {
            jTypes[i] = forName(names[i]);
        }
        return jTypes;
    }

    @Override
    public JType forName(String name, JDeclaration enclosingDeclaration) {
        JType t = forNameOrNull(name, enclosingDeclaration);
        if (t == null) {
            throw new JParseException("type not found " + name);
        }
        return t;
    }

//    @Override
//    public JType forName(String name, int arrayDim) {
//        if (arrayDim < 0) {
//            throw new IllegalArgumentException("Negative Array Dimension");
//        }
//        JType n = forName(name);
//        if (arrayDim == 0) {
//            return n;
//        }
//        JType root = n;
//        if (n.isArray()) {
//            root = n.rootComponentType();
//            arrayDim += n.arrayDimension();
//        }
//        return ((AbstractJType) root).toArrayImpl(arrayDim);
//    }

    @Override
    public JType forName(String name) {
        JType t = forNameOrNull(name);
        if (t == null) {
            throw new JParseException("type not found " + name);
        }
        return t;
    }

    @Override
    public JType forNameOrNull(String name) {
        return forNameOrNull(name, null);
    }

    @Override
    public JType forNameOrNull(String name, JDeclaration enclosingDeclaration) {
        JTypeName tn = parseName(name);
        return forNameOrNull(tn, enclosingDeclaration);
    }

//    @Override
//    @Deprecated
//    public JType[] forName(Type[] clazz) {
//        return forName(clazz, null);
//    }

//    @Override
//    @Deprecated
//    public JType[] forName(Type[] names, JDeclaration declaration) {
//        JType[] jTypes = new JType[names.length];
//        for (int i = 0; i < jTypes.length; i++) {
//            jTypes[i] = forName(names[i], declaration);
//        }
//        return jTypes;
//    }

//    @Override
//    @Deprecated
//    public JType[] forNameOrVar(Type[] names, JDeclaration declaration) {
//        JType[] jTypes = new JType[names.length];
//        for (int i = 0; i < jTypes.length; i++) {
//            jTypes[i] = forNameOrVar(names[i], declaration);
//        }
//        return jTypes;
//    }

//    @Override
//    @Deprecated
//    public JType forName(Type ctype) {
//        return (JType) forNameOrVar(ctype, null);
//    }

//    @Override
//    @Deprecated
//    public JType forName(Type ctype, JDeclaration declaration) {
//        return (JType) forNameOrVar(ctype, declaration);
//    }

//    @Override
//    @Deprecated
//    public JType forNameOrVar(Type ctype, JDeclaration declaration) {
//        if (ctype instanceof Class) {
//            Class clazz = (Class) ctype;
//            if (clazz.isArray()) {
//                return forName(clazz.getComponentType(), declaration).toArray();
//            }
//            JType found = getRegisteredOrAlias(getCanonicalTypeName(ctype));
//            if (found != null) {
//                return found;
//            }
//            //each context should have its proper types repository...
//            //so wont inherit parent's
////            if (parent != null) {
////                found = parent.forName(ctype, declaration);
////                if (found != null) {
////                    return found;
////                }
////            }
//            found = new HostJRawType((Class) ctype, this);
//            registerType(found);
//            return found;
//        } else if (ctype instanceof ParameterizedType) {
//            ParameterizedType pt = (ParameterizedType) ctype;
//            Type rawType = pt.getRawType();
//            JType rootRaw = forNameOrVar(rawType, declaration);
//            return new JParameterizedTypeImpl(
//                    rootRaw,
//                    forNameOrVar(pt.getActualTypeArguments(), declaration),
//                    rootRaw.declaringType(),
//                    this
//            );
//        } else if (ctype instanceof TypeVariable) {
//            TypeVariable tv = (TypeVariable) ctype;
//            return new DefaultJTypeVariable(
//                    tv.getName(),
//                    new JType[0],
//                    forNameOrVar(tv.getBounds(),declaration),
//                    declaration,this
//            );
//        } else if (ctype instanceof WildcardType) {
//            WildcardType tv = (WildcardType) ctype;
//            return new DefaultJTypeVariable(
//                    "?",
//                    forNameOrVar(tv.getLowerBounds(),declaration),
//                    forNameOrVar(tv.getUpperBounds(),declaration),
//                    declaration,this
//            );
//        } else if (ctype instanceof GenericArrayType) {
//            Type c = ((GenericArrayType) ctype).getGenericComponentType();
//            return forNameOrVar(c,declaration).toArray();
//        } else {
//            throw new IllegalArgumentException("Unsupported");
//        }
//    }

    @Override
    public boolean isNull(Object object) {
        return object == null;
    }

    @Override
    public JType typeOf(Object object) {
        if (object == null) {
            return null;
        }
        return forName(object.getClass().getName());
    }

    @Override
    public String typenameOf(Object object) {
        if (object == null) {
            return null;
        }
        return typeOf(object).name();
    }

//    private String classCanonicalName(Class clazz){
//        if(clazz.isArray()){
//            return classCanonicalName(clazz.getComponentType())+"[]";
//        }
//        return clazz.getName();
//    }

    @Override
    public JType declareType(String fullName, boolean redefine) {
        if (fullName.endsWith("[]")) {
            throw new JParseException("Cannot declare arrays");
        }
        JType old = forNameOrNull(fullName);
        if (old != null) {
            if (redefine) {
                //old.dispose();
            } else {
                throw new JParseException("Type " + fullName + " Declared Twice");
            }
        }
        JType jt = createMutableType0(fullName);
        registerType(jt);
        return jt;
    }

    @Override
    public JType forName(JTypeName name) {
        if (name == null) {
            return null;
        }
        return forName(name.fullName());
    }

    @Override
    public JType forNameOrNull(JTypeName name) {
        if (name == null) {
            return null;
        }
        return forNameOrNull(name.fullName());
    }

    @Override
    public void addAlias(String name, JType type) {
        aliases.put(name, type);
    }

    public DefaultJTypes setResolvers(List<JTypesResolver> resolvers) {
        this.resolvers.clear();
        if(resolvers!=null){
            this.resolvers.addAll(resolvers);
        }
        return this;
    }


    @Override
    public JTypes parent() {
        return parent;
    }

    //    @Override
    public JType[] forNameOrNull(JTypeNameOrVariable[] tnov, JDeclaration enclosingDeclaration) {
        JType[] rr = new JType[tnov.length];
        for (int i = 0; i < rr.length; i++) {
            rr[i] = forNameOrNull(tnov[i], enclosingDeclaration);
        }
        return rr;
    }

    public JType forNameOrNull(JTypeNameOrVariable tnov, JDeclaration enclosingDeclaration) {
        if (tnov instanceof JTypeNameBounded) {
            JTypeNameBounded b = (JTypeNameBounded) tnov;
            return createVarType0(
                    b.name(),
                    forNameOrNull(b.getLowerBound(), enclosingDeclaration),
                    forNameOrNull(b.getUpperBound(), enclosingDeclaration),
                    enclosingDeclaration
            );
        } else {
            JTypeName tn = (JTypeName) tnov;
            String name = tn.fullName();
            JType found = getRegisteredOrAlias(tn.fullName());
            if (found != null) {
                return found;
            }
            if (tn.arrayDimension() > 0) {
                JType root = forName(tn.rootComponentType().fullName());
                return root.toArray(tn.arrayDimension());
            }
            found = getRegisteredOrAlias(tn.name());
            if (found != null) {
                JTypeNameOrVariable[] vars = tn.vars();
                if (vars.length > 0) {
                    return ((JRawType)found).parametrize(forNameOrNull(vars, found));
                }
                return found;
            }
            for (JTypesLoader loader : loaders()) {
                JType t = loader.loadType(name, this);
                if (t != null) {
                    return t;
                }
            }
            //each context should have its proper types repository...
            //so wont inherit parent's
            if (false/*parent != null*/) {
//                JType jType = parent.forNameOrNull(name);
//                if (jType != null) {
//                    return jType;
//                }
            } else {
                JType jType = createHostType0(tn.name());
                if(jType!=null){
                    JTypeNameOrVariable[] vars = tn.vars();
                    if (vars.length > 0) {
                        return ((JRawType)jType).parametrize(forNameOrNull(vars, jType));
                    }
                    return jType;
                }
            }
            //check if declaring class exists
            int endIndex = name.lastIndexOf('.');
            if (endIndex > 0) {
                String dec = name.substring(0, endIndex);
                String sn = name.substring(endIndex + 1);
                JType ptype = forNameOrNull(dec);
                if (ptype != null) {
                    JType[] dit = ptype.declaredInnerTypes();
                    for (JType cc : dit) {
                        if (cc.simpleName().equals(sn)) {
                            return cc;
                        }
                    }
                }
            }
            return null;
        }
    }



    @Override
    public JType getRegisteredOrAlias(String jt) {
        JType jType = aliases.get(jt);
        if (jType != null) {
            return jType;
        }
        return getRegistered(jt);
    }

    @Override
    public JType getRegistered(String jt) {
        return typesMap.get(jt);
    }

    @Override
    public void registerType(JType jt) {
        JTypeName jTypeName = jt.typeName();
        String fn0 = jt.name();
//        String implClassName = jt.getClass().getSimpleName();
//        while (implClassName.length() < 14) {
//            implClassName += " ";
//        }
        if (!typesMap.containsKey(fn0)) {
            typesMap.put(fn0, jt);
//            System.err.println(System.identityHashCode(this) + " : register type " + implClassName + " " + fn0);
        }
        String fn = jTypeName.fullName();
        if (!fn.equals(fn0)) {
            if (typesMap.containsKey(fn)) {
//                System.err.println(System.identityHashCode(this) + " : register (again) type " + implClassName + " " + fn);
            } else {
                typesMap.put(fn, jt);
//                System.err.println(System.identityHashCode(this) + " : register type " + implClassName + " " + fn);
            }
        }
    }


    @Override
    public JType createArrayType0(JType root, int dim){
        return new DefaultJTypeArray(root,dim);
    }
    @Override
    public JType createNullType0(){
        return new NullJType(this);
    }

    @Override
    public JType createHostType0(String name){
        return new JTypesHostHelper(this).forName0(name);
    }

    @Override
    public JType createMutableType0(String name){
        return new DefaultJType(name,this);
    }

    @Override
    public JType createVarType0(String name, JType[] lowerBounds, JType[] upperBounds, JDeclaration declaration){
        return new DefaultJTypeVariable(name,lowerBounds,upperBounds,declaration,this);
    }

    @Override
    public JParameterizedType createParameterizedType0(JType rootRaw, JType[] parameters, JType declaringType) {
        return new JParameterizedTypeImpl(rootRaw, parameters, declaringType, this);
    }

}

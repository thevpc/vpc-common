package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JStaticObject;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.impl.types.host.AbstractJType;
import net.vpc.common.jeep.util.JTypeUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class JParameterizedTypeImpl extends AbstractJType implements JParameterizedType{
    private JType rootRaw;
    private JType superType;
    private JType[] parameters;

    private LinkedHashMap<String, JField> _fields;
    private LinkedHashMap<JSignature, JMethod> _methods;
    private LinkedHashMap<JSignature, JConstructor> _constructors;
    private String name;
    private String sname;
    private JType[] _interfaces;
    private JType declaringType;
    private LinkedHashMap<String, JType> innerTypes;
    public JParameterizedTypeImpl(JType rootRaw, JType[] parameters, JType declaringType,JTypes types) {
        super(types);
        this.rootRaw=rootRaw;
        this.parameters=parameters;
        this.declaringType=declaringType;
        //should apply params
        StringBuilder pn=new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            if(i==0){
                pn.append("<");
            }else{
                pn.append(",");
            }
            pn.append(parameters[i].name());
            if(i==parameters.length-1){
                pn.append(">");
            }
        }
        if(declaringType==null) {
            this.name = rootRaw.name() + pn.toString();
        }else{
            this.name=declaringType.name()+"."+rootRaw.simpleName()+pn;
        }
        this.sname=rootRaw.simpleName()+pn;
        JType sType = rootRaw.getSuperType();
        this.superType= sType ==null?null:JTypeUtils.buildParentType(sType,this);
    }

    @Override
    public JType[] actualTypeArguments() {
        return parameters;
    }

    @Override
    public JStaticObject staticObject() {
        //shares static with root
        return rootRaw.staticObject();
    }

    @Override
    public String[] getExports() {
        return new String[0];
    }

    @Override
    public JType getSuperType() {
        return superType;
    }

    @Override
    public JType replaceParameter(String name, JType param) {
        JType[] jTypes = actualTypeArguments();
        JType[] y = new JType[jTypes.length];
        boolean modified = false;
        for (int i = 0; i < y.length; i++) {
            y[i] = jTypes[i].replaceParameter(name, param);
            modified |= (!y[i].name().equals(jTypes[i].name()));
        }
        if (modified) {
            return types2().createParameterizedType0(rawType(),y,this);
        } else {
            return this;
        }
    }

    @Override
    public String packageName() {
        return rawType().packageName();
    }

    @Override
    public JType declaringType() {
        return declaringType;
    }

    @Override
    public boolean isNullable() {
        return rawType().isNullable();
    }

    @Override
    public JType rawType() {
        return rootRaw;
    }

    @Override
    public Object defaultValue() {
        return rawType().defaultValue();
    }

    @Override
    public JTypeVariable[] typeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JDeclaration declaration() {
        return rawType().declaringType();
    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        if (parameters.length == 0) {
//            throw new IllegalArgumentException("Invalid");
//        }
//        JTypeVariable[] vars = typeParameters();
//        if (vars.length != parameters.length) {
//            throw new IllegalArgumentException("Invalid");
//        }
//        int dim = arrayDimension();
//        JType rootRaw = dim == 0 ? this : rootComponentType().rawType();
//        return DefaultJTypes.getRegisteredOrRegister(new JParameterizedTypeImpl(rootRaw, parameters,types()),types());
//    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String simpleName() {
        return sname;
    }

    @Override
    public JType[] interfaces() {
        if(_interfaces==null){
            _interfaces= JTypeUtils.buildParentType(rawType().interfaces(),this);
        }
        return _interfaces;
    }
    private synchronized Map<String, JType> _innerTypes() {
        if (innerTypes == null) {
            innerTypes = new LinkedHashMap<>();
            for (JType item : rootRaw.declaredInnerTypes()) {
                if(item.isStatic()){
                    innerTypes.put(item.simpleName(), item);
                }else {
                    JParameterizedType f = types2().createParameterizedType0(item,
                            new JType[0],
                            this);
                    innerTypes.put(f.simpleName(), f);
                }
            }
        }
        return innerTypes;
    }

    @Override
    public JMethod[] declaredMethods() {
        if(_methods==null){
            LinkedHashMap<JSignature, JMethod> _methods=new LinkedHashMap<>();
            for (JMethod jMethod : rawType().declaredMethods()) {
                JParameterizedMethodImpl m=new JParameterizedMethodImpl(jMethod,new JType[0], this);
                _methods.put(m.signature(),m);
            }
            this._methods=_methods;
        }
        return this._methods.values().toArray(new JMethod[0]);
    }

    @Override
    public JConstructor[] declaredConstructors() {
        if(_constructors==null){
            LinkedHashMap<JSignature, JConstructor> _constructors=new LinkedHashMap<>();
            for (JConstructor i : rawType().declaredConstructors()) {
                JParameterizedConstructorImpl m=new JParameterizedConstructorImpl(i,new JType[0], this);
                _constructors.put(m.signature(),m);
            }
            this._constructors=_constructors;
        }
        return this._constructors.values().toArray(new JConstructor[0]);
    }


    @Override
    public JField[] declaredFields() {
        if(_fields==null){
            LinkedHashMap<String, JField> _fields=new LinkedHashMap<>();
            for (JField i : rawType().declaredFields()) {
                JParameterizedFieldImpl m=new JParameterizedFieldImpl((JRawField) i,this);
                _fields.put(m.name(),m);
            }
            this._fields=_fields;
        }
        return this._fields.values().toArray(new JField[0]);
    }

    @Override
    public JType[] declaredInnerTypes() {
        return _innerTypes().values().toArray(new JType[0]);
    }

    @Override
    public JType declaredInnerTypeOrNull(String name) {
        return _innerTypes().get(name);
    }

    @Override
    public boolean isPublic() {
        return rootRaw.isPublic();
    }

    @Override
    public boolean isStatic() {
        return rootRaw.isStatic();
    }

    @Override
    public int modifiers() {
        return rootRaw.modifiers();
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count),types()
        );
    }

    @Override
    public boolean isInterface() {
        return rootRaw.isInterface();
    }

}

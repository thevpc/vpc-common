package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.functions.JSignature;
import net.vpc.common.jeep.impl.types.*;
import net.vpc.common.jeep.util.JTypeUtils;
import net.vpc.common.jeep.util.JeepPlatformUtils;

import java.lang.reflect.*;
import java.util.*;

public class HostJRawMethod extends AbstractJMethod implements JRawMethod {
    private Method method;
    private JType declaringType;
    private JType genericReturnType;
    private JType rawReturnType;
    private JSignature genericSig;
    private JSignature rawSig;
    private JTypeVariable[] typeParameters;
    private JType[] argTypes;
    private String[] argNames;

    public HostJRawMethod(Method method, JType declaringType) {
        this.method = method;
        this.declaringType = declaringType;
        if(!declaringType.isRawType()){
            throw new IllegalStateException("Error");
        }
        JeepPlatformUtils.setAccessibleWorkaround(method);
        for (JTypesResolver resolver : declaringType.types().resolvers()) {
            String s= resolver.resolveMethodSignature(method);
            if(s!=null){
                genericSig= JSignature.of(declaringType.types(),s);
                break;
            }
        }
        if(genericSig==null) {
            JSig sigAnn = method.getAnnotation(JSig.class);
            if(sigAnn!=null) {
                genericSig = JSignature.of(declaringType.types(), sigAnn.value());
            }
        }
        JType[] jeepParameterTypes = htypes().forName(method.getGenericParameterTypes(),this);
        List<String> argNamesList=new ArrayList<>();
        try {
            for (Parameter parameter : method.getParameters()) {
                String an = parameter.getName();
                if(an==null){
                    //just skip
                    throw new IllegalArgumentException();
                }
                argNamesList.add(an);
            }
        }catch (Exception ex){
            //ignore
        }
        this.argTypes=jeepParameterTypes;
        if(argNamesList.size()==argTypes.length){
            this.argNames=argNamesList.toArray(new String[0]);
        }else {
            this.argNames = new String[argTypes.length];
            for (int i = 0; i < this.argNames.length; i++) {
                this.argNames[i]="arg"+(i+1);
            }
        }
        genericReturnType=htypes().forName(method.getGenericReturnType());
        rawReturnType=htypes().forName(method.getReturnType());
        typeParameters=Arrays.stream(htypes().forName(method.getTypeParameters(),this)).toArray(JTypeVariable[]::new);
        if(genericSig!=null){
            //add some match checking
            if(genericSig.argTypes().length!=jeepParameterTypes.length){
                throw new IllegalArgumentException("Method parameters mismatch");
            }
            for (int i = 0; i < jeepParameterTypes.length; i++) {
                if(!jeepParameterTypes[i].typeName().name().equals(genericSig.argType(i).getName())){
                    throw new IllegalArgumentException("Method parameters mismatch");
                }
            }
            rawSig=JSignature.of(method.getName(), JTypeUtils.buildRawType(genericSig.argTypes(),this),method.isVarArgs());
        }else {
            genericSig = new JSignature(method.getName(), jeepParameterTypes, method.isVarArgs());
            rawSig=JSignature.of(method.getName(),JTypeUtils.buildRawType(genericSig.argTypes(),this),method.isVarArgs());//just for test
//            rawSig=new JSignature(method.getName(), declaringType.types().forName(method.getParameterTypes()), method.isVarArgs());
        }
    }

    @Override
    public JType[] getArgTypes() {
        return argTypes;
    }

    @Override
    public String[] getArgNames() {
        return argNames;
    }

    protected JTypesHostHelper htypes(){
        return new JTypesHostHelper(types());
    }

    protected JTypes types(){
        return declaringType.types();
    }

    @Override
    public JSignature getGenericSignature() {
        return genericSig;
    }



    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(method.getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(method.getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    @Override
    public JType getDeclaringType() {
        return declaringType;
    }

    @Override
    public JType getReturnType() {
        return rawReturnType;
    }

    @Override
    public JType getGenericReturnType() {
        return genericReturnType;
    }

    @Override
    public JSignature getSignature() {
        return rawSig;
    }

    @Override
    public Object invoke(JInvokeContext context) {
        try {
            JEvaluable[] rargs = context.getArguments();
            Object[] eargs = new Object[rargs.length];
            for (int i = 0; i < eargs.length; i++) {
                eargs[i]=rargs[i].evaluate(context);
            }
            return method.invoke(context.getInstance(),eargs);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(ex);
        } catch (InvocationTargetException e) {
            Throwable c = e.getCause();
            if(c instanceof RuntimeException){
                throw (RuntimeException)c;
            }
            throw new RuntimeException(c);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HostJRawMethod that = (HostJRawMethod) o;
        return Objects.equals(method, that.method);
    }


    @Override
    public JTypeVariable[] getTypeParameters() {
        return typeParameters;
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return new JParameterizedMethodImpl(
                this,parameters, getDeclaringType()
        );
    }

    @Override
    public int getModifiers() {
        return method.getModifiers();
    }

    @Override
    public boolean isDefault() {
        return method.isDefault();
    }

    @Override
    public String getSourceName() {
        return "<library>";
    }
}

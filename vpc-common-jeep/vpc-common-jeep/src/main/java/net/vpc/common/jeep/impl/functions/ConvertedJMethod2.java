package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.eval.JEvaluableValue;
import net.vpc.common.jeep.impl.types.AbstractJMethod;

import java.lang.reflect.Modifier;

public class ConvertedJMethod2 extends AbstractJMethod {
    private JArgumentConverter[] argConverters;
    private JConverter resultConverter;
    private JInvokable other;
    private JInstanceArgumentResolver instanceArgumentResolver;
    private JType[] argTypes;
    private JType returnType;
    private JSignature signature;
    //return type was Object.class, fix it!
    public ConvertedJMethod2(
            JInvokable other,
                               JArgumentConverter[] argConverters,
                                JType[] argTypes,
                                JInstanceArgumentResolver instanceArgumentResolver,
                               JConverter resultConverter) {
//        super(other.name(),
//                returnType,
//                convArgTypes(other.signature().argTypes(), argConverters)
//                ,false
//        );
        this.signature = JSignature.of(other.getName(),argTypes);
        this.other = other;
        this.argTypes = argTypes;
        this.returnType = resultConverter!=null?resultConverter.targetType().getType():other.getReturnType();
        this.argConverters = argConverters;
        this.resultConverter = resultConverter;
        this.instanceArgumentResolver = instanceArgumentResolver;
    }

    @Override
    public JSignature getSignature() {
        return signature;
    }

    @Override
    public JType getDeclaringType() {
        return null;
    }

    @Override
    public JType[] getArgTypes() {
        return argTypes;
    }

    @Override
    public String[] getArgNames() {
        return new String[0];
    }

    @Override
    public int getModifiers() {
        return Modifier.STATIC|Modifier.PUBLIC;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public JTypeVariable[] getTypeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JMethod parametrize(JType... parameters) {
        return this;
    }

    public JArgumentConverter[] getArgConverters() {
        return argConverters;
    }

    public JConverter getResultConverter() {
        return resultConverter;
    }

    public JInvokable getOther() {
        return other;
    }



    public static JType[] convArgTypes(JType[] argTypes, JArgumentConverter[] converters){
        JType[] newArgTypes=new JType[argTypes.length];
        for (int i = 0; i < newArgTypes.length; i++) {
            if(converters!=null && converters[i]!=null){
                newArgTypes[i]=converters[i].argumentType();
                if(newArgTypes[i]==null){
                    newArgTypes[i]=argTypes[i];
                }
            }else{
                newArgTypes[i]=argTypes[i];
            }
        }
        return newArgTypes;
    }

    public JInstanceArgumentResolver getInstanceArgumentResolver() {
        return instanceArgumentResolver;
    }

    @Override
    public JType getReturnType() {
        return returnType;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        int otherArgsCount = other.getSignature().argsCount();
        Object[] argumentValues=new Object[otherArgsCount];
        for (int i = 0; i < argumentValues.length; i++) {
            argumentValues[i]=icontext.evaluateArg(i);
        }
        JEvaluable[] convertedArgs=new JEvaluable[otherArgsCount];
        JType[] convertedTypes=new JType[otherArgsCount];
        for (int i = 0; i < convertedArgs.length; i++) {
            if(argConverters==null || argConverters[i]==null){
                convertedArgs[i] = new JEvaluableValue(
                        argumentValues[i],
                        other.getSignature().argType(i)
                        );
                convertedTypes[i] = other.getSignature().argType(i);
            }else {
                convertedArgs[i] = new JEvaluableValue(
                        argConverters[i].getArgument(i, argumentValues, icontext.getContext())
                        ,argConverters[i].argumentType()
                );
                convertedTypes[i] = argConverters[i].argumentType();
            }
        }
        JTypedValue instanceValue=icontext.getInstance();
        if(instanceArgumentResolver!=null){
            JTypedValue tv = instanceArgumentResolver.getInstance(
                    instanceValue==null?null:instanceValue.getValue()
                    , argumentValues);
            if(tv!=null){
                instanceValue=tv;
            }
        }
        Object v = other.invoke(
                icontext.builder()
                        .setInstance(instanceValue)
                        .setArguments(convertedArgs)
                        .build());
        if(resultConverter!=null){
            v=resultConverter.convert(v, icontext);
        }
        return v;
    }

    @Override
    public String getSourceName() {
        return other.getSourceName();
    }
}

package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JFunctionBase;
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
        this.signature = JSignature.of(other.name(),argTypes);
        this.other = other;
        this.argTypes = argTypes;
        this.returnType = resultConverter!=null?resultConverter.targetType().getType():other.returnType();
        this.argConverters = argConverters;
        this.resultConverter = resultConverter;
        this.instanceArgumentResolver = instanceArgumentResolver;
    }

    @Override
    public JSignature signature() {
        return signature;
    }

    @Override
    public JType declaringType() {
        return null;
    }

    @Override
    public JType[] argTypes() {
        return argTypes;
    }

    @Override
    public String[] argNames() {
        return new String[0];
    }

    @Override
    public int modifiers() {
        return Modifier.STATIC|Modifier.PUBLIC;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public JTypeVariable[] typeParameters() {
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

    @Override
    public JType returnType() {
        return resultConverter!=null?resultConverter.targetType().getType():other.returnType();
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

    public JType[] getArgTypes() {
        return argTypes;
    }

    public JType getReturnType() {
        return returnType;
    }

    public JSignature getSignature() {
        return signature;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        int otherArgsCount = other.signature().argsCount();
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
                        other.signature().argType(i)
                        );
                convertedTypes[i] = other.signature().argType(i);
            }else {
                convertedArgs[i] = new JEvaluableValue(
                        argConverters[i].getArgument(i, argumentValues, icontext.context())
                        ,argConverters[i].argumentType()
                );
                convertedTypes[i] = argConverters[i].argumentType();
            }
        }
        JTypedValue instanceValue=icontext.instance();
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
                        .instance(instanceValue)
                        .arguments(convertedArgs)
                        .build());
        if(resultConverter!=null){
            v=resultConverter.convert(v, icontext);
        }
        return v;
    }

}

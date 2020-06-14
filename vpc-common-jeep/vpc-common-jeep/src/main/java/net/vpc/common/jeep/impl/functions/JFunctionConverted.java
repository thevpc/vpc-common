package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.JFunctionBase;
import net.vpc.common.jeep.impl.eval.JEvaluableConverter;
import net.vpc.common.jeep.util.JTypeUtils;

public class JFunctionConverted extends JFunctionBase {
    private JConverter[] argConverters;
    private JConverter resultConverter;
    private JFunction other;
    //return type was Object.class, fix it!
    public JFunctionConverted(JFunction other,
                              JConverter[] argConverters,
                              JConverter resultConverter) {
        super(other.name(),
                convRetType(JTypeOrLambda.of(other.returnType()), resultConverter).getType(),
                JTypeUtils.typesOrError(
                        convArgTypes(JTypeUtils.typesOrLambdas(other.signature().argTypes()), argConverters)
                )
                ,false
        );
        this.other = other;
        this.argConverters = argConverters;
        this.resultConverter = resultConverter;
    }

    public JConverter[] getArgConverters() {
        return argConverters;
    }

    public JConverter getResultConverter() {
        return resultConverter;
    }

    public JFunction getOther() {
        return other;
    }

    @Override
    public JType returnType() {
        return resultConverter!=null?
                resultConverter.targetType().getType()
                :other.returnType();
    }

    private static JTypeOrLambda convRetType(JTypeOrLambda a, JConverter c){
        if(c==null){
            return a;
        }
        return c.targetType();
    }
    
    private static JTypeOrLambda[] convArgTypes(JTypeOrLambda[] argTypes, JConverter[] converters){
        JTypeOrLambda[] newArgTypes=new JTypeOrLambda[argTypes.length];
        for (int i = 0; i < newArgTypes.length; i++) {
            if(converters!=null && converters[i]!=null){
                newArgTypes[i]=converters[i].originalType();
                if(newArgTypes[i]==null){
                    newArgTypes[i]=argTypes[i];
                }
            }else{
                newArgTypes[i]=argTypes[i];
            }
        }
        return newArgTypes;
    }

    @Override
    public Object invoke(JInvokeContext icontext) {
        JEvaluable[] args = icontext.arguments();
        JEvaluable[] args2=new JEvaluable[args.length];
        JTypeOrLambda[] oldTypes= JTypeUtils.typesOrLambdas(icontext.argumentTypes());
        JTypeOrLambda[] types2=new JTypeOrLambda[args.length];
        for (int i = 0; i < args.length; i++) {
            if(argConverters!=null && argConverters[i]!=null){
                args2[i]=new JEvaluableConverter(argConverters[i],args[i]);
                types2[i]=argConverters[i].targetType();
            }else{
                args2[i]=args[i];
                types2[i]=oldTypes[i];
            }
        }
        Object v = other.invoke(
                icontext.builder()
                        .arguments(args2)
                        .build());
        if(resultConverter!=null){
            v=resultConverter.convert(v, icontext);
        }
        return v;
    }
}

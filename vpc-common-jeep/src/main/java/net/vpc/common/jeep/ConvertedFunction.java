package net.vpc.common.jeep;

import net.vpc.common.jeep.nodes.ExpressionNodeConverter;

public class ConvertedFunction extends FunctionBase{
    private ExpressionEvaluatorConverter[] argConverters;
    private ExpressionEvaluatorConverter resultConverter;
    private Function other;
    public ConvertedFunction(Function other, ExpressionEvaluatorConverter[] argConverters, ExpressionEvaluatorConverter resultConverter) {
        super(other.getName(),
                Object.class,
                convArgTypes(other.getArgTypes(), argConverters)
                ,false
        );
        this.other = other;
        this.argConverters = argConverters;
        this.resultConverter = resultConverter;
    }

    @Override
    public Class getResultType(ExpressionManager evaluator, ExpressionNode[] args) {
        return resultConverter!=null?resultConverter.getTargetType():other.getResultType( evaluator, args);
    }

    private static Class[] convArgTypes(Class[] argTypes, ExpressionEvaluatorConverter[] converters){
        Class[] newArgTypes=new Class[argTypes.length];
        for (int i = 0; i < newArgTypes.length; i++) {
            if(converters!=null && converters[i]!=null){
                newArgTypes[i]=converters[i].getOriginalType();
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
    public Object evaluate(ExpressionNode[] args, ExpressionEvaluator evaluator) {
        ExpressionNode[] args2=new ExpressionNode[args.length];
        for (int i = 0; i < args.length; i++) {
            if(argConverters!=null && argConverters[i]!=null){
                args2[i]=new ExpressionNodeConverter(args[i],argConverters[i]);
            }else{
                args2[i]=args[i];
            }
        }
        Object v = other.evaluate(args2, evaluator);
        if(resultConverter!=null){
            v=resultConverter.convert(v);
        }
        return v;
    }
}

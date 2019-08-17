package net.vpc.common.jeep;

class ConstFunction extends FunctionBase {
    private Object value;
    public ConstFunction(String name,Object argValue) {
        super(name, argValue==null?Object.class:argValue.getClass(),new Class[0],false);
        this.value=argValue;
    }

    @Override
    public Object evaluate(ExpressionNode[] args, ExpressionEvaluator evaluator) {
        return value;
    }
    

}

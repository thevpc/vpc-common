package net.vpc.common.strings.deprecated;

//package net.vpc.common.strings;
//
///**
// * Created by vpc on 4/16/17.
// */
//public class ExprConst implements Expr {
//    private String userName;
//    private ExpressionParser.ConstDef def;
//
//    public ExprConst(String userName, ExpressionParser.ConstDef def) {
//        this.userName = userName;
//        this.def = def;
//    }
//
//    public String getName() {
//        return getDef().getName();
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public ExpressionParser.ConstDef getDef() {
//        return def;
//    }
//
//    public Object getValue() {
//        return def.getDefaultValue();
//    }
//
//    @Override
//    public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
//        return evaluator.evalConst(context, this);
//    }
//
//    @Override
//    public String toString() {
//        return "const(" +
//                "" + userName +
//                ", " + def.getDefaultValue() +
//                ')';
//    }
//}

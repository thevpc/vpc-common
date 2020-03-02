package net.vpc.common.jeep;

import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vpc
 */
public class ExprParserTest {

    public static void createEvaluator() {
        DefaultExpressionManager e = new DefaultExpressionManager();
        e.importType(PlatformHelper.class);
        e.addResolver(new AbstractExpressionEvaluatorResolver() {
            @Override
            public Variable resolveVariable(String name, ExpressionManager context) {
                return context.declareVar(name, String.class, name);
            }
        });
        e.declareUnaryOperators("+", "-");
        e.declareBinaryOperators("+", "-", "*");
        e.declareBinaryOperators(",");
        e.declareOperator(new FunctionBase(";", String.class, new Class[]{Object.class, Object.class}) {
            @Override
            public Object eval(ExpressionNode[] operands, ExpressionEvaluator evaluator) {
                StringBuilder sb = new StringBuilder();
                Object op1 = operands[0].evaluate(evaluator);
                Object op2 = operands[1].evaluate(evaluator);
                sb.append("(");
                sb.append(op1);
                sb.append(";");
                sb.append(op2);
                sb.append(")");
                return sb.toString();
            }
        });
        e.declareListOperator("", String.class, Object.class, new FunctionHandler() {
            @Override
            public Object evaluate(FunctionEvaluationContext context) {
                StringBuilder sb = new StringBuilder();
                ExpressionNode[] arguments = context.getArguments();
                for (int i = 0; i < arguments.length; i++) {
                    ExpressionNode argument = arguments[i];
                    Object oneOperand = argument.evaluate(context.getExpressionEvaluator());
                    if (i > 0) {
                        sb.append("_");
                    }
                    sb.append(oneOperand);
                }
                return sb.toString();
            }
        });

//        System.out.println(e.evaluate("2*3 + 4 * 5"));
        Object tt = e.createEvaluator("(a b t) ; c d ; e f").evaluate();
        String ss = tt.toString();
        org.junit.Assert.assertEquals("((a_b_t;c_d);e_f)", ss);
        System.out.println(tt);
    }

    @Test
    public void testExpr() {
        createEvaluator();
    }
}

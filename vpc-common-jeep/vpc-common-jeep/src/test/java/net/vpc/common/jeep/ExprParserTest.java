package net.vpc.common.jeep;

import net.vpc.common.jeep.core.DefaultJeep;
import net.vpc.common.jeep.core.imports.PlatformHelperImports;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        DefaultJeep e = new DefaultJeep();
        e.resolvers().importType(PlatformHelperImports.class);
        e.resolvers().addResolver(new JResolver() {
            @Override
            public JVar resolveVariable(String name, JContext context) {
                return context.vars().declareVar(name, String.class, name);
            }
        });
        e.operators().declarePrefixUnaryOperators("+", "-");
        e.operators().declareBinaryOperators("+", "-", "*");
        e.operators().declareBinaryOperators(",");
        e.operators().declareBinaryOperators(";");
//        e.operators().declareOperator(new JFunctionBase(";", e.types().forName(String.class), new JType[]{
//                e.types().forName(Object.class), e.types().forName(Object.class)}) {
//            @Override
//            public Object eval(JNode[] operands, JContext context) {
//                StringBuilder sb = new StringBuilder();
//                Object op1 = operands[0].evaluate(context);
//                Object op2 = operands[1].evaluate(context);
//                sb.append("(");
//                sb.append(op1);
//                sb.append(";");
//                sb.append(op2);
//                sb.append(")");
//                return sb.toString();
//            }
//        });
        e.operators().declareListOperator("", e.types().forName(String.class.getName()), e.types().forName(Object.class.getName()), new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                StringBuilder sb = new StringBuilder();
                JEvaluable[] arguments = context.arguments();
                for (int i = 0; i < arguments.length; i++) {
                    JEvaluable argument = arguments[i];
                    Object oneOperand = context.evaluate(argument);
                    if (i > 0) {
                        sb.append("_");
                    }
                    sb.append(oneOperand);
                }
                return sb.toString();
            }
        });
        e.operators().declareListOperator("{", e.types().forName(String.class.getName()), e.types().forName(Object.class.getName()), new JInvoke() {
            @Override
            public Object invoke(JInvokeContext context) {
                StringBuilder sb = new StringBuilder();
                JEvaluable[] arguments = context.arguments();
                for (int i = 0; i < arguments.length; i++) {
                    JEvaluable argument = arguments[i];
                    Object oneOperand = context.evaluate(argument);
                    if (i > 0) {
                        sb.append("_");
                    }
                    sb.append(oneOperand);
                }
                return sb.toString();
            }
        });

//        System.out.println(e.evaluate("2*3 + 4 * 5"));
        JContext eval = e.newContext();
        Object tt = eval.evaluate("(a b t) ; c d ; e f");
        String ss = tt.toString();
        Assertions.assertEquals("((a_b_t;c_d);e_f)", ss);
        System.out.println(tt);

        tt = eval.evaluate("{(a b t) ; c d ; e f}");
        ss = tt.toString();
    }

    @Test
    public void testExpr() {
        createEvaluator();
    }
}

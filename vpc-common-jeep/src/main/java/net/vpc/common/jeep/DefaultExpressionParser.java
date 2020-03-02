/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import net.vpc.common.jeep.nodes.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
public class DefaultExpressionParser {
    private ExpressionStreamTokenizer tokenizer;
    private ExpressionManager evaluator;

    public DefaultExpressionParser(ExpressionStreamTokenizer tokenizer, ExpressionManager evaluator) {
        this.tokenizer = tokenizer;
        this.evaluator = evaluator;
    }

    public DefaultExpressionParser(String expression, ExpressionManager evaluator) {
        this(new ExpressionStreamTokenizer(new StringReader(expression), evaluator.getTokenizerConfig()), evaluator);
    }

    public ExpressionStreamTokenizer getTokenizer() {
        return tokenizer;
    }

    //    public ExpressionNode parse(String expression, ExpressionManager definition) {
//        try {
//            return parse(createTokenizer(expression, definition), definition);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }

    private ExpressionNodeLiteral createLiteral(ExpressionNodeLiteral literal) {
        Object literal2 = literal.getValue();
        Object literal3;
        for (ExpressionEvaluatorResolver r : evaluator.getResolvers()) {
            literal3 = r.implicitConvertLiteral(literal2, evaluator);
            if (literal3 != null) {
                literal2 = literal3;
            }
        }
        return new ExpressionNodeLiteral(literal2, literal2 == null ? literal.getExprType(evaluator) : literal2.getClass());
    }

    public ExpressionStreamTokenizer.Token readWithComments() {
        ExpressionStreamTokenizer.Token token = null;
        while (true) {
            try {
                token = tokenizer.nextToken();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (token.ttype == ExpressionStreamTokenizer.TT_BLOCK_COMMENTS || token.ttype == ExpressionStreamTokenizer.TT_LINE_COMMENTS) {
                //ignore
            } else {
                break;
            }
        }
        return token;
    }

    public static ExpressionNode[] toArray(ExpressionNode node) {
        if (node == null) {
            return new ExpressionNode[0];
        }
        if (node instanceof ExpressionNodeFunctionCall) {
            ExpressionNodeFunctionCall op = (ExpressionNodeFunctionCall) node;
            if (op.isUnary("")) {
                ExpressionNode[] a = op.getOperands();
                if (a.length == 1 && a[0] instanceof ExpressionNodeArray) {
                    List<ExpressionNode> ok = new ArrayList<ExpressionNode>();
                    for (ExpressionNode value : ((ExpressionNodeArray) a[0]).getValues()) {
                        ok.addAll(Arrays.asList(toArray(value)));
                    }
                    return ok.toArray(new ExpressionNode[0]);
                }
                return a;
            }
        }
        return new ExpressionNode[]{node};
    }

    private ExpressionNode readL0() throws IOException {
        ExpressionStreamTokenizer.Token token = readWithComments();
        if (token.isEOF()) {
            return null;
        }
        if (evaluator.isUnaryOperator(token.image)) {
            ExpressionNode o = readL1(Long.MAX_VALUE, false);
            if (o == null) {
                throw new MissingTokenException(token.toString());
            }
            ExpressionNode[] args = {o};
            return createFunctionCall(token, args);
        }
        switch (token.ttype) {
            case ExpressionStreamTokenizer.TT_WORD: {
                ExpressionStreamTokenizer.Token peek = tokenizer.peek();
                if (peek.ttype == '(') {
                    //this is a function
                    readOp('(', "(");
                    ExpressionNode i = readL1(-1, true);
                    readOp(')', ")");
                    ExpressionNode[] args = null;
                    if (i instanceof ExpressionNodeFunctionCall) {
                        ExpressionNodeFunctionCall ii = (ExpressionNodeFunctionCall) i;
                        if (evaluator.isListSeparator(ii.getName())) {
                            args = ii.getOperands();
                        } else {
                            args = new ExpressionNode[]{i};
                        }
                    } else {
                        args = new ExpressionNode[]{i};
                    }
                    return createFunctionCall(token, args);
                } else if (Character.isDigit(token.image.charAt(0))) {
                    String varName = token.image;

                    //getContext().getVariableValue(token.image)
                    return createSuffixedNumber(varName);
                } else {
                    return createVariableName(token.image);
                }
            }
            case '(': {
                ExpressionNode o = readL1(-1, true);
                if (o == null) {
                    throw new MissingTokenException(token.toString());
                }
                readOp(')', ")");
                return o;
            }
            case ExpressionStreamTokenizer.TT_STRING: {
                return new ExpressionNodeLiteral(token.sval, String.class);
            }
            case ExpressionStreamTokenizer.TT_NUMBER_FLOAT: {
                //should i test if int ?
                if (((float) token.fval) == token.fval) {
                    return createLiteral(new ExpressionNodeLiteral((float) token.fval, Float.TYPE));
                } else {
                    return createLiteral(new ExpressionNodeLiteral(token.fval, Double.TYPE));
                }
            }
            case ExpressionStreamTokenizer.TT_NUMBER_INT: {
                //should i test if int ?
                if (((int) token.ival) == token.ival) {
                    //this is an int
                    return createLiteral(new ExpressionNodeLiteral((int) token.ival, Integer.TYPE));
                } else {
                    return createLiteral(new ExpressionNodeLiteral(token.ival, Long.TYPE));
                }
            }
            case ExpressionStreamTokenizer.TT_NUMBER_COMPLEX: {
                return createLiteral(new ExpressionNodeLiteral(token.cval, ExprDoubleComplex.class));
            }
            default: {
                tokenizer.pushBack(token);
                throw new UnexpectedTokenException(token.toString());
            }
        }
    }


    protected ExpressionNodeFunctionCall createFunctionCall(ExpressionStreamTokenizer.Token token, ExpressionNode[] args) {
        return new ExpressionNodeFunctionCall(token.image, args);
    }

    protected ExpressionNodeVariableName createVariableName(String varName) {
        return new ExpressionNodeVariableName(varName, Object.class);
    }

    protected ExpressionNode createSuffixedNumber(String varName) {
        return new ExpressionNodeVariableName(varName, Object.class);
    }

    private ExpressionNode readL1(long opPrecedence, boolean openPar) throws IOException {
        ExpressionNode o1 = readL0();
        if (o1 == null) {
            return null;
        }
        while (true) {
            ExpressionStreamTokenizer.Token token = readWithComments();
            if (token.isEOF()) {
                if (openPar) {
                    throw new MissingTokenException("Missing )");
                }
                return o1;
            }
            if (token.ttype == ')') {
                if (!openPar) {
//                    System.out.print("");//throw new IllegalArgumentException("Why");
                }
                tokenizer.pushBack(token);
                return o1;
            }
            if (token.ttype == '[') {
                tokenizer.pushBack(token);
                ExpressionNode[] p = readUplet('[', ']', ',');
                ExpressionNode p0;
                if (p.length != 0) {
                    p0 = new ExpressionNodeArray("[", p);
                } else {
                    p0 = p[0];
                }
                o1 = new ExpressionNodeFunctionCall("[", new ExpressionNode[]{o1, p0});
            } else if (evaluator.isBinaryOperator(token.image)) {
                long binaryOpPrecedence = evaluator.getOperatorPrecedence(token.image);
                if (binaryOpPrecedence > opPrecedence) {
                    // a binary ?
                    ExpressionNode o2 = readL1(binaryOpPrecedence, openPar);
                    o1 = createOpNode(token.image, o1, o2);
                } else {
                    tokenizer.pushBack(token);
                    return o1;
                }
//            } else if (definition.isListSeparator(token.image)) {
//                tokenizer.pushBack(token);
//                return o1;
            } else if (evaluator.isListSeparator(token.image)) {
                ExpressionNode o2 = readL1(opPrecedence, openPar);
                if (o2 instanceof ExpressionNodeFunctionCall && (((ExpressionNodeFunctionCall) o2).getName()).equals(token.image)) {
                    ExpressionNodeFunctionCall o21 = (ExpressionNodeFunctionCall) o2;
                    List<ExpressionNode> aa = new ArrayList<>();
                    aa.add(o1);
                    aa.addAll(Arrays.asList(o21.getArgs()));
                    o1 = createFunctionCall(token, aa.toArray(new ExpressionNode[0]));
                } else {
                    o1 = createFunctionCall(token, new ExpressionNode[]{o1, o2});
                }
            } else {
                //inject implicit op
                long binaryOpPrecedence = evaluator.getOperatorPrecedence("");
                if (binaryOpPrecedence > opPrecedence) {
                    tokenizer.pushBack(token);
                    ExpressionNode o2 = readL1(binaryOpPrecedence, openPar);
                    o1 = createOpNode("", o1, o2);
                } else {
                    tokenizer.pushBack(token);
                    return o1;
                }
            }
        }
    }

    private ExpressionNode createOpNode(String op, ExpressionNode o1, ExpressionNode o2) {
        // a binary ?
        if (o1 instanceof ExpressionNodeFunctionCall) {
            ExpressionNodeFunctionCall o10 = (ExpressionNodeFunctionCall) o1;
            if (o10.getName().equals(op)) {
                Function ff = evaluator.findFunction(o10.getName());
                if (ff != null && ff.isVarArgs()) {
                    if (o2 instanceof ExpressionNodeFunctionCall && ((ExpressionNodeFunctionCall) o2).getName().equals(op)) {
                        return new ExpressionNodeFunctionCall(op,
                                (ExpressionNode[]) JeepUtils.joinArraysAsType(ExpressionNode.class, new Object[]{
                                        ((ExpressionNodeFunctionCall) o1).getArgs(), ((ExpressionNodeFunctionCall) o2).getArgs()
                                })
                        );
                    } else {
                        return new ExpressionNodeFunctionCall(op,
                                (ExpressionNode[]) JeepUtils.joinArraysAsType(ExpressionNode.class,
                                        new Object[]{((ExpressionNodeFunctionCall) o1).getArgs(),
                                                new ExpressionNode[]{o2}})
                        );
                    }
                }
            }
        }
        return new ExpressionNodeFunctionCall(op, new ExpressionNode[]{o1, o2});
    }


    //    private static Class resolveType(Object o) {
//        if (o == null) {
//            throw new IllegalArgumentException("unsupported null type");
//        }
//        if (o instanceof ExprInvocation) {
//            Class returnType = ((ExprInvocation) o).getReturnType();
//            return returnType;
//        }
//        Class<?> aClass = o.getClass();
//        return aClass;
//    }

    public ExpressionNode parse() throws IOException {
        return readL1(-1, false);
    }

    private ExpressionStreamTokenizer.Token readOp(int ttype, String sval) throws IOException {
        ExpressionStreamTokenizer.Token token = readWithComments();
        if (token.ttype == ttype && token.image.equals(sval)) {
            return token;
        }
        throw new IllegalArgumentException("Expected " + sval);
    }

    private ExpressionNode[] readUplet(char start, char end, char sep) throws IOException {

        List<ExpressionNode> all = new ArrayList<>();
        readOp(start, String.valueOf(start));
        ExpressionStreamTokenizer.Token peek = tokenizer.peek();
        if (peek.ttype == end) {
            readOp(end, String.valueOf(end));
        } else {
            all.add(readL1(-1, true));
            while (true) {
                peek = tokenizer.peek();
                if (peek.ttype == end) {
                    readOp(end, String.valueOf(end));
                    break;
                } else {
                    readOp(sep, String.valueOf(sep));
                    all.add(readL1(-1, true));
                }
            }
        }
        return all.toArray(new ExpressionNode[all.size()]);

    }

}

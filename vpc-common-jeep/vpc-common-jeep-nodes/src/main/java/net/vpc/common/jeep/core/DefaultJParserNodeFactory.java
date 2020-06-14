package net.vpc.common.jeep.core;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.nodes.*;
import net.vpc.common.jeep.util.JeepUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultJParserNodeFactory implements JParserNodeFactory<JNode> {
    private JContext context;
    private JCompilationUnit compilationUnit;

    public DefaultJParserNodeFactory(JCompilationUnit compilationUnit, JContext context) {
        this.context = context;
        this.compilationUnit = compilationUnit;
    }

    public JCompilationUnit compilationUnit() {
        return compilationUnit;
    }

    public JContext context() {
        return context;
    }

    public JCompilerLog log() {
        return context.log();
    }

    @Override
    public JNode createLiteralNode(Object literal, JToken token) {
        if(literal==null){
            return new JNodeLiteral(null);
        }
        Object literal2 = literal;
        Object literal3;
        for (JResolver r : context.resolvers().getResolvers()) {
            literal3 = r.implicitConvertLiteral(literal2, context);
            if (literal3 != null) {
                literal2 = literal3;
            }
        }
        JNodeLiteral lnode = new JNodeLiteral(literal2);
        lnode.setStartToken(token);
        return lnode;
    }

    @Override
    public JNode createParsNode(List<JNode> o, JToken startToken, List<JToken> separators, JToken endToken) {
        return new JNodePars(o.toArray(new JNode[0]));
    }


    @Override
    public JNode createBracesNode(List<JNode> o, JToken startToken, JToken endToken) {
        return new JNodeBraces(o.toArray(new JNode[0]));
    }


    @Override
    public JNode createPrefixUnaryOperatorNode(JToken op, JNode arg2, JToken startToken, JToken endToken) {
        return new JNodePrefixUnaryOpCall(op.image, arg2);
    }

    @Override
    public JNode createPostfixBracketsNode(JNode o, JNode indices, JToken startToken, JToken endToken) {
        return new JNodePostfixBrackets(o, (JNodeBrackets) indices);
    }
    @Override
    public JNode createPrefixBracketsNode(JNode o, JNode indices, JToken startToken, JToken endToken) {
        return new JNodePrefixBrackets(o, (JNodeBrackets) indices);
    }

    @Override
    public JNode createPostfixBracesNode(JNode o, JNode indices, JToken startToken, JToken endToken) {
        return new JNodePostfixBraces(o,  indices);
    }
    @Override
    public JNode createPrefixBracesNode(JNode o, JNode indices, JToken startToken, JToken endToken) {
        return new JNodePrefixBraces(o,  indices);
    }

    @Override
    public JNode createPostfixParenthesisNode(JNode o, JNode indices, JToken startToken, JToken endToken) {
        return new JNodePostfixParenthesis(o, (JNodePars) indices);
    }
    @Override
    public JNode createPrefixParenthesisNode(JNode o, JNode indices, JToken endParsToken, JToken startToken, JToken endToken) {
        return new JNodePrefixParenthesis(o, (JNodePars) indices);
    }

    @Override
    public JNode createBracketsNode(List<JNode> o, JToken startToken, List<JToken> separators, JToken endToken) {
        return new JNodeBrackets(o.toArray(new JNode[0]));
    }

//    public JNode createBracketsNodeOld(JNode base, JNodeArray array) {
//        return new JNodeArrayCall(base,array);
//    }

    @Override
    public JNode createPostfixUnaryOperatorNode(JToken name, JNode arg1, JNode postNode, JToken startToken, JToken endToken) {
        return new JNodePostfixUnaryOpCall(startToken.image, arg1);
    }

    @Override
    public JNode createListOperatorNode(JToken token, List<JNode> args, JToken startToken, JToken endToken) {
        if(args.size()==2) {
            JNode o1 = args.get(0);
            JNode o2 = args.get(1);
            if (o2 instanceof JNodeFunctionCall && (((JNodeFunctionCall) o2).getName()).equals(token.image)) {
                JNodeFunctionCall o21 = (JNodeFunctionCall) o2;
                List<JNode> aa = new ArrayList<>();
                aa.add(o1);
                aa.addAll(Arrays.asList(o21.getArgs()));
                return new JNodeFunctionCall(token.image, aa.toArray(new JNode[0]));
            } else {
                return new JNodeFunctionCall(token.image, args.toArray(new JNode[0]));
            }
        }else {
            return new JNodeFunctionCall(token.image, args.toArray(new JNode[0]));
        }
    }

//    @Override
//    public JNode createFunctionCallNode(String name, JNode[] args, JToken startToken) {
//        return new JNodeFunctionCall(name, args);
//    }

    @Override
    public JNode createVarNameNode(JToken token) {
        return new JNodeVarName(token.image);
    }


    @Override
    public JNode createBinaryOperatorNode(JToken op, JNode o1, JNode o2, JToken startToken, JToken endToken) {
        return new JNodeInfixBinaryOperatorCall(op.image, o1, o2);
    }

    @Override
    public JNode createImplicitOperatorNode(JNode o1, JNode o2, JToken startToken, JToken endToken) {
        return createOpNode("", o1, o2);
    }


    protected JNode createOpNode(String op, JNode o1, JNode o2) {
//        if (o2 == null) {
//            o2=new JNodeLiteral(null);
//        }
        // a binary ?
        if (o1 instanceof JNodeFunctionCall) {
            JNodeFunctionCall o10 = (JNodeFunctionCall) o1;
            if (o10.getName().equals(op)) {
                JFunction ff = context.functions().findFunctionMatchOrNull(o10.getName());
                if (ff != null && ff.signature().isVarArgs()) {
                    if (o2 instanceof JNodeFunctionCall && ((JNodeFunctionCall) o2).getName().equals(op)) {
                        return new JNodeFunctionCall(op,
                                (JNode[]) JeepUtils.joinArraysAsType(JNode.class, new Object[]{
                                        ((JNodeFunctionCall) o1).getArgs(), ((JNodeFunctionCall) o2).getArgs()
                                })
                        );
                    } else {
                        return new JNodeFunctionCall(op,
                                (JNode[]) JeepUtils.joinArraysAsType(JNode.class,
                                        new Object[]{((JNodeFunctionCall) o1).getArgs(),
                                                new JNode[]{o2}})
                        );
                    }
                }
            }
        }
        return new JNodeFunctionCall(op, new JNode[]{o1, o2});
    }
}

package net.vpc.common.jeep;

import java.util.List;

public interface JParserNodeFactory<T extends JNode> {
    T createLiteralNode(Object literal, JToken token);

    T createParsNode(List<T> o, JToken startToken, List<JToken> separators, JToken endToken);

    T createBracesNode(List<T> o, JToken startToken, JToken endToken);

    T createPrefixUnaryOperatorNode(JToken op, T arg2, JToken startToken, JToken endToken);

    T createPostfixBracketsNode(T o, T indices, JToken startToken, JToken endToken);

    T createPrefixBracketsNode(T indices, T o, JToken startToken, JToken endToken);

    T createPostfixParenthesisNode(T o, T indices, JToken startToken, JToken endToken);

    T createPrefixParenthesisNode(T indices, T o, JToken endParsToken, JToken startToken, JToken endToken);

    T createPostfixBracesNode(T o, T indices, JToken startToken, JToken endToken);

    T createPrefixBracesNode(T indices, T o, JToken startToken, JToken endToken);


    T createBracketsNode(List<T> o, JToken startToken, List<JToken> separators, JToken endToken);

    T createPostfixUnaryOperatorNode(JToken name, T arg1, T postNode, JToken startToken, JToken endToken);

    T createListOperatorNode(JToken token, List<T> args, JToken startToken, JToken endToken);

//    T createFunctionCallNode(String name, List<T> args, JToken startToken);

    T createVarNameNode(JToken token);

    T createBinaryOperatorNode(JToken op, T o1, T o2, JToken startToken, JToken endToken);

    T createImplicitOperatorNode(T o1, T o2, JToken startToken, JToken endToken);
}

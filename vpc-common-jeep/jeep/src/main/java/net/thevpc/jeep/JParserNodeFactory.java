package net.thevpc.jeep;

import net.thevpc.jeep.core.nodes.JNodeTokens;

import java.util.List;

public interface JParserNodeFactory<T extends JNode> {
    T createIdentifierNode(String name, JNodeTokens nodeTokens);

    T createLiteralNode(Object literal, JNodeTokens nodeTokens);

    T createParsNode(List<T> children, JNodeTokens nodeTokens);

    T createBracesNode(List<T> children, JNodeTokens nodeTokens);

    T createPrefixUnaryOperatorNode(JToken op, T child, JNodeTokens nodeTokens);

    T createPostfixBracketsNode(T leftBaseChild, T rightIndexChildren, JNodeTokens nodeTokens);

    T createPrefixBracketsNode(T leftIndexChildren, T rightBaseChild, JNodeTokens nodeTokens);

    T createPostfixParenthesisNode(T leftBaseChild, T rightIndexChild, JNodeTokens nodeTokens);

    T createPrefixParenthesisNode(T leftIndexChild, T rightBaseChild, JNodeTokens nodeTokens);

    T createPostfixBracesNode(T leftBaseChild, T rightIndexChild, JNodeTokens nodeTokens);

    T createPrefixBracesNode(T leftIndexChild, T rightBaseChild, JNodeTokens nodeTokens);


    T createBracketsNode(List<T> children, JNodeTokens nodeTokens);

    T createPostfixUnaryOperatorNode(JToken name, T argumentChild, JNodeTokens nodeTokens);

    T createListOperatorNode(JToken token, List<T> argumentChildren, JNodeTokens nodeTokens);

//    T createFunctionCallNode(String name, List<T> args, JToken startToken);


    T createBinaryOperatorNode(JToken op, T leftChild, T rightChild, JNodeTokens nodeTokens);

    T createImplicitOperatorNode(T leftChild, T rightChild, JNodeTokens nodeTokens);

    T createAnnotatedNode(T node, T annotations, JNodeTokens nodeTokens);
}

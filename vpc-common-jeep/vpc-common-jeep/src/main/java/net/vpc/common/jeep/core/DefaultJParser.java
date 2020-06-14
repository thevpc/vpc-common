/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.tokens.JTokenId;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author vpc
 */
public class DefaultJParser<T extends JNode> implements JParser<T> {

    private JTokenizer tokenizer;
    private JContext context;
    private JParserNodeFactory<T> nodeFactory;
    private JCompilationUnit compilationUnit;
    private Stack<T> declarationContexts = new Stack<>();
    private JExpressionOptions defaultExpressionOptions;

    public DefaultJParser(JTokenizer tokenizer, JCompilationUnit compilationUnit, JContext context) {
        this.context = context;
        this.compilationUnit = compilationUnit;
//        if (context.tokens().config().isSkipComments()
//                && context.tokens().config().isSkipSpaces()) {
//            //ok
            this.tokenizer = tokenizer;
//        } else {
//            this.tokenizer = new NoCommentsNoSpacesJTokenizer(tokenizer);
//        }
//        nodeFactory = new DefaultJParserNodeFactory(compilationUnit, context);
    }

    public JExpressionOptions getDefaultExpressionOptions() {
        return defaultExpressionOptions;
    }

    public DefaultJParser<T> setDefaultExpressionOptions(JExpressionOptions defaultExpressionOptions) {
        this.defaultExpressionOptions = defaultExpressionOptions;
        return this;
    }

    public JParserNodeFactory<T> getNodeFactory() {
        return nodeFactory;
    }

    public void setNodeFactory(JParserNodeFactory<T> nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    @Override
    public JCompilationUnit compilationUnit() {
        return compilationUnit;
    }

    public T parse() {
        return parseExpression();
    }

    public JCompilerLog log() {
        return context.log();
    }

    public JContext context() {
        return context;
    }

    public JTokenizer tokenizer() {
        return tokenizer;
    }

//    protected T parseFunctionCall(String name, JToken startToken) {
//        //this is a function
//        List<T> args = parseParsList("function/method call", "function argument", () -> parseExpression());
//        T nn = getNodeFactory().createFunctionCallNode(name, args.toArray(new T[0]), startToken);
//        nn.setStartToken(startToken);
//        return nn;
//    }

    protected String readLongWord() {
        JToken i = peek();
        if (i.isIdentifier()) {
            StringBuilder sb = new StringBuilder();
            sb.append(i.sval);
            return sb.toString();
        }
        return null;
    }

    protected T parseExpressionPars() {
        return parseParenthesis("parenthesis");
    }

    protected T parseExpressionUnary(int opPrecedence, JExpressionOptions options) {
        JToken token = next();
        if (token.isEOF()) {
            return null;
        }
        ParseExpressionUnaryContext ucontext = new ParseExpressionUnaryContext();

        JExpressionUnaryOptions unary = options == null ? null : options.unary;
        ucontext.unary = unary;

        T middle = null;
        if ((unary == null || unary.excludedPrefixUnaryOperators == null
                || !unary.excludedPrefixUnaryOperators.contains(token.image))
                && context().operators().isPrefixUnaryOperator(token.image)) {
            JToken t = token.copy();
            T o = parseExpressionBinary(Integer.MAX_VALUE, options);
            if (o == null) {
                //this was an expression not a prefix
                log().error("X001", null, "expected expression", peek());
            } else {
                return getNodeFactory().createPrefixUnaryOperatorNode(t, o, t, o.endToken());
            }
        } else if ((unary == null || !unary.excludedPrefixParenthesis)
                && token.isImage("(")) {
            pushBack(token);
            T n = parsePrefixParsNode(options);
            if (n != null) {
                return n;
            }
        } else if ((unary == null || !unary.excludedPrefixBrackets)
                && token.isImage("[")) {
            JToken t = token.copy();
            pushBack(token);
            T o1 = parseBrackets();
            T arg = parseExpressionBinary(Integer.MAX_VALUE, options);
            if (arg == null) {
                middle = o1;
            } else {
                return getNodeFactory().createPrefixBracketsNode(o1, arg, t, arg.endToken());
            }
        } else if ((unary == null || !unary.excludedPrefixBraces)
                && token.isImage("{")) {
            JToken t = token.copy();
            pushBack(token);
            T o1 = parseBraces();
            T o = parseExpressionBinary(Integer.MAX_VALUE, options);
            if (o == null) {
                middle = o1;
            } else {
                return getNodeFactory().createPrefixBracesNode(o, o1, t, o.endToken());
            }
        } else if (token.isImage(")") || token.isImage("]") || token.isImage("}")) {
            pushBack(token);
            return null;
        } else {
            pushBack(token);
        }

        //Now middle!
        if (middle == null) {
            token = peek();
            if (token.isImage("(")) {
                middle = parseExpressionPars();
            } else if (token.isImage("[")) {
                middle = parseBrackets();
            } else if (token.isImage("{")) {
                middle = parseBraces();
            } else {
                middle = parseExpressionTerminal(options);
            }
            if (middle == null) {
                return null;
            }
        }
        while (true) {
            T r = parseExpressionUnarySuffix(opPrecedence, middle, options, ucontext);
            if (r == null) {
                break;
            } else {
                middle = r;
                if (ucontext.doBreak) {
                    break;
                }
            }
        }
        return middle;
    }

    protected T parseExpressionUnarySuffix(int opPrecedence, T middle, JExpressionOptions options, ParseExpressionUnaryContext ucontext) {
        JExpressionUnaryOptions unary = ucontext.unary;
        JToken token = next();
        if ((unary == null || unary.excludedPostfixUnaryOperators == null
                || !unary.excludedPostfixUnaryOperators.contains(token.image))
                && context().operators().isPostfixUnaryOperator(token.image)) {
            int binaryOpPrecedence = context().operators().getOperatorPrecedence(JOperator.infix(token.image));
            if (binaryOpPrecedence > opPrecedence) {
                ucontext.doBreak = true;
                return getNodeFactory().createPostfixUnaryOperatorNode(token, middle, null, middle.endToken(), token);
            } else {
                pushBack(token);
                return null;
            }
        } else {
            if ((unary == null || !unary.excludedPostfixParenthesis)
                    && token.isImage("(")) {
                JToken copy = token.copy();
                pushBack(token);
                return middle = parsePostfixParsNode(middle, copy);
            } else if ((unary == null || !unary.excludedPostfixBrackets)
                    && token.isImage("[")) {
                JToken copy = token.copy();
                pushBack(token);
                return middle = parsePostfixBracketsNode(middle, copy);
            } else if ((unary == null || !unary.excludedPostfixBraces)
                    && token.isImage("{")) {
                JToken copy = token.copy();
                pushBack(token);
                return middle = parsePostfixBracesNode(middle, copy);
            } else {
                pushBack(token);
                return null;
            }
        }
    }

    public T parsePrefixParsNodePars() {
        return parseParenthesis("cast");
    }

    public T parsePrefixParsNode(JExpressionOptions options) {
        JToken t = peek().copy();
        if (!t.isImage("(")) {
            return null;
        }
        try (JTokenizerSnapshot snapshot = tokenizer().snapshot()) {
            T o1 = parsePrefixParsNodePars();
            if (o1 != null && o1.childrenNodes().size() == 1) {
                T arg = parseExpressionBinary(Integer.MAX_VALUE, options);
                if (arg != null) {
                    return getNodeFactory().createPrefixParenthesisNode(o1, arg, o1.endToken(), t, arg.endToken());
                }
            }
            snapshot.rollback();
        }
        return null;
    }

    public T parsePostfixParsNodePars() {
        return parseParenthesis("function/method call");
    }

    public T parsePostfixParsNode(T middle, JToken copy) {
        T p = parsePostfixParsNodePars();
        return getNodeFactory().createPostfixParenthesisNode(middle, p, middle.startToken(), p.endToken());
    }

    public T parsePostfixBracketsNode(T middle, JToken copy) {
        T p = parseBrackets();
        return getNodeFactory().createPostfixBracketsNode(middle, p, middle.startToken(), p.endToken());
    }

    public T parsePostfixBracesNode(T middle, JToken copy) {
        T p = parseBraces();
        return getNodeFactory().createPostfixBracesNode(middle, p, middle.startToken(), p.endToken());
    }

    protected T parseBrackets() {
        JToken startToken = peek().copy();
        JListWithSeparators<T> elements = parseGroupedList("brackets", "brackets element", this::parseExpression, "[", ",", "]",null);
        return getNodeFactory().createBracketsNode(elements.getItems(), elements.getStartToken(), elements.getSeparatorTokens(), elements.getEndToken());
    }

    public T parseBraces() {
        JToken startToken = peek().copy();
        JListWithSeparators<T> elements = parseGroupedList("braces", "braces element", this::parseExpression, "{", ",", "}",null);
        return getNodeFactory().createBracesNode(elements.getItems(), elements.getStartToken(), elements.getEndToken());
    }

    protected T parseAndBuildExpressionBinary(JToken op, T o1, int opPrecedence, JExpressionOptions options) {
        T o2 = parseExpressionBinary(opPrecedence, options);
        if (o2 == null) {
            if (peek().isEOF()) {
                log().error("X002", null, "missing right operand for " + op.image, peek());
            } else {
                log().error("X003", null, "invalid right operand for " + op.image, peek());
            }
        }
        JToken s = o1.startToken();
        JToken e = o2 != null ? o2.endToken() : op;
        return getNodeFactory().createBinaryOperatorNode(op, o1, o2, s, e);
    }

    protected T parseExpressionBinary(int opPrecedence, JExpressionOptions options) {
        T o1 = parseExpressionUnary(opPrecedence, options);
        if (o1 == null) {
            return null;
        }
        JExpressionBinaryOptions binary = options == null ? null : options.binary;
        while (true) {
            JToken token = next();
            if (token.isEOF()) {
                return o1;
            }
            if (token.isImage(")")) {
                pushBack(token);
                return o1;
            }
            if (token.isImage("]")) {
                pushBack(token);
                return o1;
            }
            if (token.isImage("}")) {
                pushBack(token);
                return o1;
            }
//            if (token.ttype == '[') {
//                JToken s = o1 != null && o1.token() != null ? o1.token() : token;
//                T jNode = parseExpression();
//                JToken closing = next();
//                if (closing.ttype != ']') {
//                    //pushBack(token);
//                    log().error("X056", "expected ']'", closing);
//                }
//                o1 = getNodeFactory().createBracketsPostfixNode(o1, jNode, s);
//            } else if (token.ttype == '(') {
//                JToken s = o1 != null && o1.token() != null ? o1.token() : token;
//                T jNode = parseExpression();
//                JToken closing = next();
//                if (closing.ttype != ')') {
//                    //pushBack(token);
//                    log().error("X057", "expected ')'", closing);
//                }
//                o1 = getNodeFactory().createParsPostfixNode(o1, jNode, s);
//            } else if (context.operators().isPostfixUnaryOperator(token.image)) {
//                long binaryOpPrecedence = context.operators().getOperatorPrecedence(token.image);
//                if (binaryOpPrecedence > opPrecedence) {
//                    // a binary ?
//                    o1 = getNodeFactory().createSuffixUnaryOperatorNode(o1, token);
//                } else {
//                    pushBack(token);
//                    return o1;
//                }
////            } else if (definition.isListSeparator(token.image)) {
////                tokenizer.pushBack(token);
////                return o1;
            /*} else */
            if ((binary == null || binary.excludedBinaryOperators == null || !binary.excludedBinaryOperators.contains(token.image))
                    && context.operators().isBinaryOperator(token.image)) {
                int binaryOpPrecedence = context.operators().getOperatorPrecedence(JOperator.infix(token.image));
                if (binaryOpPrecedence > opPrecedence) {
                    o1 = parseAndBuildExpressionBinary(token, o1, binaryOpPrecedence, options);
                } else {
                    pushBack(token);
                    return o1;
                }
            } else if ((binary == null || !binary.excludedListOperator)
                    && context.operators().isListSeparator(token.image)) {
                o1 = parseAndBuildListOpNodeElement(o1, opPrecedence, token, options);
            } else if ((binary == null || !binary.excludedImplicitOperator)
                    && isSupportedImplicitOperator()) {
                //inject implicit op
                int binaryOpPrecedence = context.operators().getOperatorPrecedence(JOperator.infix(""));
                if (binaryOpPrecedence > opPrecedence) {
                    pushBack(token);
                    JToken s = o1 != null && o1.startToken() != null ? o1.startToken() : token;
                    T o2 = parseExpressionBinary(binaryOpPrecedence, options);
                    if (o2 == null) {
                        log().error("S004", null, "missing second operand for implicit operator", s);
                    }
                    o1 = getNodeFactory().createImplicitOperatorNode(o1, o2, s, o2 == null ? s : o2.endToken());
                } else {
                    pushBack(token);
                    return o1;
                }
            } else {
                pushBack(token);
                return o1;
            }
        }
    }

    protected T parseAndBuildListOpNodeElement(T o1, int opPrecedence, JToken token, JExpressionOptions options) {
        token = token.copy();
        T o2 = parseExpressionBinary(opPrecedence, options);
        JToken s = o1 != null && o1.startToken() != null ? o1.startToken() : token;
        JToken e = o1 != null && o1.endToken() != null ? o1.endToken() : token;
        o1 = getNodeFactory().createListOperatorNode(token, Arrays.asList(o1, o2), s, e);
        return o1;
    }

    protected boolean isSupportedImplicitOperator() {
        //TODO how to check
        return false;
    }

    public T parseExpression() {
        return parseExpressionBinary(-1, defaultExpressionOptions);
    }

    protected <T1 extends T> JListWithSeparators<T1> parseParsList(String groupName, String elementName, Supplier<T1> elementParser) {
        return parseGroupedList(groupName, elementName, elementParser, "(", ",", ")",null);
    }

    protected <T1 extends T> JListWithSeparators<T1> parseBracketsList(String groupName, String elementName, Supplier<T1> elementParser) {
        return parseGroupedList(groupName, elementName, elementParser, "[", ",", "]",null);
    }

//    protected JToken requireOp(int ttype, String sval) {
//        JToken token = next();
//        if (token.ttype == ttype && token.image.equals(sval)) {
//            return token;
//        }
//        pushBack(token);
//        log().error("X043", "Expected " + sval, token);
//        JToken t2 = token.copy();
//        t2.ttype = ttype;
//        t2.sval = sval;
//        t2.sttype = "<<Error>>";
//        return t2;
//    }

//    protected T[] readUplet(char start, char end, char sep) {
//
//        List<T> all = new ArrayList<>();
//        requireOp(start, String.valueOf(start));
//        JToken peek = peek();
//        if (peek.ttype == end) {
//            requireOp(end, String.valueOf(end));
//        } else {
//            all.add(parseExpression());
//            while (true) {
//                peek = peek();
//                if (peek.ttype == end) {
//                    requireOp(end, String.valueOf(end));
//                    break;
//                } else {
//                    requireOp(sep, String.valueOf(sep));
//                    all.add(parseExpression());
//                }
//            }
//        }
//        return all.toArray(new T[0]);
//    }

    protected T parseParenthesis(String name) {
        JToken startToken = peek();
        if (name == null) {
            name = "parenthesis";
        }
        JListWithSeparators<T> expression = parseGroupedList(name, "expression", () -> parseExpression(), "(", ",", ")",null);
        if (expression == null) {
            return null;
        }
        return getNodeFactory().createParsNode(expression.getItems(), expression.getStartToken(), expression.getSeparatorTokens(), expression.getEndToken());
    }

    /**
     *
     * @param groupName
     * @param elementName
     * @param elementParser
     * @param start
     * @param separator
     * @param end
     * @param silencedMessages when not null, errors are tracked into this list rather than into log()
     * @param <T1>
     * @return
     */
    protected <T1 extends T> JListWithSeparators<T1> parseGroupedList(String groupName, String elementName, Supplier<T1> elementParser, String start, String separator, String end,List<JCompilerMessage> silencedMessages) {
        if (start != null && start.length() == 0) {
            start = null;
        }
        if (end != null && end.length() == 0) {
            end = null;
        }
        JToken startToken = null;
        JToken endToken = null;
        List<JToken> separators = new ArrayList<>();
        if (start != null) {
            JToken n = next();
            startToken = n.copy();
            endToken = startToken;
            if (!n.image.equals(start)) {
                pushBack(n);
                return null;
            }
        }
        List<T1> list = new ArrayList<>();
        if (separator != null && separator.length() > 0) {
            boolean wasComma = true;
            while (true) {
                JToken n1 = next();
                if (n1.isEOF()) {
                    if (end != null) {
                        if(silencedMessages!=null){
                            silencedMessages.add(JCompilerMessage.error("X005", groupName, "expected '" + end + "'", n1));
                        }else {
                            log().error("X005", groupName,  "expected '" + end + "'", n1);
                        }
                    }
                    break;
                } else if (n1.image.equals(separator)) {
                    if (wasComma) {
                        if(silencedMessages!=null) {
                            silencedMessages.add(JCompilerMessage.error("X005", groupName,  "expected " + elementName, n1));
                        }else {
                            log().error("X005", groupName, "expected " + elementName, n1);
                        }
                    } else {
                        wasComma = true;
                        endToken = n1.copy();
                    }
                    separators.add(n1);
                    //read next
                } else if (end != null && n1.image.equals(end)) {
                    endToken = n1.copy();
                    break;
                } else {
                    if (!wasComma) {
                        if (end == null) {
                            pushBack(n1);
                            //cannot read more without error
                            break;
                        }
                        if(silencedMessages!=null) {
                            silencedMessages.add(JCompilerMessage.error("X005", groupName, "expected '" + separator + "'", n1));
                        }else {
                            log().error("X005", groupName,  "expected '" + separator + "'", n1);
                        }
                    }
                    wasComma = false;
                    pushBack(n1);
                    T1 t = null;
                    boolean errorReported = false;
                    while (true) {
                        t = elementParser.get();
                        if (t != null) {
                            break;
                        }
//                        t = elementParser.get();
                        JToken toSkip = next();
                        if (!errorReported) {
                            if(silencedMessages!=null) {
                                silencedMessages.add(JCompilerMessage.error("X005", groupName, "expected " + elementName, toSkip));
                            }else {
                                log().error("X005", groupName,  "expected " + elementName, toSkip);
                            }
                            errorReported = true;
                        }
                        if (toSkip.image.equals(separator) || (end != null && toSkip.image.equals(end))) {
                            pushBack(toSkip);
                            break;
                        } else if (toSkip.isEOF()) {
                            break;
                        }
                    }
                    if (t != null) {
                        endToken = t.endToken();
                        list.add(t);
                    }

                }
            }
        } else {
            while (true) {
                JToken n1 = next();
                if (n1.isEOF()) {
                    if (end != null) {
                        if(silencedMessages!=null) {
                            silencedMessages.add(JCompilerMessage.error("X005", groupName, "expected '" + end + "'", n1));
                        }else {
                            log().error("X005", groupName, "expected '" + end + "'", n1);
                        }
                    }
                    break;
                } else if (end != null && n1.image.equals(end)) {
                    endToken = n1.copy();
                    break;
                } else {
                    pushBack(n1);
                    T1 t = null;
                    while (true) {
                        t = elementParser.get();
                        if (t != null) {
                            break;
                        }
                        t = elementParser.get();
                        JToken toSkip = next();
                        if(silencedMessages!=null) {
                            silencedMessages.add(JCompilerMessage.error("X005", groupName,  "expected " + elementName, toSkip));
                        }else {
                            log().error("X005", groupName,"expected " + elementName, toSkip);
                        }
                        if (toSkip.isEOF()) {
                            break;
                        }
                    }
                    if (t != null) {
                        list.add(t);
                    }

                }
            }
        }
        return new DefaultJListWithSeparators<>(list, startToken, separators, endToken);
    }

    public T parseExpressionTerminal(JExpressionOptions options) {
        JToken token = next();
        if (token.isEOF()) {
            return null;
        }
        switch (token.def.ttype) {
            case JTokenType.TT_KEYWORD: {
                switch (token.image) {
                    case "null":
                        return getNodeFactory().createLiteralNode(null, token);
                    case "true":
                        return getNodeFactory().createLiteralNode(true, token);
                    case "false":
                        return getNodeFactory().createLiteralNode(false, token);
                }
                pushBack(token);
                return null;
            }
            case JTokenType.TT_IDENTIFIER: {
                return getNodeFactory().createVarNameNode(token);
            }
            case JTokenType.TT_STRING: {
                return getNodeFactory().createLiteralNode(token.sval, token);
            }
            case JTokenType.TT_NUMBER: {
                switch (token.def.id){
                    case JTokenId.NUMBER_INFINITY:{
                        return getNodeFactory().createLiteralNode(Double.POSITIVE_INFINITY, token);
                    }
                    case JTokenId.NUMBER_FLOAT:{
                        //should i test if int ?
                        switch (token.image.charAt(token.image.length() - 1)) {
                            case 'f':
                            case 'F': {
                                return getNodeFactory().createLiteralNode(Float.parseFloat(token.image), token);
                            }
                        }
                        return getNodeFactory().createLiteralNode(Double.parseDouble(token.image), token);
                    }
                    case JTokenId.NUMBER_INT:{
                        switch (token.image.charAt(token.image.length() - 1)) {
                            case 'l':
                            case 'L': {
                                T i = getNodeFactory().createLiteralNode(Long.parseLong(token.image), token);
                            }
                        }
                        return getNodeFactory().createLiteralNode(Integer.parseInt(token.image), token);
                    }
                    default:{
                        throw new JShouldNeverHappenException();
                    }
                }
            }
            default: {
                pushBack(token);
                return null;
            }
        }
    }

    ////////////////////////////////////////////////////////////
    protected void skipUntil(Predicate<JToken> t) {
        tokenizer().skipUntil(t);
    }

    protected void skipWhile(Predicate<JToken> t) {
        tokenizer().skipWhile(t);
    }

    public void skip() {
        tokenizer().skip();
    }

    public void skip(int count) {
        tokenizer().skip(count);
    }

    public void pushBackAll(Collection<JToken> t) {
        tokenizer.pushBackAll(t);
    }

    private JToken[] prepare(JToken[] t) {
        for (int i = 0; i < t.length; i++) {
            prepare(t[i]);
        }
        return t;
    }

    private JToken prepare(JToken t) {
        if (t != null) {
            if (t.compilationUnit == null) {
                t.compilationUnit = compilationUnit();
            }
        }
        return t;
    }

    public void pushBack(JToken t) {
        tokenizer.pushBack(t);
    }

    public JToken[] peek(int n) {
        return prepare(tokenizer.peek(n));
    }

    public JToken peek() {
        return prepare(tokenizer.peek());
    }

    public JToken next() {
        return prepare(tokenizer.next());
    }

    protected void pushDeclarationContext(T node) {
        declarationContexts.push(node);
    }

    protected T popDeclarationContext() {
        return declarationContexts.pop();
    }

    protected Stack<T> getDeclarationContexts() {
        return declarationContexts;
    }

    protected static class ParseExpressionUnaryContext {
        protected JExpressionUnaryOptions unary;
        protected boolean doBreak;
    }

}

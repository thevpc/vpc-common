package net.vpc.common.strings;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;

/**
 * Created by vpc on 4/16/17.
 */
public class ExpressionParser {

    private Map<String, ConstDef> consts = new HashMap<>();
    private Map<String, OperatorDef> operators = new HashMap<>();
    private Map<String, String> operatorAliases = new HashMap<>();
    private Map<String, String> constAliases = new HashMap<>();
    private boolean caseSensitive = true;
    private boolean parseFunctions = true;
    private String functionArgumentSeparator = ",";

    //    private Map<String, FunctionDef> functions = new HashMap<>();
    private String functionParamSeparator = ",";

    public static void main(String[] args) {
        // ((((IA3.1)+(IA3.1))+(II))+(II))+(IA)
        ExpressionParser p = new ExpressionParser();
        p.declareBinaryOperator("+",1);
        Expr e = p.parse("((((IA3.1)+(IA3.1))+(II))+(II))+(IA)");
//        Expr e = p.parse("(IA)+(IA)");
        System.out.println(e);
    }

    public ExprContext createContext() {
        return new DefaultExprContext();
    }

    public <T> T evaluate(Expr expression, ExpressionEvaluator evaluator) {
        return (T) expression.eval(createContext(), evaluator);
    }

    public <T> T evaluate(String expression, ExpressionEvaluator evaluator) {
        Expr parse = parse(expression);
        return evaluate(parse, evaluator);
    }

    public Expr parse(String expression) {
        return parse(new StreamTokenizerExt(new StringReader(expression)));
    }

    public String uniformize(String v) {
        if (v == null) {
            v = "";
        }
        if (!caseSensitive) {
            return v.toLowerCase();
        }
        return v;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public String checkNameForDeclaration(String name) {
        String name0 = uniformize(name);
        if (name0.length() == 0) {
            throw new IllegalArgumentException("Invalid empty name");
        }
        if (consts.containsKey(name0)) {
            throw new IllegalArgumentException("Already declared const " + name);
        }
        if (operators.containsKey(name0)) {
            throw new IllegalArgumentException("Already declared operator " + name);
        }
        if (operatorAliases.containsKey(name0)) {
            throw new IllegalArgumentException("Already declared operator alias " + name);
        }
        if (constAliases.containsKey(name0)) {
            throw new IllegalArgumentException("Already declared const alias " + name);
        }
//        if (functions.containsKey(name0)) {
//            throw new IllegalArgumentException("Already declared function " + name);
//        }
        return name0;
    }

    public void declareConst(String name, Object value, String... aliases) {
        String name0 = checkNameForDeclaration(name);
        consts.put(name, new ConstDef(name, value));
        for (String alias : aliases) {
            addConstAlias(alias, name);
        }
    }

    public void addOperatorAlias(String aliasName, String name) {
        aliasName = checkNameForDeclaration(aliasName);
        if (!operators.containsKey(name)) {
            throw new IllegalArgumentException("ExprOperator not found " + name);
        }
        operatorAliases.put(aliasName, name);
    }

    public void addConstAlias(String aliasName, String name) {
        aliasName = checkNameForDeclaration(aliasName);
        if (!consts.containsKey(name)) {
            throw new IllegalArgumentException("ExprConst not found " + name);
        }
        constAliases.put(aliasName, name);
    }

    public void declareBinaryOperator(String name, int precedence, String... aliases) {
        name = checkNameForDeclaration(name);
        operators.put(name, new BinaryOperatorDef(name, precedence));
        for (String alias : aliases) {
            addOperatorAlias(alias, name);
        }
    }

    public OperatorDef findOperator(String nameOrAlias) {
        nameOrAlias = uniformize(nameOrAlias);
        String newName = operatorAliases.get(nameOrAlias);
        if (newName != null) {
            return findOperator(newName);
        }
        return operators.get(nameOrAlias);
    }

    public ConstDef findConst(String nameOrAlias) {
        nameOrAlias = uniformize(nameOrAlias);
        String newName = constAliases.get(nameOrAlias);
        if (newName != null) {
            return findConst(newName);
        }
        return consts.get(nameOrAlias);
    }

    public Expr parse(StreamTokenizerExt tokenizer) {
        ExpressionEvaluatorHelper expressionEvaluatorHelper = new ExpressionEvaluatorHelper();
        return expressionEvaluatorHelper.parse(tokenizer);
    }

    private FunctionDef resolveFunction(String name) {
        return new FunctionDef(name);
    }

    public String getFunctionArgumentSeparator() {
        return functionArgumentSeparator;
    }

    public ExpressionParser setFunctionArgumentSeparator(String functionArgumentSeparator) {
        this.functionArgumentSeparator = functionArgumentSeparator;
        return this;
    }

    public boolean isParseFunctions() {
        return parseFunctions;
    }

    public ExpressionParser setParseFunctions(boolean parseFunctions) {
        this.parseFunctions = parseFunctions;
        return this;
    }

    public static class FunctionDef {
        private String name;

        public FunctionDef(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "FunctionDef{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    private static class OperatorDefPar extends OperatorNode implements Expr {
        public OperatorDefPar(String name) {
            super(name, name, Integer.MAX_VALUE);
        }

        @Override
        public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
            throw new IllegalArgumentException("Not Supported");
        }
    }

    private static class OperatorDefClosePar extends OperatorNode implements Expr {
        public OperatorDefClosePar(String name) {
            super(name, name, Integer.MAX_VALUE);
        }

        @Override
        public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
            throw new IllegalArgumentException("Not Supported");
        }
    }

    public static class OperatorDefFunction extends OperatorNode {
        private FunctionDef def;

        public OperatorDefFunction(String name, FunctionDef def) {
            super(name, name, Integer.MAX_VALUE);
            this.def = def;
        }

        public FunctionDef getDef() {
            return def;
        }
    }

    private static class OperatorNode {
        private String userName;
        private OperatorDef operator;

        public OperatorNode(String userName, String name, int precedence) {
            this(userName, new OperatorDef(name, precedence));
        }

        public OperatorNode(String userName, OperatorDef operator) {
            this.userName = userName;
            this.operator = operator;
        }

        public String getUserName() {
            return userName;
        }

        public OperatorDef getOperator() {
            return operator;
        }

        @Override
        public String toString() {
            return operator.toString();
        }
    }

//    public static class ExprOperatorOperatorDefFct extends ExprOperator{
//        public ExprOperatorOperatorDefFct(OperatorDefFct operatorDef) {
//            super(operatorDef.fct.def.name, operatorDef, new Expr[0]);
//        }
//
//        @Override
//        public OperatorDefFct getOperatorDef() {
//            return (OperatorDefFct) super.getOperatorDef();
//        }
//
//        @Override
//        public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
//            return evaluator.evalFunction(context, new ExprFunction());
//        }
//    }

    public static class OperatorDefFct extends OperatorDef{
        private OperatorDefFunction fct;

        public OperatorDefFct(OperatorDefFunction fct) {
            super(fct.def.name, Integer.MAX_VALUE);
            this.fct = fct;
        }
    }
    public static class OperatorDef {
        private String name;
        private int precedence;

        public OperatorDef(String name, int precedence) {
            this.name = name;
            this.precedence = precedence;
        }

        public String getName() {
            return name;
        }

        public int getPrecedence() {
            return precedence;
        }

        @Override
        public String toString() {
            if (precedence == Integer.MAX_VALUE) {
                return name;
            }
            return "Operator{" +
                    "'" + name + '\'' +
                    ", prec=" + precedence +
                    '}';
        }
    }

    public static class ConstDef {
        private String name;
        private Object defaultValue;

        public ConstDef(String name, Object defaultValue) {
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public String getName() {
            return name;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        @Override
        public String toString() {
            return "ConstDef{" +
                    "name='" + name + '\'' +
                    ", defaultValue=" + defaultValue +
                    '}';
        }
    }

    public static class BinaryOperatorDef extends OperatorDef {
        public BinaryOperatorDef(String name, int precedence) {
            super(name, precedence);
        }

    }

    class ExpressionEvaluatorHelper {
        Stack<Expr> vals = new Stack<>();
        Stack<OperatorNode> ops = new Stack<>();
        Object lastItem;

//        public void simplify(OperatorNode visited) {
//            while (!ops.isEmpty()) {
//                OperatorNode o = ops.peek();
//                if (o instanceof OperatorDefPar) {
//                    ops.pop();
////                    if (visited != null && visited instanceof OperatorDefClosePar) {
////                        Expr popped = vals.peek();
////                        if ((popped instanceof OperatorDefPar)) {
////                            o = ops.pop();
////                            popped = vals.pop();
////                        } else {
////                            return;
////                        }
////                    } else {
////                        return;
////                    }
//                    return;
//                } else if (o instanceof OperatorDefFunction) {
//                    if (visited != null && visited instanceof OperatorDefClosePar) {
//                        o = ops.pop();
//                        List<Expr> paramList = new ArrayList<>();
//                        Stack<Expr> paramListStack = new Stack<>();
//                        while (!vals.isEmpty()) {
//                            Expr popped = vals.pop();
////                            if ((popped instanceof OperatorDefPar)) {
////                                break;
////                            }
//                            paramListStack.push(popped);
//                        }
//                        if (paramListStack.size() > 1) {
//                            throw new IllegalArgumentException("Invalid function parameters");
//                        }
//                        //this is a function
//                        while (!paramListStack.isEmpty()) {
//                            Expr list = paramListStack.pop();
//                            if (list instanceof ExprOperator && ((ExprOperator) list).getName().equals(functionParamSeparator)) {
//                                Expr[] operands = ((ExprOperator) list).getOperands();
//                                for (int i = operands.length - 1; i >= 0; i--) {
//                                    paramListStack.push(operands[i]);
//                                }
//                            } else {
//                                paramList.add(list);
//                            }
//                        }
//                        FunctionDef g = new FunctionDef(o.getUserName());//functions.get(o.getName());
////                    if (g == null) {
////                        throw new IllegalArgumentException("Unknown function");
////                    }
//                        vals.push(new ExprFunction(g, paramList.toArray(new Expr[paramList.size()])));
//                    } else {
//                        return;
//                    }
//                } else {
//                    o = ops.pop();
//                    Expr b = vals.pop();
//                    Expr a = vals.pop();
//                    OperatorDef acceptedOp = findOperator(o.getOperator().getName());
//                    if (acceptedOp == null) {
//                        acceptedOp = o.getOperator();
//                    }
//                    vals.push(new ExprOperator(o.getUserName(), acceptedOp, new Expr[]{a, b}));
//                }
//            }
//        }

        public void openPar() {
            if (lastItem instanceof ExprVar && !vals.isEmpty() && vals.peek() instanceof ExprVar /*&& functions.get(((ExprVar) vals.peek()).getName()) != null*/) {
                if (parseFunctions) {
                    //this is a function !
                    ExprVar var = (ExprVar) vals.pop();
                    OperatorDefFunction item = new OperatorDefFunction(var.getName(), resolveFunction(var.getName()));
                    ops.push(item);
                    vals.push(new ExprFunction(item, new Expr[0]));
//                vals.push(new OperatorDefPar("("));
                    lastItem = item;
                } else {
                    OperatorDef spaceOp = findOperator(" ");
                    if (spaceOp != null) {
                        addOp(spaceOp);
                    }
                    OperatorDefPar item = new OperatorDefPar("(");
                    ops.push(item);
                    lastItem = item;
                }
            } else {
                if (lastItem!=null
                        && !(lastItem instanceof ExprOperator)
                        && !(lastItem instanceof OperatorNode)
                        ) {
                    OperatorDef spaceOp = findOperator(" ");
                    if (spaceOp != null) {
                        addOp(spaceOp);
                    }
                }
                OperatorDefPar item = new OperatorDefPar("(");
                ops.push(item);
                lastItem = item;
            }
        }

        private void simplifyStackApplyToOperator() {
            OperatorNode o = ops.pop();
            int existingArgs = vals.size();
            if (existingArgs < 2) {
                throw new IllegalArgumentException("Expected two arguments for " + o + " but found " + existingArgs);
            }

            OperatorDef acceptedOp = findOperator(o.getOperator().getName());
            if(acceptedOp instanceof OperatorDefFct){
                throw new IllegalArgumentException("Why");
            }else {
                int argsCount = 2;
                if (acceptedOp == null) {
                    acceptedOp = o.getOperator();
                }
                List<Expr> args = new ArrayList<>();
                for (int i = 0; i < argsCount; i++) {
                    args.add(0, vals.pop());
                }
                vals.push(new ExprOperator(o.getUserName(), acceptedOp, (Expr[]) args.toArray(new Expr[args.size()])));
            }

        }

        public void closePar() {
            while (!ops.isEmpty()) {
                OperatorNode peek = ops.peek();
                if (peek instanceof OperatorDefFunction) {
                    OperatorDefFunction pop = (OperatorDefFunction) ops.pop();
                    Expr pop1 = vals.pop();
                    if(pop1 instanceof ExprFunction && ((ExprFunction)pop1).getOperatorDef().fct==peek){
                        //nothing to pop!
                        vals.push(new ExprFunction(pop, new Expr[0]));
                    }else {
                        //pop ExprFunction!
                        ExprFunction e=(ExprFunction) vals.pop();

                        List<Expr> params = new ArrayList<>();
                        if (pop1 instanceof ExprOperator && ((ExprOperator) pop1).getOperatorDef().getName().equals(functionArgumentSeparator)) {
                            params.addAll(Arrays.asList(((ExprOperator) pop1).getOperands()));
                        } else {
                            params.add(pop1);
                        }
                        vals.push(new ExprFunction(pop, (Expr[]) params.toArray(new Expr[params.size()])));
                    }
                    break;
                } else if (peek instanceof OperatorDefPar && peek.getOperator().getName().equals("(")) {
                    ops.pop();
                    break;
                } else {
                    simplifyStackApplyToOperator();
                }
                //values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }


//            simplify(new OperatorDefClosePar(")"));
        }


        public void addOp(OperatorDef op) {
            OperatorNode node = new OperatorNode(op.getName(), op);

            while (!ops.empty()
                    && !(ops.peek() instanceof OperatorDefPar)
                    && !(ops.peek() instanceof OperatorDefFunction)
                    && op.precedence <= ops.peek().operator.precedence) {
                simplifyStackApplyToOperator();
            }
            ops.push(node);
            lastItem = node;
        }

        public void addValue(Expr ex) {
            vals.push(ex);
            lastItem = ex;
        }

        public Expr parse(StreamTokenizerExt tokenizer) {

            int lastTok = -1;
            boolean acceptSpaceOp = false;
            boolean doLoop = true;
            OperatorDef spaceOp = findOperator(" ");
            while (doLoop) {
                int tok = 0;
                try {
                    tok = tokenizer.nextToken();
                } catch (IOException e) {
                    throw new ExprParseException(e);
                }
                switch (tok) {
                    case StreamTokenizer.TT_WORD: {
                        if (acceptSpaceOp) {
                            if (spaceOp != null) {
                                addOp(spaceOp);
                            }
                        }
                        String usval = uniformize(tokenizer.sval);

                        OperatorDef anyOp = findOperator(usval);
                        if (anyOp != null) {
                            acceptSpaceOp = false;
                            addOp(anyOp);
                        } else {
                            acceptSpaceOp = true;
                            ConstDef c = findConst(usval);
                            if (c != null) {
                                addValue(new ExprConst(tokenizer.sval, c));
                            } else {
                                addValue(new ExprVar(tokenizer.sval));
                            }
                        }
                        break;
                    }
                    case StreamTokenizer.TT_NUMBER: {
                        if (acceptSpaceOp) {
                            if (spaceOp != null) {
                                addOp(spaceOp);
                            }
                        }
                        acceptSpaceOp = true;
                        if (tokenizer.image.indexOf('.') >= 0) {
                            addValue(new ExprVal(tokenizer.nval));
                        } else {
                            addValue(new ExprVal((int) tokenizer.nval));
                        }
                        break;
                    }
                    case '\'':
                    case '"':
                    case '`': {
                        if (acceptSpaceOp) {
                            if (spaceOp != null) {
                                addOp(spaceOp);
                            }
                        }
                        acceptSpaceOp = true;
                        addValue(new ExprStr(((char) tok), tokenizer.sval));
                        break;
                    }
                    case '(': {
                        acceptSpaceOp = false;
                        openPar();
                        break;
                    }
                    case ')': {
                        acceptSpaceOp = true;
                        closePar();
                        break;
                    }
                    case ' ': {
                        acceptSpaceOp = false;
                        break;
                    }
                    case StreamTokenizer.TT_EOF: {
                        acceptSpaceOp = false;
                        doLoop = false;
                        break;
                    }
                    default: {
                        OperatorDef anyOp = findOperator(String.valueOf((char) tok));
                        if (anyOp != null) {
                            acceptSpaceOp = false;
                            addOp(anyOp);
                        } else {
                            throw new ExprParseException("Unexpected token " + String.valueOf((char) tok) + " (" + String.valueOf(tok) + ")");
                        }
                        break;
                    }
                }
                lastTok = tok;
            }
            while (!ops.empty()) {
                simplifyStackApplyToOperator();
            }
            if (vals.isEmpty()) {
                throw new ExprParseException("Empty Expression");
            }
            return vals.pop();
        }

        private class TokenList {
            StreamTokenizerExt tokenizer;
            LinkedList<Token> temp = new LinkedList<>();

            public TokenList(StreamTokenizerExt tokenizer) {
                this.tokenizer = tokenizer;
            }

            Token get() {
                if (temp.isEmpty()) {
                    int ttype = -1;
                    try {
                        ttype = tokenizer.nextToken();
                    } catch (IOException e) {
                        throw new ExprParseException(e);
                    }
                    return new Token(ttype, tokenizer.sval, tokenizer.image, tokenizer.nval);
                }
                return temp.removeFirst();
            }

            void pushBack(Token t) {
                temp.add(t);
            }
        }

        private class Token {
            public int ttype;
            public String sval;
            public String image;
            public double nval;

            public Token(int ttype, String sval, String image, double nval) {
                this.ttype = ttype;
                this.sval = sval;
                this.image = image;
                this.nval = nval;
            }
        }
    }

    private class DefaultExprContext implements ExprContext {
        private Map<String, Object> map = new HashMap<>();

        @Override
        public Object getValue(String name) {
            return map.get(name);
        }

        @Override
        public void setValue(String name, Object value) {
            map.put(name, value);
        }
    }
}

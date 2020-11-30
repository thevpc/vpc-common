package net.thevpc.jshell.parser2;

import net.thevpc.jshell.JShell;
import net.thevpc.jshell.JShellContext;
import net.thevpc.jshell.JShellUniformException;
import net.thevpc.jshell.NodeEvalUnsafeRunnable;
import net.thevpc.jshell.parser.nodes.InstructionNode;
import net.thevpc.jshell.parser.nodes.Node;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class Yaccer {

    private final Lexer lexer;
    private final LinkedList<Node2> buffer = new LinkedList<>();

    public Yaccer(Lexer lexer) {
        this.lexer = lexer;
    }

    public Iterable<Command> commands() {
        return () -> new Iterator<Command>() {
            Command n = null;

            @Override
            public boolean hasNext() {
                n = readCommand();
                return n != null;
            }

            @Override
            public Command next() {
                return n;
            }
        };
    }

    public Iterable<Node2> nodes() {
        return () -> new Iterator<Node2>() {
            Node2 n = null;

            @Override
            public boolean hasNext() {
                n = readNode();
                return n != null;
            }

            @Override
            public Node2 next() {
                return n;
            }
        };
    }

    public Node2 readNodeL0() {
        if (!buffer.isEmpty()) {
            return buffer.removeFirst();
        }
        Token u = getLexer().peekToken();
        if (u == null) {
            return null;
        }
        switch (u.type) {
            case "WHITE": {
                getLexer().skipWhites();
                return new WhiteNode(u);
            }
            case "NEWLINE": {
                u = getLexer().nextToken();
                return new NewlineNode(u);
            }
            case "#": {
                return readComments();
            }
            case "WORD":
            case "$WORD":
            case "\"":
            case "'":
            case "`":
            case "$(":
            case "$((":
            case "${":
            case "{":
            case "=":
            case ":":
            case "&&":
            case "&":
            case "|":
            case "||":
            case "<":
            case "<<":
            case ">":
            case ">>":
            case "&>":
            case "&>>":
            case "&<":
            case "&<<": {
                return new TokenNode(getLexer().nextToken());
            }
            case "(": {
                return readPar();
            }
        }
        throw new IllegalArgumentException("Unexpected " + u.type);
    }

    private Lexer getLexer() {
        return lexer;
    }

    public Node2 readNodeL1() {
        Node2 a = readNodeL0();
        if (a == null) {
            return a;
        }
        Token u = getLexer().peekToken();
        if (u == null) {
            return a;
        }
        switch (u.type) {
            case "&&":
            case "&": {
                Token op = getLexer().nextToken();
                Node2 b = readNodeL0();
                if (b == null) {
                    return new UnOpSuffix(a, op);
                }
                return new BinOp(a, op, b);
            }
        }
        return a;
    }

    public Node2 readNodeL2() {
        Node2 a = readNodeL1();
        if (a == null) {
            return a;
        }
        Token u = getLexer().peekToken();
        if (u == null) {
            return a;
        }
        switch (u.type) {
            case "||":
            case "|": {
                Token op = getLexer().nextToken();
                Node2 b = readNodeL1();
                if (b == null) {
                    return new UnOpSuffix(a, op);
                }
                return new BinOp(a, op, b);
            }
        }
        return a;
    }

    public Node2 readNodeL3() {
        Node2 a = readNodeL1();
        if (a == null) {
            return a;
        }
        Token u = getLexer().peekToken();
        if (u == null) {
            return a;
        }
        switch (u.type) {
            case ";": {
                Token op = getLexer().nextToken();
                Node2 b = readNodeL1();
                if (b == null) {
                    return new UnOpSuffix(a, op);
                }
                return new BinOp(a, op, b);
            }
        }
        return a;
    }

    public Node2 readNodeL4() {
        Node2 a = readNodeL3();
        if (a == null) {
            return a;
        }
        Token u = getLexer().peekToken();
        if (u == null) {
            return a;
        }
        switch (u.type) {
            case ">":
            case "&>":
            case "<":
            case "&<": {
                Token op = getLexer().nextToken();
                Node2 b = readNodeL3();
                if (b == null) {
                    return new UnOpSuffix(a, op);
                }
                return new BinOp(a, op, b);
            }
        }
        return a;
    }

    public Node2 readNodeL5() {
        Node2 a = readNodeL4();
        if (a == null) {
            return null;
        }

        Token u = getLexer().peekToken();
        if (u == null) {
            return a;
        }
        switch (u.type) {
            case ";": {
                Token op = getLexer().nextToken();
                Node2 b = readNodeL4();
                if (b == null) {
                    return new UnOpSuffix(a, op);
                }
                return new BinOp(a, op, b);
            }
        }
        return a;
    }


    public Node2 readNode() {
//        return readNodeL5();
        return readNodeL0();
    }

    public Par readPar() {
        Token u = getLexer().peekToken();
        if (u == null) {
            return null;
        }
        if (u.type.equals("(")) {
            getLexer().nextToken();
            Node2 n = readCommand();
            u = getLexer().peekToken();
            if (u == null) {
                return null;
            }
            getLexer().nextToken();
            return new Par(n);
        }
        return null;
    }

    public Command readCommandL1() {
        getLexer().skipWhites();
        Token t = getLexer().peekToken();
        if (t == null) {
            return null;
        }
        Command line = readLine();
        if (line == null) {
            return null;
        }
        boolean loop = true;
        while (loop) {
            loop = false;
            t = getLexer().peekToken();
            if (t != null) {
                switch (t.type) {
                    case "<":
                    case ">":
                    case "<<":
                    case ">>":
                    case "&<":
                    case "&>":
                    case "&<<":
                    case "&>>": {
                        getLexer().nextToken();
                        Command next = readLine();
                        if (next == null) {
                            line = new SuffixOpCommand(line, t);
                        } else {
                            line = new BinOpCommand(line, t, next);
                            loop = true;
                        }
                        break;
                    }
                }
            }
        }
        return line;
    }

    public Command readCommandL2() {
        Command line = readCommandL1();
        if (line == null) {
            return null;
        }
        boolean loop = true;
        while (loop) {
            loop = false;
            Token t = getLexer().peekToken();
            if (t != null) {
                switch (t.type) {
                    case "|": {
                        getLexer().nextToken();
                        Command next = readCommandL1();
                        if (next == null) {
                            line = new SuffixOpCommand(line, t);
                        } else {
                            line = new BinOpCommand(line, t, next);
                            loop = true;
                        }
                    }
                }
            }
        }
        return line;
    }

    public Command readCommandL3() {
        Command line = readCommandL2();
        if (line == null) {
            return null;
        }
        boolean loop = true;
        while (loop) {
            loop = false;
            Token t = getLexer().peekToken();
            if (t != null) {
                switch (t.type) {
                    case "&&":
                    case "||": {
                        getLexer().nextToken();
                        Command next = readCommandL2();
                        if (next == null) {
                            line = new SuffixOpCommand(line, t);
                        } else {
                            line = new BinOpCommand(line, t, next);
                            loop = true;
                        }
                    }
                }
            }
        }
        return line;
    }

    private String getArgumentsLineFirstArgToken(Command line) {
        if (line != null) {
            Argument arg1 = ((ArgumentsLine) line).args.get(0);
            if (arg1.nodes.size() == 1 && arg1.nodes.get(0) instanceof TokenNode) {
                Token token = ((TokenNode) arg1.nodes.get(0)).token;
                if (token.type.equals("WORD")) {
                    return token.value.toString();
                }
            }
        }
        return "";
    }

    public Command readCommandSimple() {
        Command line = readCommandL3();
        if (line == null) {
            return null;
        }
        String start = getArgumentsLineFirstArgToken(line);
        switch (start) {
            case "if": {
                IfCommand ifCommand = new IfCommand();
                ifCommand._then = readCommandSimple();
                if (!getArgumentsLineFirstArgToken(ifCommand._then).equals("then")) {
                    throw new IllegalArgumentException("Error");
                }
                ifCommand._if = new CondBloc(line, readCommandSimple());
                boolean endIf = false;
                while (!endIf) {
                    Command a = readCommandSimple();
                    switch (getArgumentsLineFirstArgToken(a)) {
                        case "else": {
                            if (ifCommand._else == null) {
                                ifCommand._else = readCommandSimple();
                                if (ifCommand._else == null) {
                                    throw new IllegalArgumentException("Missing Else");
                                }
                            } else {
                                throw new IllegalArgumentException("Else already visited");
                            }
                            break;
                        }
                        case "elif": {
                            Command c = readCommandSimple();
                            if (c == null) {
                                throw new IllegalArgumentException("Missing Elif");
                            }
                            ifCommand._elif.add(new CondBloc(a, c));
                            break;
                        }
                        case "fi": {
                            //good
                            endIf = true;
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Expected fi");
                        }
                    }
                }
                return ifCommand;
            }
            case "while": {
                WhileCommand ifCommand = new WhileCommand();
                ifCommand._do = readCommandSimple();
                ifCommand._while = new CondBloc(line, readCommandSimple());
                ifCommand._done = readCommandSimple();
                return ifCommand;
            }
        }
        boolean loop = true;
        while (loop) {
            loop = false;
            Token t = getLexer().peekToken();
            if (t != null) {
                switch (t.type) {
                    case "&":{
                        getLexer().nextToken();
                        Command next = readCommandL3();
                        if (next == null) {
                            line = new SuffixOpCommand(line, t);
                        } else {
                            line = new BinOpCommand(line, t, next);
                            loop = true;
                        }
                        break;
                    }
                    case "NEWLINE": {
                        getLexer().nextToken();
                        Command next = readCommandL3();
                        if (next == null) {
                            //
                        } else {
                            line = new BinOpCommand(line, t, next);
                            loop = true;
                        }
                        break;
                    }
                }
            }
        }
        return line;
    }

    public Command readCommand() {
        return readCommandSimple();
    }

    public ArgumentsLine readLine() {
        List<Argument> a = new ArrayList<>();
        while (true) {
            Token t = getLexer().peekToken();
            if (t == null) {
                break;
            }
            boolean exit = false;
            switch (t.type) {
                case "NEWLINE":
                case ";":
                case "&":
                case "&&":
                case "<<":
                case ">>":
                case "&<":
                case "&>":
                case "&<<":
                case "&>>":
                case "|":
                case "||": {
                    exit = true;
                    break;
                }
            }
            if (exit) {
                break;
            }
            if (t.type.equals("WHITE")) {
                getLexer().nextToken();
                //ignore
            } else {
                Argument aa = readArgument();
                if (aa != null) {
                    a.add(aa);
                } else {
                    throw new IllegalArgumentException("Unexpected " + aa);
                }
            }
        }
        if (a.isEmpty()) {
            return null;
        }
        return new ArgumentsLine(a);
    }

    public List<Command> readScript() {
        List<Command> a = new ArrayList<>();
        while (true) {
            Command aa = readLine();
            if (aa != null) {
                a.add(aa);
            } else {
                break;
            }
        }
        if (a.isEmpty()) {
            return null;
        }
        return a;
    }

    public Comments readComments() {
        List<Token> ok = new ArrayList<>();
        while (true) {
            Token t = getLexer().peekToken();
            if (t == null) {
                break;
            }
            if (t.type.equals("#")) {
                getLexer().nextToken();
                ok.add(t);
            } else {
                break;
            }
        }
        if (ok.isEmpty()) {
            return null;
        }
        return new Comments(ok);
    }

    public Argument readArgument() {
        List<Node2> a = new ArrayList<>();
        while (true) {
            Token t = getLexer().peekToken();
            if (t == null || t.type.equals("NEWLINE")) {
                break;
            }
            if (t.type.equals("#")) {
                if (!a.isEmpty()) {
                    break;
                }
            }
            Node2 n = readNode();
            if (n == null) {
                break;
            }
            if (n instanceof WhiteNode) {
                break;
            }
            a.add(n);
        }
        if (a.isEmpty()) {
            return null;
        }
        return new Argument(a);
//        List<Token> ok = new ArrayList<>();
//        boolean loop = true;
//        while (loop) {
//            Token t = jShellParser2.lexer().peedToken();
//            if (t == null) {
//                break;
//            }
//            switch (t.type) {
//                case "WORD":
//                case "$WORD":
//                case "$(":
//                case "$((":
//                case "${":
//                case "(":
//                case "{":
//                case "=":
//                case ":":
//                case "\"":
//                case "'":
//                case "`": {
//                    jShellParser2.lexer().nextToken();
//                    ok.add(t);
//                    break;
//                }
//                default: {
//                    loop = false;
//                    break;
//                }
//            }
//        }
//        if (ok.isEmpty()) {
//            return null;
//        }
//        return new Argument(ok);
    }

    public String evalNodeString(Node node, JShellContext context) {
        if (node instanceof Comments) {
            return "";
        } else if (node instanceof TokenNode) {
            switch (((TokenNode) node).token.type) {
                case "WORD": {
                    return (((TokenNode) node).token.value.toString());
                }
                case "$WORD": {
                    String s = (String) ((TokenNode) node).token.value;
                    switch (s) {
                        case "0": {
                            return (context.getServiceName());
                        }
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                        case "5":
                        case "6":
                        case "7":
                        case "8":
                        case "9": {
                            return context.getArgsArray()[Integer.parseInt(s) - 1];
                        }
                        case "?": {
                            return String.valueOf(context.getArgsArray().length);
                        }
                        default:{
                            String y = context.vars().get(s);
                            if(y==null){
                                y="";
                            }
                            return y;
                        }
                    }
                }
                case "`":
                case "$(": {
                    List<Token> subTokens = new ArrayList<>((Collection<? extends Token>) ((TokenNode) node).token.value);
                    Yaccer yy2 = new Yaccer(new PreloadedLexer(subTokens));
                    Command subCommand = yy2.readCommand();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    JShellContext c2 = context.getShell().createContext(context)
                            //need to inherit service name and arguments!!
                            .setServiceName(context.getServiceName())
                            .setArgs(context.getArgsArray());
                    PrintStream p = new PrintStream(out);
                    c2.setOut(p);
                    subCommand.eval(c2);
                    p.flush();
                    return (context.getShell().escapeString(out.toString()));
                }
                default: {
                    String newline = ((TokenNode) node).token.value.toString();
                    return (newline);
                }
            }
        }
        throw new RuntimeException("Error");
    }

    public interface Node2 extends Node {

    }

    public abstract interface Command extends InstructionNode, Node2 {
        void eval(JShellContext context);

    }

    public class WhiteNode implements Node2 {
        Token token;

        public WhiteNode(Token token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return String.valueOf(token);
        }
    }

    public class NewlineNode implements Node2 {
        Token token;

        public NewlineNode(Token token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return String.valueOf(token);
        }
    }

    public class TokenNode implements Node2 {
        Token token;

        public TokenNode(Token token) {
            this.token = token;
        }

        @Override
        public String toString() {
            return String.valueOf(token);
        }
    }

    public class BinOpCommand implements Command {
        Command left;
        Token op;
        Command right;

        public BinOpCommand(Command left, Token op, Command right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

        @Override
        public void eval(final JShellContext context) {
            String cmd = op.type.equals("NEWLINE")?";":String.valueOf(op.value);
            context.getShell().getNodeEvaluator().evalBinaryOperation(cmd, left, right, context);
        }

        @Override
        public String toString() {
            return "(" +
                    left +
                    " " + op.value +
                    right +
                    ')';
        }
    }

    public class SuffixOpCommand implements Command {
        Command a;
        Token op;

        public SuffixOpCommand(Command a, Token op) {
            this.a = a;
            this.op = op;
        }

        @Override
        public void eval(JShellContext context) {
            switch (op.type){
                case "&":{
                    context.getShell().getNodeEvaluator().evalSuffixAndOperation(a, context);
                    break;
                }
            }
            throw new IllegalArgumentException("Unsupported yet");
        }
    }

    public class CondBloc {
        Command cond;
        Command block;

        public CondBloc(Command cond, Command block) {
            this.cond = cond;
            this.block = block;
        }

        public boolean eval(JShellContext context) {
//        System.out.println("+ IF " + conditionNode);
            boolean trueCond = false;
            if (cond != null) {
                try {
                    context.getShell().uniformException(new NodeEvalUnsafeRunnable(cond, context));
                    trueCond = true;
                } catch (JShellUniformException ex) {
                    if (ex.isQuit()) {
                        ex.throwQuit();
                    }
                    trueCond = false;
                }
                if (trueCond) {
                    if (block != null) {
                        block.eval(context);
                    }
                    return true;
                }
            }
            return false;
        }
    }

    public class IfCommand implements Command {
        CondBloc _if;
        Command _then;
        List<CondBloc> _elif = new ArrayList<>();
        Command _else;

        @Override
        public void eval(JShellContext context) {
//        System.out.println("+ IF " + conditionNode);
            if (_if.eval(context)) {
                return;
            }
            for (CondBloc condBloc : _elif) {
                if (condBloc.eval(context)) {
                    return;
                }
            }
            if (_else != null) {
                _else.eval(context);
            }
        }

    }

    public class WhileCommand implements Command {
        CondBloc _while;
        Command _do;
        Command _done;

        @Override
        public void eval(JShellContext context) {
            while (true) {
                if (!_while.eval(context)) {
                    break;
                }
            }
        }
    }

    public class ArgumentsLine implements Command {
        List<Argument> args;

        public ArgumentsLine(List<Argument> args) {
            this.args = args;
        }

        @Override
        public void eval(JShellContext context) {
            JShell shell = context.getShell();
            ArrayList<String> cmds = new ArrayList<String>();
            for (Argument arg : args) {
                String r = arg.evalString(context);
                cmds.add(r);
            }
            if(cmds.isEmpty()){
                return;
            }
            if(cmds.size()==1 && cmds.get(0).isEmpty()){
                return;
            }
            shell.executePreparedCommand(cmds.toArray(new String[0]), true, true, true, context);
        }

        @Override
        public String toString() {
            return "ArgumentsLine{" +
                    args +
                    '}';
        }
    }

    public class Par implements Node2 {
        Node2 element;

        public Par(Node2 element) {
            this.element = element;
        }
    }

    public class UnOpSuffix implements Node2 {
        Node2 a;
        Token op;

        public UnOpSuffix(Node2 a, Token op) {
            this.a = a;
            this.op = op;
        }

        @Override
        public String toString() {
            return a +
                    " " + op;
        }
    }

    public class BinOp implements Node2 {
        Node2 a;
        Token op;
        Node2 b;

        public BinOp(Node2 a, Token op, Node2 b) {
            this.a = a;
            this.op = op;
            this.b = b;
        }

        @Override
        public String toString() {
            return a +
                    " " + op +
                    " " + b;
        }
    }

    public class Argument implements Node2 {
        List<Node2> nodes;

        public Argument(List<Node2> nodes) {
            this.nodes = nodes;
        }

        @Override
        public String toString() {
            if (nodes.size() == 1) {
                return nodes.get(0).toString();
            }
            return nodes.toString();
        }

        public String evalString(JShellContext context) {
            StringBuilder sb = new StringBuilder();
            for (Node2 node : nodes) {
                sb.append(evalNodeString(node, context));
            }
            return sb.toString();
        }
    }

    public class Comments implements Node2 {
        List<Token> tokens;

        public Comments(List<Token> tokens) {
            this.tokens = tokens;
        }

        @Override
        public String toString() {
            return "Comments{" +
                    tokens +
                    '}';
        }
    }

//    public void pushBack(Node n){
//        buffer.addFirst(n);
//    }
}

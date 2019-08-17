package net.vpc.common.javashell;

import net.vpc.common.javashell.parser.JShellParserHelper;
import net.vpc.common.javashell.parser.nodes.InstructionNode;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DefaultJShellWordEvaluator implements JShellWordEvaluator {
    public static int readQuotes(char[] chars, int i, StringBuilder v) {
        return JShellParserHelper.readQuotes(chars, i, v);
    }

    @Override
    public String evalSimpleQuotesExpression(String expressionString, JShellContext context) {
//should replace wildcards ...
        StringBuilder sb = new StringBuilder();
        char[] chars = expressionString.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '$': {
                    sb.append(chars[i]);
                    break;
                }
                case '\"': {
                    i=readAndEvalDblQuotesExpression(chars,i,sb,context);
                    break;
                }
                case '`': {
                    i=readAndEvalAntiQuotesString(chars,i,sb,context);
                    break;
                }
                default: {
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String evalDollarSharp(JShellContext context) {
        return (String.valueOf(context.getArgsList().size()));
    }

    @Override
    public String evalDollarName(String name, JShellContext context) {
        return (String.valueOf(context.vars().get(name, "")));
    }

    @Override
    public String evalDollarInterrogation(JShellContext context) {
        return (String.valueOf(context.getLastResult().getCode()));
    }

    @Override
    public String evalDollarInteger(int index, JShellContext context) {
        if (index < context.getArgsList().size()) {
            return (String.valueOf(context.getArgsList().get(index)));
        }
        return "";
    }

    @Override
    public String evalDollarExpression(String stringExpression, JShellContext context) {
        String str = evalSimpleQuotesExpression(stringExpression, context);
        if (str.equals("#")) {
            return evalDollarSharp(context);
        } else if (str.equals("?")) {
            return evalDollarInterrogation(context);
        } else if (str.isEmpty()) {
            //do nothing
            return "";
        } else if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
            int index = Integer.parseInt(str, 10);
            return evalDollarInteger(index, context);
        } else {
            return evalDollarName(str, context);
        }
    }

    @Override
    public String evalDoubleQuotesExpression(String stringExpression, JShellContext context) {
//should replace wildcards ...
        StringBuilder sb = new StringBuilder();
        char[] chars = stringExpression.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '$': {
                    i=readAndEvalDollarExpression(chars,i,sb,false,context);
                    break;
                }
                case '\'': {
                    i=readAndEvalSimpleQuotesExpression(chars,i,sb,context);
                    break;
                }
                case '`': {
                    i=readAndEvalAntiQuotesString(chars,i,sb,context);
                    break;
                }
                default: {
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String evalAntiQuotesExpression(String stringExpression, JShellContext context) {
        InstructionNode t = context.getShell().parseCommandLine(stringExpression);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JShellContext c2 = context.getShell().createContext(context);
        c2.setOut(new PrintStream(out));
        t.eval(c2);
        c2.out().flush();
        return out.toString();
    }

    public String expandEnvVars(String stringExpression, boolean escapeResultPath, JShellContext context) {
        StringBuilder sb = new StringBuilder();
        char[] chars = stringExpression.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '$': {
                    i=readAndEvalDollarExpression(chars,i,sb,true,context);
                    break;
                }
                default: {
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }
    public String evalNoQuotesExpression(String stringExpression, JShellContext context) {
        StringBuilder sb = new StringBuilder();
        char[] chars = stringExpression.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            switch (c) {
                case '$': {
                    i=readAndEvalDollarExpression(chars,i,sb,true,context);
                    break;
                }
//                case '\"': {
//                    i=readAndEvalDblQuotesExpression(chars,i,sb,context);
//                    break;
//                }
//                case '\'': {
//                    i=readAndEvalSimpleQuotesExpression(chars,i,sb,context);
//                    break;
//                }
//                case '`': {
//                    i=readAndEvalAntiQuotesString(chars,i,sb,context);
//                    break;
//                }
                case '\\': {
                    i++;
                    sb.append(chars[i]);
                    break;
                }
                default: {
                    sb.append(chars[i]);
                }
            }
        }
        return sb.toString();
    }

    protected int readAndEvalSimpleQuotesExpression(char[] chars, int i, StringBuilder out, JShellContext context) {
        StringBuilder v = new StringBuilder();
        int count = readQuotes(chars, i, v);
        out.append(evalSimpleQuotesExpression(v.toString(), context));
        return i + count;
    }

    protected int readAndEvalAntiQuotesString(char[] chars, int i, StringBuilder out, JShellContext context) {
        StringBuilder v = new StringBuilder();
        int count = readQuotes(chars, i, v);
        out.append(evalAntiQuotesExpression(v.toString(), context));
        return i + count;
    }

    protected int readAndEvalDblQuotesExpression(char[] chars, int i, StringBuilder out, JShellContext context) {
        StringBuilder v = new StringBuilder();
        int count = readQuotes(chars, i, v);
        out.append(evalDoubleQuotesExpression(v.toString(), context));
        return i + count;
    }

    protected int readAndEvalDollarExpression(char[] chars, int i, StringBuilder out, boolean escapeResultPath, JShellContext context) {
        if (i + 1 < chars.length) {
            i++;
            if (chars[i] == '{') {
                StringBuilder v = new StringBuilder();
                while (i < chars.length && chars[i] != '}') {
                    v.append(chars[i]);
                    i++;
                }
                String r = evalDollarExpression(v.toString(), context);
                if(escapeResultPath){
                    r=context.getShell().escapePath(r);
                }
                out.append(r);
            } else {
                StringBuilder v = new StringBuilder();
                while (i < chars.length && chars[i] != ' ' && chars[i] != '\t') {
                    v.append(chars[i]);
                    i++;
                }
                String r = evalDollarExpression(String.valueOf(v.toString()), context);
                if(escapeResultPath){
                    r=context.getShell().escapePath(r);
                }
                out.append(r);
            }
        }
        return i;
    }

}

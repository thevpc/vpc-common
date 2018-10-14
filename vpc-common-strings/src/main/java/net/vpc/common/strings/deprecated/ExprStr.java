package net.vpc.common.strings.deprecated;

//package net.vpc.common.strings;
//
///**
// * Created by vpc on 4/16/17.
// */
//public class ExprStr implements Expr {
//    private char quotes;
//    private String value;
//
//    public ExprStr(char quotes, String value) {
//        this.quotes = quotes;
//        this.value = value;
//    }
//
//    public char getQuotes() {
//        return quotes;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    @Override
//    public Object eval(ExprContext context, ExpressionEvaluator evaluator) {
//        return evaluator.evalStr(context, this);
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(quotes);
//        for (char c : value.toCharArray()) {
//            switch (c) {
//                case '\n': {
//                    sb.append("\\n");
//                }
//                case '\\': {
//                    sb.append("\\\\");
//                }
//                case '\r': {
//                    sb.append("\\r");
//                }
//                case '\f': {
//                    sb.append("\\f");
//                }
//                default: {
//                    if (c == quotes) {
//                        sb.append("\\");
//                    }
//                    sb.append(c);
//                }
//            }
//        }
//        sb.append(quotes);
//        return sb.toString();
//    }
//}

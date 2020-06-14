package net.vpc.common.jeep;

import java.util.HashMap;
import java.util.Map;

public class JOperatorPrecedences {
    public static final String OPERATORS = "-+$=<>#~%~/*|&!^#:.;`\\\"'?";
    /**
     * none
     */
    public static final int PRECEDENCE_0 = 1;
    /**
     * ',' ';'
     */
    public static final int PRECEDENCE_1 = 10;
    /**
     * ' ' '='
     */
    public static final int PRECEDENCE_2 = 20;
    /**
     * '&lt;' '&gt;' '#' '!'
     */
    public static final int PRECEDENCE_3 = 30;
    /**
     * '&' '|' '~'
     */
    public static final int PRECEDENCE_4 = 40;
    /**
     * '+' '-'
     */
    public static final int PRECEDENCE_5 = 50;
    /**
     * '*' '/'
     */
    public static final int PRECEDENCE_6 = 60;
    /**
     * '^' '$' ':'
     */
    public static final int PRECEDENCE_7 = 70;
    /**
     * none
     */
    public static final int PRECEDENCE_8 = 80;
    /**
     * none
     */
    public static final int PRECEDENCE_9 = 90;
    /**
     * none
     */
    public static final int PRECEDENCE_10 = 100;
    public static final int PRECEDENCE_11 = 110;
    public static final int PRECEDENCE_12 = 120;
    public static final int PRECEDENCE_13 = 130;
    public static final int PRECEDENCE_14 = 140;
    public static final int PRECEDENCE_15 = 150;
    public static final int PRECEDENCE_16 = 160;
    public static final int PRECEDENCE_17 = 170;
    public static final int PRECEDENCE_18 = 180;
    public static final int PRECEDENCE_19 = 190;
    public static final int PRECEDENCE_20 = 200;
    public static final int PRECEDENCE_MAX = 102;
    public static final Map<JOperator, Integer> defaultPrecedences = new HashMap<>();

    static {
        defaultPrecedences.put(JOperator.infix("."), PRECEDENCE_15);

        defaultPrecedences.put(JOperator.postfix("++"), PRECEDENCE_14);
        defaultPrecedences.put(JOperator.postfix("--"), PRECEDENCE_14);

        defaultPrecedences.put(JOperator.prefix("++"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("--"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("+"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("-"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("!"), PRECEDENCE_13);
        defaultPrecedences.put(JOperator.prefix("~"), PRECEDENCE_13);

        defaultPrecedences.put(JOperator.infix("*"), PRECEDENCE_12);
        defaultPrecedences.put(JOperator.infix("/"), PRECEDENCE_12);
        defaultPrecedences.put(JOperator.infix("%"), PRECEDENCE_12);

        defaultPrecedences.put(JOperator.infix("+"), PRECEDENCE_11);
        defaultPrecedences.put(JOperator.infix("-"), PRECEDENCE_11);

        defaultPrecedences.put(JOperator.infix("<<"), PRECEDENCE_10);
        defaultPrecedences.put(JOperator.infix(">>"), PRECEDENCE_10);
        defaultPrecedences.put(JOperator.infix(">>>"), PRECEDENCE_10);

        defaultPrecedences.put(JOperator.infix("<"), PRECEDENCE_9);
        defaultPrecedences.put(JOperator.infix("<="), PRECEDENCE_9);
        defaultPrecedences.put(JOperator.infix(">"), PRECEDENCE_9);
        defaultPrecedences.put(JOperator.infix(">="), PRECEDENCE_9);

        defaultPrecedences.put(JOperator.infix("=="), PRECEDENCE_8);
        defaultPrecedences.put(JOperator.infix("!="), PRECEDENCE_8);

        defaultPrecedences.put(JOperator.infix("&"), PRECEDENCE_7);

        defaultPrecedences.put(JOperator.infix("^"), PRECEDENCE_6);

        defaultPrecedences.put(JOperator.infix("|"), PRECEDENCE_5);

        defaultPrecedences.put(JOperator.infix("&&"), PRECEDENCE_4);

        defaultPrecedences.put(JOperator.infix("||"), PRECEDENCE_3);

        defaultPrecedences.put(JOperator.infix("="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("+="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("-="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("*="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("/="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("%="), PRECEDENCE_1);
        defaultPrecedences.put(JOperator.infix("->"), PRECEDENCE_0);
    }

    public static int getDefaultPrecedenceOrNull(JOperator op) {
        return defaultPrecedences.get(op);
    }

    public static int getDefaultPrecedence(JOperator op) {
        Integer a = defaultPrecedences.get(op);
        if (a != null) {
            return a;
        }
        throw new IllegalArgumentException("No Default Precedence for " + op);
    }
}

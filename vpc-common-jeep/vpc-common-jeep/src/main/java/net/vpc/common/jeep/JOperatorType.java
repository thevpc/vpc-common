package net.vpc.common.jeep;

public enum JOperatorType {
    PREFIX_UNARY,
    POSTFIX_UNARY,
    INFIX_BINARY,

    /**
     * special operators are operators that should be handled in a special manner.
     * They do not have priorities
     */
    SPECIAL,
}

package net.vpc.common.javashell;

/**
 * Created by vpc on 11/4/16.
 */
public interface JShellWordEvaluator {

    String evalDollarSharp(JShellContext context);

    String evalDollarName(String name, JShellContext context);

    String evalDollarInterrogation(JShellContext context);

    String evalDollarInteger(int index, JShellContext context);

    String evalDollarExpression(String stringExpression, JShellContext context);

    String evalSimpleQuotesExpression(String expressionString, JShellContext context);

    String evalDoubleQuotesExpression(String stringExpression, JShellContext context);

    String evalAntiQuotesExpression(String stringExpression, JShellContext context);

    String evalNoQuotesExpression(String stringExpression, JShellContext context);

    String expandEnvVars(String stringExpression, boolean escapeResultPath, JShellContext context);
}

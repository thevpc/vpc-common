package net.vpc.common.jeep;

import java.util.Map;

public interface ExpressionEvaluator {
    Map<String,Object> getUserProperties();

    ExpressionManager getExpressionManager();

    Object evaluate();

    Object evaluateFunction(String name, ExpressionNode... args);

    Object getVariableValue(String varName);

    void setVariableValue(String varName, Object value);


}

package net.vpc.common.jeep;

public interface JEvaluators {
    JEvaluatorFactory getFactory();

    JEvaluators setFactory(JEvaluatorFactory factory);

    JEvaluator newEvaluator();

}

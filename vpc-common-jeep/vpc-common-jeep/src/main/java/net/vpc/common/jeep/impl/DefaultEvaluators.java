package net.vpc.common.jeep.impl;

import net.vpc.common.jeep.JContext;
import net.vpc.common.jeep.JEvaluator;
import net.vpc.common.jeep.JEvaluatorFactory;
import net.vpc.common.jeep.JEvaluators;
//import net.vpc.common.jeep.core.DefaultJEvaluator;

public class DefaultEvaluators implements JEvaluators {
    private JContext context;
    private JEvaluatorFactory factory;
    private JEvaluators parent;

    public DefaultEvaluators(JContext context,JEvaluators parent) {
        this.context = context;
        this.parent = parent;
    }

    @Override
    public JEvaluatorFactory getFactory() {
        return factory;
    }

    @Override
    public JEvaluators setFactory(JEvaluatorFactory factory) {
        this.factory = factory;
        return this;
    }

    public JEvaluator newEvaluator() {
        if (factory != null) {
            return factory.create(context);
        }
        if(parent!=null){
            JEvaluator e = parent.newEvaluator();
            if(e!=null){
                return e;
            }
        }
        throw new IllegalArgumentException("Missing Evaluator Instance");
//        return DefaultJEvaluator.INSTANCE;
    }

}

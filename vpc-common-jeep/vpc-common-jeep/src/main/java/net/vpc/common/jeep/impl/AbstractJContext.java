/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.impl.functions.DefaultJInvokeContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vpc
 */
public abstract class AbstractJContext implements JContext {
    private final Map<String, Object> userProperties = new HashMap<>();
    private JCompilerLog log=new DefaultJCompilerLog();

    @Override
    public JCompilerLog log() {
        return log;
    }

    @Override
    public JContext log(JCompilerLog log) {
        this.log = log==null?new DefaultJCompilerLog() : log;
        return this;
    }

    @Override
    public Map<String, Object> userProperties() {
        return userProperties;
    }

    public Object evaluate(JNode expression) {
        if (expression == null) {
            return null;
        }
        JEvaluator jEvaluator = evaluators().newEvaluator();
        return jEvaluator.evaluate(expression, new DefaultJInvokeContext(
                this,
                jEvaluator,
                null,
                new JEvaluable[0],
                null,null
        ));
    }

    public Object evaluate(String expression) {
        if (expression == null) {
            return null;
        }
        return evaluate(parse(expression));
    }


}

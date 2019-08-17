/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.util.Stack;
import java.util.concurrent.Callable;

/**
 *
 * @author vpc
 */
public class ExpressionEvaluatorSharedContextHelper {

    static ThreadLocal<Stack<ExpressionManager>> context = new ThreadLocal<>();

    public static ExpressionManager getCurrent() {
        Stack<ExpressionManager> v = context.get();
        if(v==null || v.isEmpty()){
            return null;
        }
        return v.peek();
    }
    
    public static <T> T invokeSilentCallable(ExpressionManager e, SilentCallable<T> run) {
        Stack<ExpressionManager> v = context.get();
        if (v == null) {
            v = new Stack<>();
            context.set(v);
        }
        v.push(e);
        try {
            return run.call();
        } finally {
            v.pop();
        }
    }

    public static <T> T invokeCallable(ExpressionManager e, Callable<T> run) throws Exception {
        Stack<ExpressionManager> v = context.get();
        if (v == null) {
            v = new Stack<>();
            context.set(v);
        }
        v.push(e);
        try {
            return run.call();
        } finally {
            v.pop();
        }
    }

    public static void invokeRunnable(ExpressionManager e, Runnable run) {
        Stack<ExpressionManager> v = context.get();
        if (v == null) {
            v = new Stack<>();
            context.set(v);
        }
        v.push(e);
        try {
            run.run();
        } finally {
            v.pop();
        }
    }
}

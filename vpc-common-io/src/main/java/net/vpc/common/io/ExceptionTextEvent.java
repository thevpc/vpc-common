/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.io;

/**
 * @author vpc
 */
public class ExceptionTextEvent {
    private Object source;
    private ExceptionText exceptionText;

    public ExceptionTextEvent(Object source, ExceptionText exceptionText) {
        this.source = source;
        this.exceptionText = exceptionText;
    }

    public Object getSource() {
        return source;
    }

    public ExceptionText getExceptionText() {
        return exceptionText;
    }

}

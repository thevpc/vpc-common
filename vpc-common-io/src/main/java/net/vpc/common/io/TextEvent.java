/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.io;

/**
 * @author vpc
 */
public class TextEvent {
    private Object source;
    private String line;

    public TextEvent(Object source, String line) {
        this.source = source;
        this.line = line;
    }

    public Object getSource() {
        return source;
    }

    public String getLine() {
        return line;
    }

}

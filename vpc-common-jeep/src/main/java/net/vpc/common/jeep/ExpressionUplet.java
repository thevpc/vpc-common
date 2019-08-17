/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 *
 * @author vpc
 */
public class ExpressionUplet {
    private String name;
    private Object[] elements;

    public ExpressionUplet(String name, Object[] elements) {
        this.name = name;
        this.elements = elements;
    }

    public String getName() {
        return name;
    }

    public Object[] getElements() {
        return elements;
    }
    
}

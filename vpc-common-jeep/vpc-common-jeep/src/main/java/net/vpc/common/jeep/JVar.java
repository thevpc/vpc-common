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
public interface JVar {

    JType undefinedType();

    boolean isReadOnly();

    void setReadOnly(boolean readOnly);

    String name();

//    JType getEffectiveType(JContext context);

    JType type();

    Object getValue(JContext context);

    JVar setValue(Object value, JContext context);

    boolean isDefinedValue();

    boolean isUndefinedValue();

    JVar setUndefinedValue();

}
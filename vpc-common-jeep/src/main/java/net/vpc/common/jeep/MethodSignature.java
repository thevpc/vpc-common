/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.util.Objects;

/**
 *
 * @author vpc
 */
public class MethodSignature extends ArgumentTypes {

    private final String name;

    public MethodSignature(String name, Class[] operandTypes, boolean varArg) {
        super(operandTypes, varArg);
        this.name = name;
    }

    public MethodSignature toNoVarArgs() {
        if (isVarArgs()) {
            return new MethodSignature(name, getTypes(), false);
        }
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MethodSignature methodSignature = (MethodSignature) o;
        return Objects.equals(name, methodSignature.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name);
    }

    @Override
    public String toString() {
        return ((name == null || name.isEmpty()) ? "<IMPLICIT>" : name) + "(" + JeepUtils.getSimpleClassName(getTypes()) + ")";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.util.Objects;

/**
 *
 * @author vpc
 */
public class BooleanTuple2 {

    private final boolean value1;
    private final boolean value2;

    public BooleanTuple2(boolean value1, boolean value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public boolean getValue1() {
        return value1;
    }

    public boolean getValue2() {
        return value2;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.value1);
        hash = 53 * hash + Objects.hashCode(this.value2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BooleanTuple2 other = (BooleanTuple2) obj;
        if (this.value1!=other.value1) {
            return false;
        }
        if (this.value2!= other.value2) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tuple{" + "value1=" + value1 + ", value2=" + value2 + '}';
    }

}

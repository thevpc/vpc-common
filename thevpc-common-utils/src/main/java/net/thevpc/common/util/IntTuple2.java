/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.util;

import java.util.Objects;

/**
 *
 * @author thevpc
 */
public class IntTuple2 {

    private final int value1;
    private final int value2;

    public IntTuple2(int value1, int value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public int getValue1() {
        return value1;
    }

    public int getValue2() {
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
        final IntTuple2 other = (IntTuple2) obj;
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

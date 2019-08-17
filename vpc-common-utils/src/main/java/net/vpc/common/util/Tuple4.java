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
public class Tuple4<T1, T2, T3, T4> {

    private final T1 value1;
    private final T2 value2;
    private final T3 value3;
    private final T4 value4;

    public Tuple4(T1 value1, T2 value2, T3 value3, T4 value4) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public T1 getValue1() {
        return value1;
    }

    public T2 getValue2() {
        return value2;
    }

    public T3 getValue3() {
        return value3;
    }

    public T4 getValue4() {
        return value4;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.value1);
        hash = 23 * hash + Objects.hashCode(this.value2);
        hash = 23 * hash + Objects.hashCode(this.value3);
        hash = 23 * hash + Objects.hashCode(this.value4);
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
        final Tuple4<?, ?, ?, ?> other = (Tuple4<?, ?, ?, ?>) obj;
        if (!Objects.equals(this.value1, other.value1)) {
            return false;
        }
        if (!Objects.equals(this.value2, other.value2)) {
            return false;
        }
        if (!Objects.equals(this.value3, other.value3)) {
            return false;
        }
        if (!Objects.equals(this.value4, other.value4)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tuple{" + "value1=" + value1 + ", value2=" + value2 + ", value3=" + value3 + ", value4=" + value4 + '}';
    }

}

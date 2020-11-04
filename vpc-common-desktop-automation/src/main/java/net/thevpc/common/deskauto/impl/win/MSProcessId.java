/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.win;

import net.thevpc.common.deskauto.ProcessId;

/**
 *
 * @author vpc
 */
public class MSProcessId implements ProcessId {

    private int value;

    public MSProcessId(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MSProcessId other = (MSProcessId) obj;
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

}

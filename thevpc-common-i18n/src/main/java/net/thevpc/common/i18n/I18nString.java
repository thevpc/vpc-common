/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.i18n;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author thevpc
 */
public class I18nString {

    private String id;
    private Object[] args;
    private I18n i18n;

    public I18nString(String id, Object... args) {
        this.id = id;
        this.args = args;
    }

    public String getId() {
        return id;
    }

    public Object[] getArgs() {
        return args;
    }

    public I18n getI18n() {
        return i18n;
    }

    public void setI18n(I18n i18n) {
        this.i18n = i18n;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Arrays.deepHashCode(this.args);
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
        final I18nString other = (I18nString) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Arrays.deepEquals(this.args, other.args)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (i18n != null) {
            return i18n.getString(id);
        }
        return id;
    }

}

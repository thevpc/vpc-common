/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.win;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import java.util.Objects;
import net.thevpc.common.deskauto.DEWindowId;

/**
 *
 * @author thevpc
 */
public class MSWinDEWindowId implements DEWindowId {

    private HWND value;

    public MSWinDEWindowId(HWND value) {
        this.value = value;
    }

    public long getLongValue() {
        return this.value == null ? 0 : Pointer.nativeValue(this.value.getPointer());
    }

    @Override
    public String toString() {
        return String.valueOf(getLongValue());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(getLongValue());
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
        final MSWinDEWindowId other = (MSWinDEWindowId) obj;
        if (!Objects.equals(getLongValue(), other.getLongValue())) {
            return false;
        }
        return true;
    }

}

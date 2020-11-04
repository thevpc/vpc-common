/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.prs;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 4 sept. 2007 17:54:26
 */
public class ObjectHolder<T> {

    public ObjectHolder() {
    }

    public ObjectHolder(T value) {
        this.value = value;
    }

    private T value;
    public T get(){
        return value;
    }
    public void set(T t){
        value=t;
    }
}

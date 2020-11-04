/**
 * ====================================================================
 *                        vpc-commons library
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
package net.thevpc.common.swings.util;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public class StringStartsWithFilter implements StringFilter {
    String pattern;
    boolean caseSensitive;
    public StringStartsWithFilter(String pattern,boolean caseSensitive) {
        this.pattern = caseSensitive?pattern:pattern.toLowerCase();
        this.caseSensitive = caseSensitive;
    }


    public boolean accept(String value) {
        if (value == null) {
            value = "";
        }
        String input = value;
        if(caseSensitive){
            return input.startsWith(pattern);
        }else{
            return input.toLowerCase().startsWith(pattern);
        }
    }
}

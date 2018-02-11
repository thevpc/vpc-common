/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.vpc.common.prs.messageset;

import java.util.EventObject;
import java.util.Locale;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 août 2007 22:29:55
 */
public class MessageSetChangeEvent extends EventObject {
    private Locale oldLocale;
    private Locale newLocale;
    public MessageSetChangeEvent(Object source) {
        super(source);
    }

    public MessageSetChangeEvent(Object source, Locale oldLocale, Locale newLocale) {
        super(source);
        this.oldLocale = oldLocale;
        this.newLocale = newLocale;
    }

    public boolean isLocaleChange(){
        return oldLocale!=newLocale;
    }

    public Locale getOldLocale() {
        return oldLocale;
    }

    public Locale getNewLocale() {
        return newLocale;
    }
}

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

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 28 avr. 2007 21:07:13
 */
public abstract class MessageSetBundleAdapter extends AbstractMessageSetBundle{

    public abstract MessageSetBundle getMessageSetBundle();

    public String getString/*NoCache*/(String key) throws MissingResourceException {
        return getMessageSetBundle().getString(key);
    }

    public Locale getLocale() {
        return getMessageSetBundle().getLocale();
    }

    public void setLocale(Locale locale) {
        //do nothing
    }
}

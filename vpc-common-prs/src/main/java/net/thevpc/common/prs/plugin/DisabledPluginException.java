/**
 * ====================================================================
 *             Pluggable Resources Set
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 *
 * Copyright (C) 2006-2007 Taha BEN SALAH
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

package net.thevpc.common.prs.plugin;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 dec. 2006 17:24:52
 */
public class DisabledPluginException extends PluginException{

    public DisabledPluginException(String plugin) {
        super(plugin,"Plugin "+plugin+" is Disabled");
    }

    public DisabledPluginException(String plugin,String message) {
        super(plugin,message);
    }

    public DisabledPluginException(String plugin,String message, Throwable cause) {
        super(plugin,message, cause);
    }

    public DisabledPluginException(String plugin,Throwable cause) {
        super(plugin,cause);
    }
}
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

package net.vpc.common.prs.plugin;

import java.net.URL;
import java.util.Iterator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 22 sept. 2007 21:36:41
 */
public interface PluginRepository extends Comparable<PluginRepository> {
    public static final int UNREACHABLE = -2;
    public static final int UNKNOWN = -1;

    URL getURL();

    int getLoad();

    public PluginDescriptor getPluginDescriptor(String id);

    /**
     * available plugins count in the PluginRepository
     *
     * @return available plugins count
     */
    public int size();

    public Iterator<PluginDescriptor> getPluginDescriptors();

    public void refresh();
}

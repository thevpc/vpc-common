/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/

package net.thevpc.common.prs.plugin;

import java.net.URL;
import java.util.Iterator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 22 sept. 2007 21:36:41
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

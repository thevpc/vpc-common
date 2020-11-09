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

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 dec. 2006 17:25:17
 */
public class InvalidPluginException extends PluginException{

    public InvalidPluginException(String plugin) {
        super(plugin,"Invalid Plugin "+plugin);
    }

    public InvalidPluginException(String plugin,String message) {
        super(plugin,message);
    }

    public InvalidPluginException(String plugin,String message, Throwable cause) {
        super(plugin,message, cause);
    }

    public InvalidPluginException(String plugin,Throwable cause) {
        super(plugin,cause);
    }
}

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

package net.thevpc.common.swing.plaf;

import javax.swing.*;

/**
 *
 * @author thevpc
 */
public interface PlafHandler {
    public PlafItem[] loadItems();
    public int accept(String plaf);
    public void install();
    public PlafItem getPlafItem(String item);
    public void apply(PlafItem plafItem) throws ClassNotFoundException, 
               InstantiationException, 
               IllegalAccessException,
               UnsupportedLookAndFeelException ;
}

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

package net.thevpc.common.swings.iconset;


import javax.swing.*;

import net.thevpc.common.prs.iconset.IconSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 26 juin 2006 08:44:38
 */
public class JTabbedPaneIconSetUpdater implements ComponentIconSetUpdater {
    public void updateIconSet(JComponent component, String id, IconSet iconSet) {
        JTabbedPane comp = (JTabbedPane) component;
        for(int i=0;i<comp.getTabCount();i++){
            String index="["+i+"]";
            comp.setIconAt(i,iconSet.getIconW(id+index));
            comp.setDisabledIconAt(i,iconSet.getIconS(id+index+".disabled"));
        }
    }

    public void install(JComponent comp, String id) {
        //
    }
}
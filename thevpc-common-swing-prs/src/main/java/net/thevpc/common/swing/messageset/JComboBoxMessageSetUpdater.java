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

package net.thevpc.common.swing.messageset;


import javax.swing.*;

import net.thevpc.common.prs.messageset.MessageSet;
import net.thevpc.common.swing.prs.LocalizedByValueComboboxRenderer;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  27 juin 2006 00:38:27
 */
public class JComboBoxMessageSetUpdater implements ComponentMessageSetUpdater {
    public void updateMessageSet(JComponent component, String id, MessageSet messageSet) {
        JComboBox combo = (JComboBox) component;
        if (combo.getRenderer() instanceof LocalizedByValueComboboxRenderer) {
            ((LocalizedByValueComboboxRenderer) combo.getRenderer()).setMessageSet(messageSet);
        }
    }

    public void install(JComponent comp, String id) {
    }

}

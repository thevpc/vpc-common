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

package net.thevpc.common.swings.messageset;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import net.thevpc.common.prs.messageset.MessageSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  27 juin 2006 00:25:16
 */
public class JToggleButtonMessageSetUpdater extends AbstractButtonMessageSetUpdater {
    private ItemListener toggleButtonItemListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            AbstractButton component = (AbstractButton) e.getSource();
            MessageSet r = (MessageSet) component.getClientProperty("JToggleButtonMessageSetUpdater.MessageSet");
            if (r != null) {
                updateMessageSet(component, (String) component.getClientProperty(SwingMessageSetManager.PROP_ID), r);
            }
        }
    };

    public void updateMessageSet(JComponent component, String id, MessageSet messageSet) {
        super.updateMessageSet(component, id, messageSet);
        component.putClientProperty("JToggleButtonMessageSetUpdater.MessageSet", messageSet);
    }

    public void install(JComponent comp, String id) {
        ((JToggleButton) comp).addItemListener(toggleButtonItemListener);
    }
}

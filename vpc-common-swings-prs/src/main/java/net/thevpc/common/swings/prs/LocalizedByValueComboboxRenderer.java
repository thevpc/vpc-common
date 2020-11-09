/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
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
package net.thevpc.common.swings.prs;


import net.thevpc.common.prs.messageset.MessageSet;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 12 aout 2005
 */
public class LocalizedByValueComboboxRenderer extends DefaultListCellRenderer {
    private MessageSet messageSet;
    private JComboBox combo;

    public LocalizedByValueComboboxRenderer(JComboBox combo, MessageSet messageSet) {
        this.combo = combo;
        this.messageSet = messageSet;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String valuesKey = (String) combo.getClientProperty("ValuesKey");
        if (valuesKey == null) {
            valuesKey = combo.getName();
        }
        String str = messageSet == null ? String.valueOf(value) : messageSet.get(valuesKey + "." + value);
        return super.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);
    }

    public MessageSet getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(MessageSet messageSet) {
        this.messageSet = messageSet;
    }
}

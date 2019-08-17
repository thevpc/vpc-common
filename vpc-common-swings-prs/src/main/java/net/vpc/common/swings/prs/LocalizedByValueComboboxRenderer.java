/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
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
package net.vpc.common.swings.prs;


import net.vpc.common.prs.messageset.MessageSet;

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

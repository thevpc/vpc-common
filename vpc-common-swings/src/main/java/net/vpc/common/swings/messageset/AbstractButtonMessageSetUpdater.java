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

package net.vpc.common.swings.messageset;

import net.vpc.common.swings.PRSManager;

import javax.swing.*;
import net.vpc.common.prs.messageset.MessageSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  27 juin 2006 00:15:35
 */
public class AbstractButtonMessageSetUpdater implements ComponentMessageSetUpdater {

    public void updateMessageSet(JComponent component, String id, MessageSet messageSet) {
        AbstractButton comp = (AbstractButton) component;
        Action a = comp.getAction();
//        if(comp.getIcon()==null && preferIconOnly){
//            preferIconOnly=false;
//        }
        String text = messageSet.get(id, true);
        if (a != null && SwingMessageSetManager.isMessageSetSupported(a)) {
//            if (preferIconOnly /*&& icon != null*/ && !(comp instanceof JMenuItem)) {
//                comp.setText("");
//            }
            //dont update
            if (PRSManager.isShortNamePreferred(comp)) {
                String shortName = messageSet.get(id + ".shortName", null, false);
                if (shortName != null) {
                    comp.setText(shortName);
                    comp.putClientProperty("noIconText", shortName);
                }
                comp.putClientProperty("shortName", shortName == null ? text : shortName);
            }
            return;
        }
        String tooltip = messageSet.get(id + ".toolTipText", null, false);
        //String actionName=preferIcoOnly ?(icon == null ? text : null):text;
        String actionTooltip = tooltip == null ? text : tooltip;
        if (a != null) {
            a.putValue(Action.NAME, text);
            a.putValue("noIconText", text);
            a.putValue(Action.SHORT_DESCRIPTION, actionTooltip);
        } else {
            comp.setText(text);
            comp.setToolTipText(tooltip == null ? text : tooltip);
        }
        comp.putClientProperty("noIconText", text);
        if (comp.isSelected()) {
            String tooltip2 = messageSet.get(id + ".selectedToolTipText", null, false);
            String selectedText = messageSet.get(id + ".selectedText", null, false);
            if (tooltip2 != null) {
                comp.setToolTipText(tooltip2);
            }
            if (selectedText != null) {
                comp.setText(selectedText);
                comp.putClientProperty("noIconText", selectedText);
            }
        }
        if (PRSManager.isShortNamePreferred(comp)) {
            String shortName = messageSet.get(id + ".shortName", null, false);
            if (shortName != null) {
                comp.setText(shortName);
                comp.putClientProperty("noIconText", shortName);
            }
            comp.putClientProperty("shortName", shortName == null ? text : shortName);
        }
    }

    public void install(JComponent comp, String id) {
    //
    }
}

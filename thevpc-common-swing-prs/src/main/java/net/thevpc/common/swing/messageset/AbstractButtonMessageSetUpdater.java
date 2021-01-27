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
import net.thevpc.common.swing.prs.PRSManager;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  27 juin 2006 00:15:35
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

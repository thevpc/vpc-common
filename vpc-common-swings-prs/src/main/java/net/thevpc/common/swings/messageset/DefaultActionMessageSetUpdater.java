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

import net.thevpc.common.prs.messageset.MessageSet;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  27 juin 2006 00:01:02
 */
public class DefaultActionMessageSetUpdater implements ActionMessageSetUpdater {
    public void updateResources(Action action, String id, MessageSet messageSet) {
        String text = messageSet.get(id, true);
        String tooltip = messageSet.get(id + ".toolTipText", null, false);
        String shortName = messageSet.get(id + ".shortName", null, false);
        String actionTooltip = tooltip == null ? text : tooltip;
        action.putValue(Action.NAME, text);
        action.putValue(Action.SHORT_DESCRIPTION, actionTooltip);
        action.putValue("noIconText", text);
        action.putValue("shortName", shortName==null?text:shortName);
//        a.putValue(Action.SMALL_ICON, icon);

    }
}

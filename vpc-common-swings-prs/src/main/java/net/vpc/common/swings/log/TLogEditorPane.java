/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.swings.log;

import javax.swing.JComponent;
import net.vpc.common.prs.iconset.IconSet;
import net.vpc.common.prs.log.TLog;
import net.vpc.common.prs.messageset.MessageSet;
import net.vpc.common.swings.prs.ComponentResourcesUpdater;
import net.vpc.common.swings.prs.PRSManager;

/**
 *
 * @author vpc
 */
public class TLogEditorPane extends LogEditorPane implements TLog{

    public TLogEditorPane() {
        PRSManager.addSupport(this, "LogTextArea", new ComponentResourcesUpdater() {
            public void update(JComponent comp, String id, MessageSet messageSet, IconSet iconSet) {
                PRSManager.update(support.getActions(), messageSet, iconSet);
            }
        });
    }
    
}

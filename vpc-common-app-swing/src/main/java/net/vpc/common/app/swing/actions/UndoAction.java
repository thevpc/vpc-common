/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app.swing.actions;

import java.awt.event.ActionEvent;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.Application;

/**
 *
 * @author vpc
 */
public class UndoAction extends AbstractAppAction {

    public UndoAction(Application aplctn) {
        super(aplctn, "Undo");
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        getApplication().history().undoAction();
    }
    @Override
    public void refresh() {

    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.app.swing.actions;

import java.awt.event.ActionEvent;

import net.thevpc.common.app.AbstractAppAction;
import net.thevpc.common.app.Application;

/**
 *
 * @author thevpc
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

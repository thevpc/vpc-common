/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.app.swing.actions;

import java.awt.event.ActionEvent;

import net.thevpc.common.app.AbstractAppAction;
import net.thevpc.common.app.Application;
import net.thevpc.swings.plaf.UIPlafManager;

/**
 *
 * @author vpc
 */
public class PlafAction extends AbstractAppAction {

    private String plaf;

    public PlafAction(Application aplctn, String plaf) {
        super(aplctn, "Plaf_" + plaf);
        this.plaf = plaf;
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        UIPlafManager.INSTANCE.apply(plaf);
    }
    @Override
    public void refresh() {

    }

}

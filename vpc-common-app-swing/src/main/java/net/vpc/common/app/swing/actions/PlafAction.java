/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app.swing.actions;

import java.awt.event.ActionEvent;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.Application;
import net.vpc.swings.plaf.UIPlafManager;

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.app.swing.actions;

import net.thevpc.common.app.AbstractAppAction;
import net.thevpc.common.app.Application;
import net.thevpc.common.swings.win.InternalWindowsHelper;

import java.awt.event.ActionEvent;

/**
 *
 * @author thevpc
 */
public class TileWindows extends AbstractAppAction {
    private InternalWindowsHelper wins;
    public TileWindows(Application aplctn, InternalWindowsHelper wins) {
        super(aplctn, "TileWindows");
        this.wins=wins;
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        wins.tileFrames();
    }

}

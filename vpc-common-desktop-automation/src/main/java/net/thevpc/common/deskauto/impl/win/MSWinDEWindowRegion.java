/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.win;

import java.awt.Rectangle;
import net.thevpc.common.deskauto.ContextualRobot;
import net.thevpc.common.deskauto.DEWindow;
import net.thevpc.common.deskauto.DEWindowRegion;
import net.thevpc.common.deskauto.impl.shared.DefaultDEWindowRegionRobot;

/**
 *
 * @author vpc
 */
public class MSWinDEWindowRegion implements DEWindowRegion{
    private MSWinDEWindow win;
    private Rectangle relativeBounds;
    private Rectangle absoluteBounds;
    private DefaultDEWindowRegionRobot robot;

    public MSWinDEWindowRegion(MSWinDEWindow win, Rectangle relativeBounds,Rectangle absoluteBounds) {
        this.win = win;
        this.relativeBounds = relativeBounds;
        this.absoluteBounds = absoluteBounds;
    }

    @Override
    public DEWindow getWindow() {
     return win;
    }

    @Override
    public ContextualRobot getRobot() {
        if(robot==null){
            robot = new DefaultDEWindowRegionRobot(this);
        }
        return robot;
    }

    @Override
    public Rectangle getRelativeBounds() {
        return relativeBounds;
    }
    
    @Override
    public Rectangle getAbsoluteBounds() {
        return absoluteBounds;
    }
    
}

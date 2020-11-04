package net.thevpc.common.deskauto.impl.shared;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import net.thevpc.common.deskauto.impl.shared.input.AbstractContextualRobot;
import net.thevpc.common.deskauto.impl.win.MSWinDEWindowRegion;

/**
 *
 * @author vpc
 */
public class DefaultDEWindowRegionRobot extends AbstractContextualRobot {

    private MSWinDEWindowRegion win;

    public DefaultDEWindowRegionRobot(MSWinDEWindowRegion win) {
        this.win = win;
    }

    @Override
    public void keyPress(int vkey) {
        win.getWindow().getRobot().keyPress(vkey);
    }

    @Override
    public void keyRelease(int vkey) {
        win.getWindow().getRobot().keyRelease(vkey);
    }

    @Override
    public void mousePress(int vkey) {
        win.getWindow().getRobot().mousePress(vkey);
    }

    @Override
    public void mouseRelease(int vkey) {
        win.getWindow().getRobot().mouseRelease(vkey);
    }

    @Override
    public void mouseMove(int x, int y) {
        Rectangle b = win.getRelativeBounds();
        win.getWindow().getRobot().mouseMove(b.x + x, b.y + y);
    }

    @Override
    public void delay(int time) {
        win.getWindow().getRobot().delay(time);
    }

    @Override
    public BufferedImage createScreenCapture(Rectangle rect) {
        Rectangle b = win.getRelativeBounds();

        int xmin = b.x + (rect == null ? 0 : rect.x);
        int xmax = b.x + (rect == null ? b.width : (rect.x + rect.width));
        int ymin = b.y + (rect == null ? 0 : rect.y);
        int ymax = b.y + (rect == null ? b.height : (rect.y + rect.height));

        if (xmin < b.x) {
            xmin = b.x;
        }
        if (xmax > (b.x + b.width)) {
            xmax = b.x + b.width;
        }

        if (ymin < b.y) {
            ymin = b.y;
        }
        if (ymax > (b.y + b.height)) {
            ymax = b.y + b.height;
        }

        Rectangle rect2 = new Rectangle(xmin, ymin, xmax - xmin, ymax - ymin);
        return win.getWindow().getRobot().createScreenCapture(rect2);
    }

}

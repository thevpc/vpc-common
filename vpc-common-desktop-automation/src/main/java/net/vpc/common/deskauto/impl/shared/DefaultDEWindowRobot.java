/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.deskauto.impl.shared;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import net.vpc.common.deskauto.impl.shared.input.AbstractContextualRobot;
import net.vpc.common.deskauto.impl.win.MSWinDEWindow;

/**
 *
 * @author vpc
 */
public class DefaultDEWindowRobot extends AbstractContextualRobot {

    private MSWinDEWindow win;

    public DefaultDEWindowRobot(MSWinDEWindow win) {
        this.win = win;
    }

    @Override
    public void keyPress(int vkey) {
        win.moveToFront();
        win.getWindowManager().getRobot().keyPress(vkey);
    }

    @Override
    public void keyRelease(int vkey) {
        win.moveToFront();
        win.getWindowManager().getRobot().keyRelease(vkey);
    }

    @Override
    public void mousePress(int vkey) {
        win.moveToFront();
        win.getWindowManager().getRobot().mousePress(vkey);
    }

    @Override
    public void mouseRelease(int vkey) {
        win.moveToFront();
        win.getWindowManager().getRobot().mouseRelease(vkey);
    }

    @Override
    public void mouseMove(int x, int y) {
        win.moveToFront();
        Rectangle b = win.getBounds();
        win.getWindowManager().getRobot().mouseMove(b.x + x, b.y + y);
    }

    @Override
    public void delay(int time) {
        win.moveToFront();
        win.getWindowManager().getRobot().delay(time);
    }

    @Override
    public BufferedImage createScreenCapture(Rectangle rect) {
        win.moveToFront();
        Rectangle b = win.getBounds();

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
        return win.getWindowManager().getRobot().createScreenCapture(rect2);
    }

}

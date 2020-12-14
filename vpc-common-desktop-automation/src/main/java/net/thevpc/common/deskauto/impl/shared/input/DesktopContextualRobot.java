/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.shared.input;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thevpc
 */
public class DesktopContextualRobot extends AbstractContextualRobot {

    private static Robot robot = null;

    public DesktopContextualRobot() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(DesktopContextualRobot.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException(ex);
        }
    }

    public void keyPress(int vkey) {
        System.out.println("keyPress "+KeyEvent.getKeyText(vkey));
        robot.keyPress(vkey);
    }

    public void keyRelease(int vkey) {
        System.out.println("keyRelease "+KeyEvent.getKeyText(vkey));
        robot.keyRelease(vkey);
    }

    public void mousePress(int vkey) {
        System.out.println("mousePress "+KeyEvent.getKeyText(vkey));
        robot.mousePress(vkey);
    }

    public void mouseRelease(int vkey) {
        System.out.println("mouseRelease "+KeyEvent.getKeyText(vkey));
        robot.mouseRelease(vkey);
    }

    public void mouseMove(int x, int y) {
        System.out.println("mouseMove "+x+" , "+y);
        robot.mouseMove(x, y);
    }

    public void delay(int time) {
        System.out.println("delay "+time);
        robot.delay(time);
    }

    @Override
    public BufferedImage createScreenCapture(Rectangle rect) {
        System.out.println("createScreenCapture "+rect);
        return robot.createScreenCapture(rect);
    }

}

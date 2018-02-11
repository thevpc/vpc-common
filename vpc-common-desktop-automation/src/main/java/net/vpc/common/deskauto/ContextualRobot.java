/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.deskauto;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author vpc
 */
public interface ContextualRobot {

    public void keyPress(int vkey);

    public void keyRelease(int vkey);

    public void mousePress(int vkey);

    public void mouseRelease(int vkey);

    public void mouseMove(int x, int y);

    public void delay(int time);

    public void think();

    public void click(int button);

    public void dblClick(int button);

    public void click(Point point, int button);

    public void dblClick(Point point, int button);

    public void leftClick();

    public void leftDblClick();

    public void rightClick();

    public void rightDblClick();

    /**
     * example hello world ${}
     *
     * @param cmd string
     */
    public void sendCommand(String cmd);

    public void keyType(int vkey);

    public BufferedImage createScreenCapture(Rectangle rect);

}

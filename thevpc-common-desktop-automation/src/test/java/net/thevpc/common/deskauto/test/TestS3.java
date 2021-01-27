/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.test;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import net.thevpc.common.deskauto.DEWindow;
import net.thevpc.common.deskauto.DEWindowFilterFactory;
import net.thevpc.common.deskauto.DesktopEnvironment;
import net.thevpc.common.strings.StringComparators;

/**
 *
 * @author thevpc
 */
public class TestS3 {

    public static void main(String[] args) {
        try {
            DesktopEnvironment m = DesktopEnvironment.getInstance();
        for (DEWindow w : m.findWindows(DEWindowFilterFactory.visible())) {
            display(w);
        }
            DEWindow w = m.findSingleWindow(DEWindowFilterFactory.module(StringComparators.ilike("*Explorer.EXE*")));
            System.out.println("window bounds ="+w.getBounds());
            System.out.println("client bounds ="+w.getClientRegion().getRelativeBounds());
            System.out.println("title bounds ="+w.getTitleRegion().getRelativeBounds());
            ImageIO.write(w.getRobot().createScreenCapture(null), "png", new File("c:/work/a.png"));
            ImageIO.write(w.getClientRegion().getRobot().createScreenCapture(null), "png", new File("c:/work/b.png"));
            ImageIO.write(w.getTitleRegion().getRobot().createScreenCapture(null), "png", new File("c:/work/c.png"));
            w.getRobot().sendCommand("alt ; 'oo' ; move 200 200 click");
//        w.moveToFront();
//            w.getRobot().sendCommand("move 100 100; lclick");
//            display(w);
        } catch (IOException ex) {
            Logger.getLogger(TestS3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void display(DEWindow w) {
        String modifiers = (w.isRegular() ? "R" : "-") + (w.isSpecial() ? "S" : "-") + (w.isTool() ? "T" : "-") + (w.isChild()? "C" : "-")+ (w.isVisible() ? "V" : "-");
        System.out.println(w.getId() + " " + modifiers + ":: '" + w.getTitle() + "' :: " + w.getBounds());
        System.out.println("\t" + w.getProcessId() + " : " + w.getModuleName());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.prs;

import java.awt.Dimension;
import javax.swing.ImageIcon;

import net.thevpc.common.prs.util.ProgressEvent;
import net.thevpc.common.prs.util.ProgressMonitor;
import net.thevpc.common.swing.splash.JSplashScreen;

/**
 *
 * @author thevpc
 */
public class PRSSplashScreen extends JSplashScreen implements ProgressMonitor {

    public PRSSplashScreen(ImageIcon image, Dimension preferredDimension) {
        super(image, preferredDimension);
    }

    public void progressStart(ProgressEvent e) {
        progressStart(e.getMessage());
    }

    public void progressUpdate(ProgressEvent e) {
        progressUpdate(e.getMessage());
    }

    public void progressEnd(ProgressEvent e) {
        progressEnd(e.getMessage());
    }

}

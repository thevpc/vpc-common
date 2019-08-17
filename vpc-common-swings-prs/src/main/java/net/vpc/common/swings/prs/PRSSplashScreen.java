/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.swings.prs;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import net.vpc.common.prs.util.ProgressEvent;
import net.vpc.common.swings.SimpleSplashScreen;

/**
 *
 * @author vpc
 */
public class PRSSplashScreen extends SimpleSplashScreen implements net.vpc.common.prs.util.ProgressMonitor {

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

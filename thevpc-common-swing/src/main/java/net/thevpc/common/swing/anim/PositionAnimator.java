/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.anim;

import java.awt.Dimension;
import java.awt.Point;

/**
 *
 * @author thevpc
 */
public interface PositionAnimator {

    void start(Dimension canvasSize, long time);

    Point nextStep(long time, Dimension size);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.anim;

import java.awt.Dimension;

/**
 *
 * @author vpc
 */
public interface AnimPoint {

    public static AnimPoint of(int dx, int dy) {
        return of(dx, dy,0,0,0,0);
    }
    
    public static AnimPoint of(int dx, int dy, float cx, float cy, float ix, float iy) {
        return new DefaultPointExt(dx, dy, cx, cy, ix, iy);
    }

    int getX(Dimension itemSize, Dimension canvasSize);

    int getY(Dimension itemSize, Dimension canvasSize);
}

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
public class DefaultPointExt implements AnimPoint {

    private float ix;
    private float iy;
    private float cx;
    private float cy;
    private int dx;
    private int dy;

    public DefaultPointExt(int dx, int dy, float cx, float cy, float ix, float iy) {
        this.ix = ix;
        this.iy = iy;
        this.cx = cx;
        this.cy = cy;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int getX(Dimension itemSize, Dimension canvasSize) {
        return (int) (ix * itemSize.width + cx * canvasSize.width) + dx;
    }

    @Override
    public int getY(Dimension itemSize, Dimension canvasSize) {
        return (int) (iy * itemSize.width + cy * canvasSize.width) + dy;
    }

}

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
public class LinearAnimator implements PositionAnimator {

    AnimPoint from;
    AnimPoint to;
    Dimension canvasSize;
    long time;
    long startTime;
    long period;

    public LinearAnimator(AnimPoint from, AnimPoint to, long period) {
        this.from = from;
        this.to = to;
        this.period = period;
    }

    @Override
    public void start(Dimension canvasSize, long startTime) {
        this.canvasSize = canvasSize;
        this.startTime = startTime;
    }

    @Override
    public Point nextStep(long time, Dimension itemSize) {
        time = time - startTime;
        if (time < 0) {
            time = 0;
        } else if (time >= period) {
            time = period;
        }
        double a = 1.0 * time / period;
        return nextLinePoint((float) a, itemSize);
    }

    public Point nextLinePoint(float position, Dimension itemSize) {
        int xmin = from.getX(itemSize, canvasSize);
        int xmax = to.getX(itemSize, canvasSize);
        int ymin = from.getY(itemSize, canvasSize);
        int ymax = to.getY(itemSize, canvasSize);

        if (position <= 0.0) {
            return new Point(xmin, ymin);
        } else if (position > 1.0) {
            return new Point((int) xmax, (int) ymax);
        }
        double sx = xmin;
        double sy = ymin;
        double tx = xmax;
        double ty = ymax;
        double nx = (tx - sx) * position + sx;
        double ny = (ty - sy) * position + sy;
        return new Point((int) nx, (int) ny);
    }

}

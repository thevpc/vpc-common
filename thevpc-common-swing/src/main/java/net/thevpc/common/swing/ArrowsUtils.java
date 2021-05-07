/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 *
 * @author vpc
 */
public class ArrowsUtils {

    public static enum ArrowType {
        NONE,
        SIMPLE,
        INHERITS,
        AGGREGATION,
        COMPOSITION,
    }

    private static enum InternalArrowType {
        NONE,
        TRIANGLE,
        V,
        DIAMOND
    }
    //https://gist.github.com/raydac/df97493f58b0521fb20a

    public static ArrowType parseArrowTypeLenient(String s) {
        if (s == null || s.trim().isEmpty()) {
            return ArrowType.NONE;
        }
        try {
            return ArrowType.valueOf(s.toUpperCase());
        } catch (Exception ex) {
            return ArrowType.NONE;
        }
    }

    public static void drawArrow(final Graphics2D gfx, final Point2D start, final Point2D end, final Stroke lineStroke,
            final Stroke arrowStroke,
            final float arrowSize,
            ArrowType startArrowType,
            ArrowType endArrowType
    ) {
        if (startArrowType == null) {
            startArrowType = ArrowType.NONE;
        }
        if (endArrowType == null) {
            endArrowType = ArrowType.NONE;
        }

        final double startx = start.getX();
        final double starty = start.getY();
        gfx.setStroke(arrowStroke);
        final double deltax = startx - end.getX();
        final double result;
        if (deltax == 0.0d) {
            result = Math.PI / 2;
        } else {
            result = Math.atan((starty - end.getY()) / deltax) + (startx < end.getX() ? Math.PI : 0);
        }
        double scx = 0;
        double scy = 0;
        double ecx = 0;
        double ecy = 0;
        InternalArrowType istartArrowType = InternalArrowType.NONE;
        InternalArrowType iendArrowType = InternalArrowType.NONE;
        //start
        {
            final double angle = result;
            boolean outline = false;

            double arrowAngle = Math.PI / 12.0d;
            switch (startArrowType) {
                case NONE: {
                    istartArrowType = InternalArrowType.NONE;
                    break;
                }
                case INHERITS: {
                    istartArrowType = InternalArrowType.TRIANGLE;
                    outline = true;
                    arrowAngle = Math.PI / 8.0d;
                    break;
                }
                case SIMPLE: {
                    istartArrowType = InternalArrowType.TRIANGLE;
                    outline = false;
                    arrowAngle = Math.PI / 12.0d;
                    break;
                }
                case AGGREGATION: {
                    istartArrowType = InternalArrowType.DIAMOND;
                    outline = true;
                    arrowAngle = Math.PI / 8.0d;
                    break;
                }
                case COMPOSITION: {
                    istartArrowType = InternalArrowType.DIAMOND;
                    outline = false;
                    arrowAngle = Math.PI / 8.0d;
                    break;
                }
                default: {
                    istartArrowType = InternalArrowType.TRIANGLE;
                    outline = false;
                    arrowAngle = Math.PI / 12.0d;
                }
            }

            final double x1 = arrowSize * Math.cos(angle - arrowAngle);
            final double y1 = arrowSize * Math.sin(angle - arrowAngle);
            final double x2 = arrowSize * Math.cos(angle + arrowAngle);
            final double y2 = arrowSize * Math.sin(angle + arrowAngle);

            final double cx = (arrowSize / 2.0f) * Math.cos(angle);
            final double cy = (arrowSize / 2.0f) * Math.sin(angle);
            switch (istartArrowType) {
                case NONE: {
                    break;
                }
                case TRIANGLE: {
                    scx = -2 * cx;
                    scy = -2 * cy;
                    final GeneralPath polygon = new GeneralPath();
                    polygon.moveTo(start.getX(), start.getY());
                    polygon.lineTo(start.getX() - x1, start.getY() - y1);
                    polygon.lineTo(start.getX() - x2, start.getY() - y2);
                    polygon.closePath();
                    if (outline) {
                        gfx.draw(polygon);
                    } else {
                        gfx.fill(polygon);
                    }
                    break;
                }
                case DIAMOND: {
                    scx = -2 * cx;
                    scy = -2 * cy;
                    final GeneralPath polygon = new GeneralPath();
                    polygon.moveTo(start.getX(), start.getY());
                    polygon.lineTo(start.getX() - x1, start.getY() - y1);
                    polygon.lineTo(start.getX() - x2, start.getY() - y2);
                    polygon.lineTo(start.getX() - scx, start.getY() - scy);
                    polygon.closePath();
                    if (outline) {
                        gfx.draw(polygon);
                    } else {
                        gfx.fill(polygon);
                    }
                    break;
                }
            }
        }
        //end
        {
            final double angle = result;
            boolean outline = false;

            double arrowAngle = Math.PI / 8.0d;
            switch (endArrowType) {
                case NONE: {
                    iendArrowType = InternalArrowType.NONE;
                    break;
                }
                case INHERITS: {
                    iendArrowType = InternalArrowType.TRIANGLE;
                    outline = true;
                    arrowAngle = Math.PI / 8.0d;
                    break;
                }
                case SIMPLE: {
                    iendArrowType = InternalArrowType.TRIANGLE;
                    outline = false;
                    arrowAngle = Math.PI / 12.0d;
                    break;
                }
                case AGGREGATION: {
                    iendArrowType = InternalArrowType.DIAMOND;
                    outline = true;
                    arrowAngle = Math.PI / 8.0d;
                    break;
                }
                case COMPOSITION: {
                    iendArrowType = InternalArrowType.DIAMOND;
                    outline = false;
                    arrowAngle = Math.PI / 8.0d;
                    break;
                }
                default: {
                    iendArrowType = InternalArrowType.TRIANGLE;
                    outline = false;
                    arrowAngle = Math.PI / 12.0d;
                }
            }

            final double x1 = arrowSize * Math.cos(angle - arrowAngle);
            final double y1 = arrowSize * Math.sin(angle - arrowAngle);
            final double x2 = arrowSize * Math.cos(angle + arrowAngle);
            final double y2 = arrowSize * Math.sin(angle + arrowAngle);

            final double cx = (arrowSize / 2.0f) * Math.cos(angle);
            final double cy = (arrowSize / 2.0f) * Math.sin(angle);
            switch (iendArrowType) {
                case NONE: {
                    break;
                }
                case TRIANGLE: {
                    ecx = 2 * cx;
                    ecy = 2 * cy;
                    final GeneralPath polygon = new GeneralPath();
                    polygon.moveTo(end.getX(), end.getY());
                    polygon.lineTo(end.getX() + x1, end.getY() + y1);
                    polygon.lineTo(end.getX() + x2, end.getY() + y2);
                    polygon.closePath();
                    if (outline) {
                        gfx.draw(polygon);
                    } else {
                        gfx.fill(polygon);
                    }
                    break;
                }
                case DIAMOND: {
                    ecx = 2 * cx;
                    ecy = 2 * cy;
                    final GeneralPath polygon = new GeneralPath();
                    polygon.moveTo(end.getX(), end.getY());
                    polygon.lineTo(end.getX() + x1, end.getY() + y1);
                    polygon.lineTo(end.getX() + ecx, end.getY() + ecy);
                    polygon.closePath();
                    if (outline) {
                        gfx.draw(polygon);
                    } else {
                        gfx.fill(polygon);
                    }
                    break;
                }
            }
            gfx.setStroke(lineStroke);
            gfx.drawLine((int) (startx + scx), (int) (starty + scy), (int) (end.getX() + ecx), (int) (end.getY() + ecy));
        }
    }
}

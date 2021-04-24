/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset.util;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author vpc
 */
public class SVGSalamander {

    public static Image getImageFromSvg(URL url, int width0) {
        try {

            SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(url.toURI());
            float dw = diagram.getWidth();
            float dh = diagram.getHeight();
            double wscale = 1;
            double hscale = 1;
            if (width0 > 0 && width0 != dw) {
                wscale = width0 / dw;
                hscale = wscale;
                dh = (float) (dh * wscale);
                dw = width0;
            }
            BufferedImage image = new BufferedImage((int) dw, (int) dh, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = image.createGraphics();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                if (wscale != 1 || hscale != 1) {
                    g.scale(wscale, hscale);
                }
                diagram.setIgnoringClipHeuristic(true);
                diagram.render(g);
            } finally {
                g.dispose();
            }
            return image;

        } catch (SVGException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

}

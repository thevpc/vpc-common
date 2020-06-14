/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.iconset;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author vpc
 */
public class ResourcesIconSet implements IconSet {

    private final String id;
    private final int width;
    private final int height;
    private final String path;
    private final String type;
    private final ClassLoader classLoader;

    public ResourcesIconSet(String id, int width, int height, String path, String type, ClassLoader classLoader) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.path = path;
        this.classLoader = classLoader;
        this.type = type;
    }

    @Override
    public Dimension getSize() {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return new Dimension(width, height);
    }

    @Override
    public String getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getPath() {
        return path;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public ImageIcon getIcon(String id) {
        String p = path;
        if (p.startsWith("/")) {
            p = p.substring(1);
        }
        if (!p.endsWith("/")) {
            p += "/";
        }
        p += id + "." + type;
        URL u = null;
        if (classLoader == null) {
            u = ClassLoader.getSystemResource(p);
        } else {
            u = classLoader.getResource(p);
        }
        if (u != null) {
            return getScaledImageIcon(u, width, height);
        }
        return null;
    }

    static ImageIcon getScaledImageIcon(URL srcImg, int w, int h) {
        Image image = new ImageIcon(srcImg).getImage();
        return new ImageIcon(getScaledImage(image, w, h));
    }

    static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

}

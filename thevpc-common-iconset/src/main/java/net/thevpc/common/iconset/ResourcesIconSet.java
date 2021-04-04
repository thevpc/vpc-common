/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 *
 * @author thevpc
 */
public class ResourcesIconSet implements IconSet {

    private final String id;
    private final int width;
    private final int height;
    private final String path;
    private final String basePath;
    private final String type;
    private final ClassLoader classLoader;
    private final Properties names;

    public ResourcesIconSet(String id, int size, String path, ClassLoader classLoader) {
        this(id, size, size, path, null, classLoader);
    }

    public ResourcesIconSet(String id, int width, int height, String path, String type, ClassLoader classLoader) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.path = path;
        this.classLoader = classLoader;
        StringBuilder p = new StringBuilder(path.trim());
        if (p.charAt(0) == '/') {
            p.delete(0, 1);
        }
        if (p.charAt(p.length() - 1) == '/') {
            p.delete(p.length() - 1, p.length());
        }
        URL namesURL = null;
        if (classLoader == null) {
            namesURL = ClassLoader.getSystemResource(p + "/.iconset.properties");
        } else {
            namesURL = classLoader.getResource(p + "/.iconset.properties");
        }
        names = new Properties();
        if (namesURL != null) {
            try (InputStreamReader r = new InputStreamReader(namesURL.openStream())) {
                names.load(r);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        //.base-url=/net/thevpc/more/iconsets/feather/feather-black
        //.icon-extension=png
        if (names.getProperty(".base-url") != null && names.getProperty(".base-url").trim().length() > 0) {
            p.setLength(0);
            p.append(names.getProperty(".base-url").trim());
            if (p.charAt(0) == '/') {
                p.delete(0, 1);
            }
            if (p.charAt(p.length() - 1) == '/') {
                p.delete(p.length() - 1, p.length());
            }
            this.basePath = p.toString();
        } else {
            this.basePath = p.toString();
        }
        if (names.getProperty(".icon-extension") != null && names.getProperty(".icon-extension").trim().length() > 0) {
            this.type = names.getProperty(".icon-extension").trim();
        } else if (type != null && type.trim().length() > 0) {
            this.type = type.trim();
        } else {
            this.type = "png";
        }
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

    public String getBasePath() {
        return basePath;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public ImageIcon getIcon(String id) {
        if (id == null) {
            return null;
        }
        String id2 = names.getProperty(id);
        if (id2 != null) {
            id = id2;
        }
        StringBuilder p = new StringBuilder(basePath).append('/').append(id).append('.').append(type);
        URL u = null;
        if (classLoader == null) {
            u = ClassLoader.getSystemResource(p.toString());
        } else {
            u = classLoader.getResource(p.toString());
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

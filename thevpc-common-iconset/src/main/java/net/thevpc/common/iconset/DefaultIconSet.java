/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import net.thevpc.common.iconset.util.IconUtils;

/**
 *
 * @author thevpc
 */
public class DefaultIconSet implements IconSet {

    private final String id;
    private final String path;
    private final String basePath;
    private final String type;
    private final ClassLoader classLoader;
    private final Properties names;
    private static final Logger LOG = Logger.getLogger(DefaultIconSet.class.getName());
    private ImageMapCache cache = new ImageMapCache();
    private IconTransform initIconTransform;

    public DefaultIconSet(String id, String path, ClassLoader classLoader) {
        this(id, path, classLoader, null);
    }

    public DefaultIconSet(String id, String path, String type, ClassLoader classLoader) {
        this(id, path, type, classLoader,null);
    }

    public DefaultIconSet(String id, String path, ClassLoader classLoader, IconTransform initIconTransform) {
        this(id, path, null, classLoader, initIconTransform);
    }

    public DefaultIconSet(String id, String path, String type, ClassLoader classLoader, IconTransform initIconTransform) {
        this.id = id;
        this.initIconTransform = initIconTransform;
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
    public String getId() {
        return id;
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

    public ImageIcon getIcon(String id, IconSetConfig size) {
        if (id == null) {
            return null;
        }
        if (size == null) {
            size = IconSetConfig.DEFAULT;
        }
        String id2 = names.getProperty(id);
        if (id2 != null) {
            id = id2;
        }
        ImageSizeCache icon = cache.get(id, size);
        if (icon.icon != null) {
            return icon.icon;
        }
        if (size.equals(IconSetConfig.DEFAULT)) {
            StringBuilder p = new StringBuilder(basePath).append('/').append(id).append('.').append(type);
            URL u = null;
            if (classLoader == null) {
                u = ClassLoader.getSystemResource(p.toString());
            } else {
                u = classLoader.getResource(p.toString());
            }
            if (u != null) {
                ImageIcon i = IconUtils.loadFixedScaleImageIconSafe(u, 128, 128);
                if (initIconTransform != null) {
                    i = new ImageIcon(initIconTransform.transformIcon(i.getImage()));
                }
                icon.icon = i;
                return icon.icon;
            }
            if (!cache.reportedNotFound.contains(id)) {
                cache.reportedNotFound.add(id);
                if (cache.reportedNotFound.size() > cache.maxReports) {
                    for (Iterator<String> it = cache.reportedNotFound.iterator(); it.hasNext();) {
                        it.next();
                        it.remove();
                        break;
                    }
                }
                LOG.log(Level.FINER, "iconset {0}: icon not found: {1}", new Object[]{getId(), id});
            }
            return null;
        } else {
            //check if svg file, as no need to load icon with initial size
            StringBuilder p = new StringBuilder(basePath).append('/').append(id).append('.').append(type);
            URL u = null;
            if (classLoader == null) {
                u = ClassLoader.getSystemResource(p.toString());
            } else {
                u = classLoader.getResource(p.toString());
            }
            if (IconUtils.isSVG(u)) {
                if (u != null) {
                    int w = size.getWidth();
                    int h = size.getHeight();
                    ImageIcon i = null;
                    if (w < 0 && h < 0) {
                        w = 128;
                        h = 128;
                        i = IconUtils.loadFixedScaleImageIconSafe(u, w, h);
                    } else if (h < 0) {
                        i = IconUtils.loadFixedScaleImageIconSafe(u, w, h);
                    } else if (w == h) {
                        i = IconUtils.loadFixedScaleImageIconSafe(u, w, h);
                    } else {
                        i = IconUtils.loadFixedScaleImageIconSafe(u, w, h);
                    }
                    if (initIconTransform != null && i!=null) {
                        i = new ImageIcon(initIconTransform.transformIcon(i.getImage()));
                    }
                    icon.icon = i;
                    return icon.icon;
                }
                if (!cache.reportedNotFound.contains(id)) {
                    cache.reportedNotFound.add(id);
                    if (cache.reportedNotFound.size() > cache.maxReports) {
                        for (Iterator<String> it = cache.reportedNotFound.iterator(); it.hasNext();) {
                            it.next();
                            it.remove();
                            break;
                        }
                    }
                    LOG.log(Level.FINER, "iconset {0}: icon not found: {1}", new Object[]{getId(), id});
                }
                return null;
            } else {

                ImageIcon o = getIcon(id, IconSetConfig.DEFAULT);
                if (o == null) {
                    return null;
                }
                Image i = IconUtils.getFixedSizeImage(o.getImage(), size.getWidth(), size.getHeight());
                if (size.getTransform() != null) {
                    i = size.getTransform().transformIcon(i);
                }
                o = new ImageIcon(i);
                icon.icon = o;
                return o;
            }
        }
    }

    private static class ImageSizeCache {

        private IconSetConfig size;
        private ImageIcon icon;

        public ImageSizeCache(IconSetConfig size) {
            this.size = size;
        }

    }

    private static class ImageSizeMapCache {

        private IconSetConfig size;
        private Map<String, ImageSizeCache> icons = new HashMap<>();

        public ImageSizeMapCache(IconSetConfig size) {
            this.size = size;
        }

        public ImageSizeCache get(String id) {
            ImageSizeCache q = icons.get(id);
            if (q == null) {
                q = new ImageSizeCache(size);
                icons.put(id, q);
            }
            return q;
        }
    }

    private static class ImageMapCache {

        private int maxReports = 2000;
        private Set<String> reportedNotFound = new HashSet<>();

        private Map<IconSetConfig, ImageSizeMapCache> icons = new HashMap<>();

        public ImageSizeCache get(String id, IconSetConfig size) {
            ImageSizeMapCache q = icons.get(size);
            if (q == null) {
                q = new ImageSizeMapCache(size);
                icons.put(size, q);
            }
            return q.get(id);
        }

    }

}

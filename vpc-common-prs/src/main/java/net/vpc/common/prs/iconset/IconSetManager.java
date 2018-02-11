/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.iconset;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.logging.Level;
import javax.swing.Icon;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.vpc.common.prs.log.LoggerProvider;
import net.vpc.common.prs.classloader.PluggableResourcesClassLoader;
import net.vpc.common.prs.plugin.UrlCacheManager;
import net.vpc.common.prs.util.IOUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author vpc
 */
public class IconSetManager {

    private static final String ICON_SET_FACTORY_CHANGED = "ICON_SET_FACTORY_CHANGED";
    private static final String ICON_SET_CHANGED = "ICON_SET_FACTORY_CHANGED";

    private static Map<String, ClassLoader> iconSetImportesClassLoaders = new HashMap<String, ClassLoader>();
    private static IconSetFactory factory;
    private static IconSet defaultIconSet;
    private static PropertyChangeSupport support = new PropertyChangeSupport(IconSetManager.class);

    public static void setClassLoaderForIconSetImports(String importPath, ClassLoader loader) {
        if (loader == null) {
            iconSetImportesClassLoaders.remove(importPath);
        } else {
            iconSetImportesClassLoaders.put(importPath, loader);
        }
    }

    public static ClassLoader getClassLoaderForIconSetImports(String importPath) {
        return iconSetImportesClassLoaders.get(importPath);
    }

    public static void loadAvailableIconSets(URL repository, ClassLoader parent, Object owner, UrlCacheManager urlCacheManager, LoggerProvider loggerProvider) throws IOException {
        ArrayList<URL> repositoryItems = new ArrayList<URL>();
        ArrayList<IconSetDescriptor> all = new ArrayList<IconSetDescriptor>();
        if ("file".equals(repository.getProtocol())) {
            File[] bootIconSets = new File(repository.getFile()).listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.matches(".*\\.jar");
                }
            });
            if (bootIconSets == null) {
                bootIconSets = new File[0];
            }
            Arrays.sort(bootIconSets);
            for (File bootIconSet : bootIconSets) {
                repositoryItems.add(bootIconSet.toURL());
            }
        } else {
            throw new IllegalArgumentException("Unsupported Repository protocol " + repository.getProtocol() + ". please specify folder URL");
        }
        for (URL repositoryItem : repositoryItems) {
            try {
                all.addAll(Arrays.asList(lookupIconsetDescriptors(repositoryItem, parent, owner, urlCacheManager, loggerProvider)));
            } catch (InvalidMessageSetException e) {
                loggerProvider.getLogger(IconSetManager.class.getName()).log(Level.SEVERE, "[IGNORED]  Invalid IconsSet " + repositoryItem, e);
            } catch (MalformedURLException e) {
                loggerProvider.getLogger(IconSetManager.class.getName()).log(Level.SEVERE, "Bad URL", e);
                throw new IllegalArgumentException("bad url", e);
            }
        }
        for (IconSetDescriptor i1 : all) {
            try {
                IconSetManager.registerIconSet(i1, loggerProvider);
            } catch (Exception e) {
                System.err.println(e);
                //
            }
        }
    }

    public static void registerIconSet(URL url, ClassLoader parent, Object owner, UrlCacheManager urlCacheManager, LoggerProvider loggerProvider) throws IOException {
        Throwable e = null;
        for (IconSetDescriptor iconSetDescriptor : lookupIconsetDescriptors(url, parent, owner, urlCacheManager, loggerProvider)) {
            try {
                registerIconSet(iconSetDescriptor, loggerProvider);
            } catch (Throwable e1) {
                loggerProvider.getLogger(IconSetManager.class.getName()).log(Level.WARNING, "Unable to register IconSet " + iconSetDescriptor, e1);
                if (e != null) {
                    e = e1;
                }
            }
        }
        if (e != null) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else if (e instanceof IOException) {
                throw (IOException) e;
            }
        }
    }

    public static IconSetDescriptor[] lookupIconsetDescriptors(URL url, ClassLoader parent, final Object owner, UrlCacheManager urlCacheManager, final LoggerProvider loggerProvider) throws IOException {
        final ArrayList<IconSetDescriptor> descArray = new ArrayList<IconSetDescriptor>();
        URLClassLoader ucl = new URLClassLoader(new URL[]{url}, IconSetManager.class.getClassLoader());
        URL iconsetxml = ucl.getResource("META-INF/iconsets.xml");
        if (iconsetxml == null) {
            iconsetxml = ucl.getResource("meta-inf/iconsets.xml");
        }
        if (iconsetxml == null) {
            throw new NoSuchElementException("missing iconset.xml");
        }
        final PluggableResourcesClassLoader loader = new PluggableResourcesClassLoader(url, parent, IOUtils.getFileNameWithoutExtension(new File(url.getFile())));

        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxFactory.newSAXParser();
            InputStream stream = iconsetxml.openStream();
            saxParser.parse(stream, new DefaultHandler() {

                private String qName = null;
                private String id = null;
                private String name = null;
                private String group = null;
                private String className = null;
                private ArrayList<String> params = null;

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if ("iconset".equals(qName)) {
                        id = attributes.getValue("id");
                        name = attributes.getValue("name");
                        group = attributes.getValue("group");
                        params = new ArrayList<String>();
                        String path = attributes.getValue("path");
                        if (path != null && path.trim().length() > 0) {
                            params.add(path);
                        }
                    }
                    this.qName = qName;
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if ("id".equals(qName)) {
                        id = new String(ch, start, length).trim();
                    } else if ("name".equals(qName)) {
                        name = new String(ch, start, length).trim();
                    } else if ("group".equals(qName)) {
                        group = new String(ch, start, length).trim();
                    } else if ("class".equals(qName)) {
                        className = new String(ch, start, length).trim();
                    } else if ("parameter".equals(qName)) {
                        String p = new String(ch, start, length).trim();
                        params.add(p);
                    } else if ("path".equals(qName)) {
                        String p = new String(ch, start, length).trim();
                        params.add(p);
                    }
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equals("iconset")) {
                        if ((id == null || id.trim().length() == 0) && params.size() > 0) {
                            String p = params.get(0);
                            int d = p.lastIndexOf('.');
                            if (d > 0 && d < (p.length() - 1)) {
                                id = p.substring(d + 1);
                            }
                        }
                        if (className == null || className.trim().length() == 0) {
                            className = "net.vpc.common.prs.iconset.DefaultIconSet";
                        }
                        if (name == null || name.trim().length() == 0) {
                            name = id;
                        }
                        descArray.add(new IconSetDescriptor(
                                id, name,
                                group,
                                className,
                                params.toArray(new String[params.size()]),
                                owner,
                                loader));
                    }
//                    System.out.println("IconSetDescriptor.endElement "+qName);
                    this.qName = null;
                }
            });
        } catch (Throwable t) {
            throw new InvalidIconSetException(t.toString());
        }
        return descArray.toArray(new IconSetDescriptor[descArray.size()]);
    }

    public static Icon getIcon(String iconSetName, String icon) throws IconSetNotFoundException, IconNotFoundException {
        return getIcon(iconSetName, icon, Locale.getDefault());
    }

    public static Icon getIcon(String icon, Locale locale) throws IconSetNotFoundException, IconNotFoundException {
        return getIcon(defaultIconSet.getId(), icon, locale);
    }

    public static Icon getIcon(String iconSetName, String icon, Locale locale) throws IconSetNotFoundException, IconNotFoundException {
        return getIconSetFactory().getIconSet(iconSetName, locale).getIcon(icon);
    }
    
        public static IconSetFactory getIconSetFactory() {
        if (factory == null) {
            factory = new DefaultIconSetFactory();
        }
        return factory;
    }

    public static void setIconSetFactory(IconSetFactory fct) {
        IconSetFactory old = factory;
        factory = fct;
        support.firePropertyChange(ICON_SET_FACTORY_CHANGED, old, factory);
    }

    public static IconSet getIconSet() {
        return IconSetManager.defaultIconSet;
    }

    public static IconSet getIconSet(String iconSetName) {
        return getIconSetFactory().getIconSet(iconSetName, Locale.getDefault());
    }

    public static void setIconSet(String iconSetName) {
        setIconSet(iconSetName, Locale.getDefault());
    }

    public static void setIconSet(String iconSetName, Locale locale) {
        if (iconSetName != null) {
            IconSet old = IconSetManager.defaultIconSet;
            IconSetManager.defaultIconSet = getIconSetFactory().getIconSet(iconSetName, locale);
            support.firePropertyChange(ICON_SET_CHANGED, old, IconSetManager.defaultIconSet);
        }
    }

    public static void setIconSet(IconSet iconSet) {
        if (iconSet != null) {
            IconSet old = IconSetManager.defaultIconSet;
            IconSetManager.defaultIconSet = iconSet;
            support.firePropertyChange(ICON_SET_CHANGED, old, IconSetManager.defaultIconSet);
        }
    }

    public static void registerIconSet(IconSetDescriptor desc,LoggerProvider loggerProvider) {
        getIconSetFactory().registerIconSetDescriptor(desc,loggerProvider);
    }

    public static IconSetDescriptor[] getIconSetDescriptors() {
        return getIconSetFactory().getIconSetDescriptors();
    }

}

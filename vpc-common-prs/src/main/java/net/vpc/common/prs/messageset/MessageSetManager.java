/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */

package net.vpc.common.prs.messageset;


import net.vpc.common.prs.locale.LocaleManager;
import net.vpc.common.prs.plugin.UrlCacheManager;
import net.vpc.common.prs.classloader.PluggableResourcesClassLoader;
import net.vpc.common.prs.classloader.MultiClassLoader;
import net.vpc.common.prs.util.IOUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 26 juin 2006 23:57:46
 */
public class MessageSetManager {
    private static MultiClassLoader defaultClassLoader;
    private static ArrayList<URL> validClassLoaderURLs = new ArrayList<URL>();



    public static ClassLoader getDefaultClassLoader() {
        if (defaultClassLoader == null) {
            defaultClassLoader = new MultiClassLoader(MessageSetManager.class.getClassLoader());
        }
        return defaultClassLoader;
    }

    public static void registerMessageSet(URL messageSetURL, ClassLoader parent,final Object owner, UrlCacheManager urlCacheManager) throws IOException {
        try {
            URLClassLoader ucl = new URLClassLoader(new URL[]{messageSetURL}, parent/*MessageSetManager.class.getClassLoader()*/);
            URL iconsetxml = ucl.getResource("META-INF/locales.xml");
            if (iconsetxml == null) {
                iconsetxml = ucl.getResource("meta-inf/locales.xml");
            }
            if (iconsetxml == null) {
                throw new NoSuchElementException("missing locales.xml");
            }
//            final PluggableResourcesClassLoader loader = new PluggableResourcesClassLoader(messageSetURL, parent);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            InputStream stream = iconsetxml.openStream();
            saxParser.parse(stream, new DefaultHandler() {
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if ("locale".equals(qName)) {
                        String name = attributes.getValue("name");
                        if (name != null) {
                            String[] parts = name.split("_");
                            LocaleManager.getInstance().registerLocale(new Locale(parts[0], parts.length > 1 ? parts[1]
                                    : "", parts.length > 2 ? parts[2] : ""));
                        }
                    }
                }

            });
            validClassLoaderURLs.add(messageSetURL);
            if (defaultClassLoader == null) {
                defaultClassLoader=new MultiClassLoader(MessageSetManager.class.getClassLoader());
            }
            defaultClassLoader.setDelagateLoaders(new PluggableResourcesClassLoader(validClassLoaderURLs.toArray(new URL[validClassLoaderURLs.size()]), parent, IOUtils.getFileNameWithoutExtension(new File(messageSetURL.getFile()))));
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Throwable e) {
            System.err.println("InvalidMessageSetException [IGNORED] " + messageSetURL + " : " + e);
            throw new IllegalArgumentException("InvalidMessageSetException [IGNORED] " + messageSetURL, e);
        }
    }

    public static void loadAvailableMessageSets(URL repository, ClassLoader parent,Object owner,UrlCacheManager urlCacheManager) throws IOException {
        ArrayList<URL> repositoryItems = new ArrayList<URL>();
        if ("file".equals(repository.getProtocol())) {
            File[] bootIconSets = new File(repository.getFile())
                    .listFiles(new FilenameFilter() {
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
            registerMessageSet(repositoryItem, parent,owner,urlCacheManager);
        }
    }

}

/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/

package net.thevpc.common.prs.messageset;


import net.thevpc.common.prs.classloader.MultiClassLoader;
import net.thevpc.common.prs.classloader.PluggableResourcesClassLoader;
import net.thevpc.common.prs.locale.LocaleManager;
import net.thevpc.common.prs.plugin.UrlCacheManager;
import net.thevpc.common.prs.util.PRSPrivateIOUtils;
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
            defaultClassLoader.setDelagateLoaders(new PluggableResourcesClassLoader(validClassLoaderURLs.toArray(new URL[validClassLoaderURLs.size()]), parent, PRSPrivateIOUtils.getFileNameWithoutExtension(new File(messageSetURL.getFile()))));
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

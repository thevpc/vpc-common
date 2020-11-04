package net.thevpc.common.prs.plugin;

import net.thevpc.common.prs.classloader.PluggableResourcesClassLoader;
import net.thevpc.common.prs.util.PRSPrivateIOUtils;
import net.thevpc.common.prs.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class PluginLoader {
    private Vector<PluginLoaderListener> listeners;

    public PluginDescriptor loadPluginDescriptor(final URL url, final ClassLoader parent) throws PluginException {
        try {
            //URLClassLoader ucl = new URLClassLoader(new URL[]{url}, parent);
            URLClassLoader ucl0 = new URLClassLoader(new URL[]{url}, null);
            URL xmlUrl = ucl0.getResource("META-INF/plugin.xml");
            String CRC = PRSPrivateIOUtils.computeCRC(url.openStream());
            if (xmlUrl == null) {
                xmlUrl = ucl0.getResource("meta-inf/plugin.xml");
            }
            PluginDescriptor descriptor = null;
            if (xmlUrl == null) {
                System.err.println("url " + url + " is not a plugin archive, ignored  : missing plugin.xml");
            } else {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                InputStream stream = xmlUrl.openStream();
                MyHandler myHandler = new MyHandler(url, CRC, parent);
                saxParser.parse(stream, myHandler);
                descriptor = myHandler.descriptor;
            }
            if (descriptor == null) {
                throw new IllegalArgumentException("Invalid plugin");
            }

            if (listeners != null) {
                for (PluginLoaderListener listener : listeners) {
                    listener.prepareResources(descriptor, CRC);
                }
            }
//            if (descriptor.getResources() == null) {
//                throw new IllegalArgumentException("getResources == null");
//            }
            if (listeners != null) {
                for (PluginLoaderListener listener : listeners) {
                    listener.pluginDescriptorReloaded(descriptor);
                }
            }
            return descriptor;
        } catch (Throwable t) {
            throw new PluginException(url.toString(), t);
        }
    }


    public void addListener(PluginLoaderListener listener) {
        if (listeners == null) {
            listeners = new Vector<PluginLoaderListener>();
        }
        listeners.add(listener);
    }

    public void removeListener(PluginLoaderListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    private static void addPluginIfNotFound(PluginDescriptor info, Hashtable<String, PluginDescriptor> allDescs) {
        if (allDescs.containsKey(info.getId())) {
            PluginDescriptor other = allDescs.get(info.getId());
            System.err.println("unable to register " +
                    "plugin " + info.getId() + ", ignored  : Already registred" +
                    "\n\tNew plugin is " + info.getId() + ", class " + info.getPluginClassName() + ", url " + info.getPluginURL() +
                    "\n\tExisting plugin is " + other.getId() + ", class " + other.getPluginClassName() + ", url " + other.getPluginURL()
            );
        } else {
            allDescs.put(info.getId(), info);
        }
    }

    private class MyHandler extends DefaultHandler {
        String tag = null;
        StringBuilder sb = null;
        PluginDescriptor descriptor = null;
        final URL url;
        final ClassLoader parent;
        String CRC;

        private MyHandler(URL url, String CRC, ClassLoader parent) {
            this.url = url;
            this.CRC = CRC;
            this.parent = parent;
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            tag = qName;
            if ("plugin".equals(qName)) {
                String attrClassName = attributes.getValue("class");
                boolean attrSystem = "true".equalsIgnoreCase(attributes.getValue("system"));
                String pluginId = attributes.getValue("id");
                PluggableResourcesClassLoader loader = null;
                try {
                    loader = new PluggableResourcesClassLoader(url, parent, PRSPrivateIOUtils.getFileNameWithoutExtension(new File(url.getFile())));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                descriptor = new PluginDescriptor(attrClassName, url, attrSystem);
                descriptor.setCRC(CRC);
                descriptor.setTitle(attributes.getValue("title"));
                descriptor.setVersion(Version.parse(attributes.getValue("version")));
                descriptor.setHomeUrl(attributes.getValue("url"));
                descriptor.setContributors(attributes.getValue("contributors"));
                descriptor.setAuthor(attributes.getValue("author"));
                descriptor.setCategory(attributes.getValue("category"));
                descriptor.setId(pluginId);
                String dynamicLoadingString = attributes.getValue("dynamicLoading");
                if (dynamicLoadingString != null) {
                    descriptor.setDynamicLoading(Boolean.valueOf(dynamicLoadingString));
                }
                try {
                    descriptor.setApplicationVersion(VersionInterval.parse(attributes.getValue("applicationVersion")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (listeners != null) {
                    for (PluginLoaderListener listener : listeners) {
                        listener.pluginDescriptorCreated(descriptor);
                    }
                }
            }
            sb = null;
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            if (sb == null) {
                sb = new StringBuilder();
            }
            sb.append(new String(ch, start, length).trim());
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("plugin".equalsIgnoreCase(qName)) {
                //descriptor = null;
                //tag = null;
            } else {
                String val = (sb == null || sb.toString().trim().length() == 0) ? null : sb.toString().trim();
                if ("title".equalsIgnoreCase(tag)) {
                    descriptor.setTitle(val);
                } else if ("version".equalsIgnoreCase(tag)) {
                    descriptor.setVersion(Version.parse(val));
                } else if ("author".equalsIgnoreCase(tag)) {
                    descriptor.setAuthor(val);
                } else if ("url".equalsIgnoreCase(tag)) {
                    descriptor.setHomeUrl(val);
                } else if ("contributors".equalsIgnoreCase(tag)) {
                    descriptor.setContributors(val);
                } else if ("category".equalsIgnoreCase(tag)) {
                    descriptor.setCategory(val);
                } else if ("description".equalsIgnoreCase(tag)) {
                    descriptor.setDescription(val);
                } else if ("applicationVersion".equalsIgnoreCase(tag)) {
                    try {
                        descriptor.setApplicationVersion(VersionInterval.parse(val));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if ("dependency".equalsIgnoreCase(tag)) {
                    ArrayList<PluginDependency> d = new ArrayList<PluginDependency>(Arrays.asList(descriptor.getDependencies()));
                    try {
                        PluginDependency pd = PluginDependency.parse(val);

                        if (pd != null) {
                            d.add(pd);
                        }
                        descriptor.setDependencies(d.toArray(new PluginDependency[d.size()]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (tag != null) {
                    System.err.println("skipped tag (" + tag + ") in URL : " + url);
                }
            }
            sb = null;
        }

    }

}

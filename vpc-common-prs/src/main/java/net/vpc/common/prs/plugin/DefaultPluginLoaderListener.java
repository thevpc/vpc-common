package net.vpc.common.prs.plugin;

import net.vpc.common.prs.Version;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class DefaultPluginLoaderListener implements PluginLoaderListener {
    private PluginManager pluginManager;

    public DefaultPluginLoaderListener(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void pluginDescriptorCreated(PluginDescriptor pluginInfo) {

    }

    public void prepareResources(PluginDescriptor pluginInfo, String newCRC) {
        File pluginFolder;
        File pluginFile;
        Properties properties = new Properties();
        pluginFolder = pluginManager.getPluginVarFolder(pluginInfo.getId());
        pluginInfo.setWorkingDirectory(pluginFolder);
        pluginFile = new File(pluginFolder, "plugin.nfo");
        if (pluginFile.exists()) {
            try {
                properties.load(new FileInputStream(pluginFile));
            } catch (IOException e) {
                //ignore
            }
        }

        String idProperty = properties.getProperty("id");
        String versionProperty = properties.getProperty("version");
        String oldCRC = properties.getProperty("CRC");
        if (
                (idProperty == null
                        || pluginInfo.getId().equals(idProperty)) &&
                        (
                                versionProperty == null
                                        || !pluginInfo.getVersion().equals(new Version(versionProperty))
                                        || !pluginInfo.getCRC().equals(oldCRC)
                        )
                ) {
            pluginInfo.setUpdated(true);
            eradicate(new File(pluginFolder, "lib"));

            ArrayList<URL> embeddedURLs = new ArrayList<URL>();
            JarInputStream jar = null;
            try {
                try {
                    jar = new JarInputStream(pluginInfo.getPluginURL().openStream());
                    ZipEntry nextEntry;
                    URLClassLoader ucl0 = null;
                    while ((nextEntry = jar.getNextEntry()) != null) {
                        String path = nextEntry.getName();
                        String pathlc = path.toLowerCase();
                        if (pathlc.startsWith("meta-inf/lib/") && !pathlc.endsWith("/") /*not folder*/ /*&& (pathlc.endsWith(".jar"))*/) {
                            if (ucl0 == null) {
                                ucl0 = new URLClassLoader(new URL[]{pluginInfo.getPluginURL()}, null);
                            }
                            URL url2 = ucl0.getResource(path);
//                    System.out.println("pathlc = " + pathlc+" {from "+url+"}");
                            embeddedURLs.add(translateURL(url2, pluginInfo));
                        }
                    }
                } finally {
                    if (jar != null) {
                        jar.close();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            pluginInfo.setResources(embeddedURLs.toArray(new URL[embeddedURLs.size()]));

        } else {
            File libFolder = new File(pluginFolder, "lib");
            String libProperty = properties.getProperty("lib");
            ArrayList<URL> embeddedURLs = new ArrayList<URL>();
            if (libProperty != null && libProperty.length() > 0) {
                String[] lib = libProperty.split(";");
                for (String s : lib) {
                    try {
                        embeddedURLs.add(new File(libFolder, s).toURI().toURL());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            pluginInfo.setResources(embeddedURLs.toArray(new URL[embeddedURLs.size()]));
        }
    }

    private static URL translateURL(URL url, PluginDescriptor pluginInfosRet) throws IOException {
        File tempFolder = new File(pluginInfosRet.getWorkingDirectory(), "lib");
        if (!tempFolder.exists()) {
            if (!tempFolder.mkdirs()) {
                throw new IllegalArgumentException("Unable to create cache folder");
            }
        }
        File ff = new File(tempFolder, getURLName(url));
        FileOutputStream os = new FileOutputStream(ff);

        InputStream cis = null;
        cis = url.openStream();
        byte[] buf = new byte[1024 * 10];
        int c;
        while ((c = cis.read(buf)) >= 0) {
            os.write(buf, 0, c);
        }
        os.close();
        cis.close();
        return ff.toURI().toURL();
    }

    private static String getURLName(URL url) {
        String p = url.getPath();
        int x = p.lastIndexOf('/');
        if (x >= 0) {
            p = p.substring(x + 1);
        }
        x = p.lastIndexOf('!');
        if (x >= 0) {
            p = p.substring(x + 1);
        }
        return p;
    }

    public void pluginDescriptorReloaded(PluginDescriptor pluginInfo) {
        try {
            File pluginFolder;
            File pluginFile;
            Properties properties = new Properties();
            pluginFolder = pluginManager.getPluginVarFolder(pluginInfo.getId());
            pluginInfo.setWorkingDirectory(pluginFolder);
            pluginFile = new File(pluginFolder, "plugin.nfo");

            properties.put("id", pluginInfo.getId());
            properties.put("CRC", pluginInfo.getCRC());
            properties.put("version", pluginInfo.getVersion().toString());
            StringBuilder sb = new StringBuilder();
            for (URL u : pluginInfo.getResources()) {
                if (sb.length() > 0) {
                    sb.append(";");
                }
                sb.append(getURLName(u));
            }
            properties.setProperty("lib", sb.toString());
            pluginFile.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(pluginFile);
            properties.store(out, "Plugin Descriptor");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private String getURLName(URL url) {
//        String p = url.getPath();
//        int x = p.lastIndexOf('/');
//        if (x >= 0) {
//            p = p.substring(x + 1);
//        }
//        x = p.lastIndexOf('!');
//        if (x >= 0) {
//            p = p.substring(x + 1);
//        }
//        return p;
//    }


    private void eradicate(File f) {
        if (f.exists()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                for (File file : f.listFiles()) {
                    eradicate(file);
                }
                f.delete();
            } else {
                throw new IllegalArgumentException("could not eradicate : " + f);
            }
        }
    }
}

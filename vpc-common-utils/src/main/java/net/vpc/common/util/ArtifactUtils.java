/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resolve Maven (Nuts ?) artifacts Helper class
 * @author vpc
 */
public class ArtifactUtils {

    /**
     * resolve all Maven/Nuts artifact definitions in the classloader 
     * that has loaded <code>clazz</code>
     * @param clazz
     * @return  artifacts array in the form groupId:artfcatId#version
     */
    public static String[] resolveArtifacts(Class clazz) {
        List<String> all = new ArrayList<String>();
        try {
            final String n = clazz.getName().replace('.', '/').concat(".class");
            final Enumeration<URL> r = clazz.getClassLoader().getResources(n);
            for (URL baseUrls : Collections.list(r)) {
                final URLParts aa = new URLParts(baseUrls);
                String basePath = aa.getLastPart().getPath().substring(0, aa.getLastPart().getPath().length() - n.length());
                if (!basePath.endsWith("/")) {
                    basePath += "/";
                }
                final URLParts p = aa.getParent().append(basePath + "META-INF/maven");
                System.out.println(baseUrls);
                for (URL url : p.getChildren(false, true, new URLFilter() {
                    @Override
                    public boolean accept(URL path) {
                        return new URLParts(path).getName().equals("pom.properties");
                    }
                })) {
                    if (url != null) {
                        Properties prop = new Properties();
                        try {
                            prop.load(url.openStream());
                        } catch (IOException e) {
                            //
                        }
                        String version = prop.getProperty("version");
                        String groupId = prop.getProperty("groupId");
                        String artifactId = prop.getProperty("artifactId");
                        if (version != null && version.trim().length() != 0) {
                            all.add(groupId + ":" + artifactId + "#" + version);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ArtifactUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return all.toArray(new String[all.size()]);
    }

    public static String resolveArtifact(Class clazz) {
        return resolveArtifact(clazz, "Dev");
    }

    public static String resolveArtifact(Class clazz, String defaultValue) {
        for (String v : resolveArtifacts(clazz)) {
            return v;
        }
        return defaultValue;
    }

    public static String resolveArtifact(Class clazz, String groupId, String artifactId, String defaultValue) {
        String ver = resolveArtifactVersion(clazz, groupId, artifactId, defaultValue);
        return groupId + ":" + artifactId + "#" + ver;
    }

    public static String resolveArtifactVersion(String groupId, String artifactId, String defaultValue) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");

        if (url != null) {
            Properties p = new Properties();
            try {
                p.load(url.openStream());
            } catch (IOException e) {
                //
            }
            String version = p.getProperty("version");
            if (version != null && version.trim().length() != 0) {
                return version;
            }
        }
        return defaultValue;
    }
    public static String resolveArtifactVersion(Class clazz, String groupId, String artifactId, String defaultValue) {
        URL url = clazz.getClassLoader().getResource("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");

        if (url != null) {
            Properties p = new Properties();
            try {
                p.load(url.openStream());
            } catch (IOException e) {
                //
            }
            String version = p.getProperty("version");
            if (version != null && version.trim().length() != 0) {
                return version;
            }
        }
        return defaultValue;
    }

}

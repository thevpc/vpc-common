/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
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
package net.vpc.common.prs;

import net.vpc.common.prs.plugin.PluginDescriptor;
import net.vpc.common.prs.plugin.PluginRepositoryTool;
import net.vpc.common.prs.util.IOUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 6 oct. 2007 15:26:07
 */
public class PRSTools {
    private static final String VERSION = "1.3.20110413";

    public static void main(String[] args) {
        System.out.println("PRSTools v " + VERSION);
        if (args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(arg);
            }
            try {
                System.out.println("PRSTools[" + new File(".").getCanonicalPath() + "] : " + sb);
            } catch (IOException e) {
                System.out.println("PRSTools[" + new File(".").getAbsolutePath() + "] : " + sb);
            }
            for (int i = 0; i < args.length; i++) {
                String c = args[i];
                if ("-build-repository".equals(c)) {
                    buildRepository(args[i + 1], args[i + 2]);
                    i += 2;
                } else if ("-build-file".equals(c)) {
                    buildFile(args[i + 1], args[i + 2], args[i + 3]);
                    i += 3;
                } else {
                    showHelp(args);
                    System.exit(1);
                    return;
                }
            }
        }
    }

    public static void buildRepository(String version, String rootFolder) {
        String binFolder = rootFolder + "/bin";
        String srcFolder = rootFolder + "/src";
        if (!isValidBinfolder(new File(binFolder))) {
            try {
                System.err.println("Not a valid Plugins folder : " + new File(rootFolder).getCanonicalPath());
            } catch (IOException e) {
                System.err.println("Not a valid Plugins folder : " + new File(rootFolder).getAbsolutePath());
            }
            return;
        }
        try {
            PluginRepositoryTool.buildLocalPluginsRepository(version, new File(rootFolder), new File(binFolder), new File(srcFolder));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            String htmlTemplate = new File(rootFolder, "vpp.list.html").getPath();
            String htmlTarget = new File(rootFolder, "list.html").getPath();

            if (!new File(htmlTemplate).exists()) {
                System.err.println("Invalid Template file " + new File(htmlTemplate).getCanonicalPath() + " <== " + new File(htmlTemplate));
                System.exit(1);
            }
            PluginDescriptor[] localPlugins = PluginRepositoryTool.getLocalPlugins(new File(binFolder));
            Arrays.sort(localPlugins, new Comparator<PluginDescriptor>() {
                public int compare(PluginDescriptor o1, PluginDescriptor o2) {
                    int i = o1.getCategory().compareTo(o2.getCategory());
                    if (i == 0) {
                        return o1.getId().compareTo(o2.getId());
                    }
                    return i;
                }
            });
            class PluginData {
                public String category;
                public String name;
                public String title;
                public String version;
                public String binFile;
                public String srcFile;
                public String description;
                public String author;
            }
            class Data {
                public String lastUpdated;
                public PluginData[] plugins;
            }
            Data d = new Data();
            d.lastUpdated = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
            d.plugins = new PluginData[localPlugins.length];
            for (int i = 0; i < localPlugins.length; i++) {
                PluginDescriptor localPlugin = localPlugins[i];
                PluginData p = new PluginData();
                d.plugins[i] = p;
                p.author = localPlugin.getAuthor();
                p.binFile = new File(localPlugin.getPluginURL().getFile()).getName();
                p.category = localPlugin.getCategory();
                p.description = localPlugin.getDescription();
                p.name = localPlugin.getId();
                p.srcFile = IOUtils.getFileNameWithoutExtension(new File(localPlugin.getPluginURL().getFile())) + "-src.zip";
                p.title = localPlugin.getTitle();
                p.version = localPlugin.getVersion().toString();
            }

            SimpleHtmlReportParser parser = new SimpleHtmlReportParser();
            PrintStream out = null;
            FileReader in = null;
            try {
                in = new FileReader(htmlTemplate);
                out = new PrintStream(new File(htmlTarget));
                parser.eval(d, in, out);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    //
                }
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception e) {
                    //
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public static void showHelp(String[] args) {
        System.err.println("[SYNTAX] PRSTools ");
        System.err.println("              [-build-repository <version> <root_folder>]");
        System.err.println("              [-build-file <template> <props> <target>]");
    }


    private static void buildFile(String template, String propsFile, String target) {
        try {
            Properties props = new Properties();
            props.putAll(System.getProperties());
            props.load(new FileReader(propsFile));
            SimpleHtmlReportParser parser = new SimpleHtmlReportParser();
            PrintStream out = null;
            FileReader in = null;
            try {
                in = new FileReader(new File(template));
                out = new PrintStream(new File(target));
                parser.eval(props, in, out);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    //
                }
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception e) {
                    //
                }
            }
        } catch (IOException ee) {
            ee.printStackTrace();
        }
    }

    private static boolean isValidBinfolder(File binFolder) {
        if (!binFolder.exists()) {
            return false;
        }
        String[] plugins = binFolder.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jar");
            }
        });
        return (plugins != null && plugins.length > 0);
    }

//    public static void visitJarClasses(URL url, JarClassVisitor visitor, ClassLoader classLoader) throws IOException {
//        if (classLoader == null) {
//            classLoader = new URLClassLoader(new URL[]{url}, null);
//        }
//        JarInputStream jar = new JarInputStream(url.openStream());
//        ZipEntry nextEntry;
//        while ((nextEntry = jar.getNextJarEntry()) != null) {
//            String path = nextEntry.getName();
//            if (path.endsWith(".class") && path.indexOf('$') < 0) {
//                String className = path.substring(0, path.length() - 6).replace('/', '.');
//                try {
//                    Class clazz = Class.forName(className, true, classLoader);
//                    if (!visitor.nextClass(clazz)) {
//                        return;
//                    }
//                } catch (ClassNotFoundException ex) {
//                    System.err.println("Unable to load (" + className + ") due to ClassNotFoundException : " + ex + "\n==>" + url);
//                    ex.printStackTrace();
//                } catch (Throwable ex) {
//                    System.err.println("Unable to load (" + className + ") due to " + ex.getClass().getSimpleName() + " : " + ex + "\n==>" + url);
//                    ex.printStackTrace();
//                }
//            }
//        }
//    }


}

package net.vpc.common.prs.plugin;

import net.vpc.common.prs.util.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class PluginRepositoryTool {
    protected static FilenameFilter JAR_FILENAME_FILTER = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.matches(".*\\.jar");
        }
    };

    public static PluginDescriptor[] getLocalPlugins(File pluginBinaries) throws IOException {
        File[] pluginJarFiles = pluginBinaries.listFiles(JAR_FILENAME_FILTER);
        if (pluginJarFiles == null) {
            pluginJarFiles = new File[0];
        }
        ArrayList<PluginDescriptor> pluginInfos = new ArrayList<PluginDescriptor>();
        for (File jarFile : pluginJarFiles) {
            try {
                PluginLoader loader = new PluginLoader();
                pluginInfos.add(loader.loadPluginDescriptor(jarFile.toURL(), DefaultPluginManager.class.getClassLoader()));
            } catch (PluginException e) {
                e.printStackTrace();
            }
        }
        return pluginInfos.toArray(new PluginDescriptor[pluginInfos.size()]);
    }

    public static void buildLocalPluginsRepository(String version, File repositoryRootFolder, File pluginBinaries, File pluginSources) throws IOException {
        buildLocalPluginsRepository(version, getLocalPlugins(pluginBinaries), pluginSources, repositoryRootFolder);
    }

    public static void buildLocalPluginsRepository(String version, PluginDescriptor[] plugins, File pluginSources, File repositoryRootFolder) throws IOException {
        PluginsList ll = new PluginsList();
        repositoryRootFolder.mkdirs();
        for (PluginDescriptor pluginInfo : plugins) {
            //if (!pluginInfo.isSystem()) {
            PluginDescriptor ipi = new PluginDescriptor();
            ipi.setId(pluginInfo.getId());
            ipi.setAuthor(pluginInfo.getAuthor());
            ipi.setCategory(pluginInfo.getCategory());
            ipi.setContributors(pluginInfo.getContributors());
            ipi.setTitle(pluginInfo.getTitle());
            ipi.setHomeUrl(pluginInfo.getHomeUrl());
            ipi.setVersion(pluginInfo.getVersion());
            ipi.setDependencies(pluginInfo.getDependencies());
            ipi.setDescription(pluginInfo.getDescription());
            ipi.setApplicationVersion(pluginInfo.getApplicationVersion());
            ipi.setDynamicLoading(pluginInfo.isDynamicLoading());

            File destBinaryFile = new File(repositoryRootFolder + "/bin/" + (new File(pluginInfo.getPluginURL().getFile()).getName()));
            if (!pluginInfo.getPluginURL().equals(destBinaryFile.toURI().toURL())) {
                IOUtils.copy(pluginInfo.getPluginURL(), destBinaryFile);
            }
            ipi.setBinarySize(destBinaryFile.length());
            ipi.setBinaryUrl("bin/" + (new File(pluginInfo.getPluginURL().getFile()).getName()));

            File srcFile0 = new File(pluginSources, IOUtils.getFileNameWithoutExtension(new File(pluginInfo.getPluginURL().getFile())) + "-src.zip");
            File srcFile1 = new File(repositoryRootFolder + "/src/", IOUtils.getFileNameWithoutExtension(new File(pluginInfo.getPluginURL().getFile())) + "-src.zip");
            if (srcFile0.exists()) {
                if (!srcFile0.toURI().toURL().equals(srcFile1.toURI().toURL())) {
                    IOUtils.copy(srcFile0, srcFile1);
                }
                ipi.setSourceSize(srcFile1.length());
                ipi.setSourceUrl("src/" + srcFile1.getName());
            } else {
                ipi.setSourceUrl(null);
                ipi.setSourceSize(0);
            }
            ll.addPluginDescriptor(ipi);
            //}
        }
        File f = new File(repositoryRootFolder, "load");
        if (!f.exists() || (f.length() != 1024)) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(repositoryRootFolder, "load"));
                fos.write(new byte[1024]);
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
        ll.store(repositoryRootFolder);
        PrintStream versionFile = null;
        try {
            versionFile = new PrintStream(new File(repositoryRootFolder, "version.txt"));
            versionFile.println("version=" + version);
        } finally {
            if (versionFile != null) {
                versionFile.close();
            }
        }
    }

    public static void addPluginIfNotFound(PluginDescriptor info, Map<String, PluginDescriptor> allDescs) {
        if (allDescs.containsKey(info.getId())) {
            PluginDescriptor other = allDescs.get(info.getId());
            System.err.println("unable to register " +
                    "plugin " + info.getId() + ", ignored  : Already registered" +
                    "\n\tNew plugin is " + info.getId() + ", class " + info.getPluginClassName() + ", url " + info.getPluginURL() +
                    "\n\tExisting plugin is " + other.getId() + ", class " + other.getPluginClassName() + ", url " + other.getPluginURL()
            );
        } else {
            allDescs.put(info.getId(), info);
        }
    }
}

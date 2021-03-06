/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
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
package net.thevpc.common.prs.plugin;

import net.thevpc.common.prs.util.PRSPrivateIOUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 8 sept. 2007 18:58:27
 */
public class LocalRepositoryImpl extends AbstractLocalRepository {

    private final File enableFile;
    private final BlendedFile folderPlugins;
    private final File varFolder;

    private static FileFilter JAR_FILENAME_FILTER = new FileFilter() {

        public boolean accept(File file) {
            return file.getName().matches(".*\\.jar");
        }
    };

    public LocalRepositoryImpl(BlendedFile folderPlugins, File varFolder) {
        this.enableFile = new File(varFolder, "plugins-local-repository.xml");
        this.folderPlugins = folderPlugins;
        this.varFolder = varFolder;
        folderPlugins.mkdirs();
        varFolder.mkdirs();
    }

    public void load() {
        reset();
        Properties properties = new Properties();//
        try {
            FileInputStream is = null;
            try {
                is = new FileInputStream(enableFile);
                properties.loadFromXML(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (k.endsWith(".enabled")) {
                PluginRepositoryInfo repositoryInfo = new PluginRepositoryInfo(k.substring(0, k.length() - ".enabled".length()));
                repositoryInfo.setEnabled(Boolean.valueOf(v));
                register(repositoryInfo);
            }
        }
        installAndUninstallAll();
    }

    @Override
    public void store() throws PluginException {
        Properties properties = new Properties();//
        for (PluginRepositoryInfo repositoryInfo : getPlugins()) {
            properties.put(repositoryInfo.getId() + ".enabled", repositoryInfo.isEnabled() ? "true" : "false");
        }
        FileOutputStream os = null;
        try {
            try {
                File parentFile = enableFile.getParentFile();
                if (parentFile != null) {
                    PRSPrivateIOUtils.mkdirs(parentFile);
                }
                os = new FileOutputStream(enableFile);
                properties.storeToXML(os, "LocalRepositoryImpl");
            } finally {
                if (os != null) {
                    os.close();
                }
            }
        } catch (IOException e) {
            throw new PluginException("Unable to store local repository : " + e.getMessage(), e);
        }
    }

    @Override
    public String[] getPluginsForInstall() {
        File installFolder = getInstallFolder();
        File[] files = installFolder.listFiles();
        ArrayList<String> ok = new ArrayList<String>();
        if (files != null) {
            for (File file : files) {
                if (PRSPrivateIOUtils.getFileExtension(file).equals("jar")) {
                    ok.add(PRSPrivateIOUtils.getFileNameWithoutExtension(file));
                }
            }
        }
        return ok.toArray(new String[ok.size()]);
    }

    @Override
    public URL addInstallable(InputStream inputStream, String name) throws IOException {
        File installFolder = getInstallFolder();
        PRSPrivateIOUtils.mkdirs(installFolder);
        File file = null;
        try {
            file = new File(installFolder, name + ".jar");
            PRSPrivateIOUtils.copy(inputStream, file);
        } catch (IOException e) {
            if (file != null) {
                file.delete();
            }
            throw e;
        } finally {
            inputStream.close();
        }
        return file.toURI().toURL();
    }

    @Override
    public String[] getPluginsForUninstall() {
        //File installFolder = new File(folderPlugins, ".uninstall");
        File uninstallFolder = getUninstallFolder();
        File[] files = uninstallFolder.listFiles();
        ArrayList<String> ok = new ArrayList<String>();
        if (files != null) {
            for (File file : files) {
                if (PRSPrivateIOUtils.getFileExtension(file).equals("jar")) {
                    ok.add(PRSPrivateIOUtils.getFileNameWithoutExtension(file));
                }
            }
        }
        return ok.toArray(new String[ok.size()]);
    }

    /**
     * Check plugins for modification.
     * <p>
     * Plugins under [InstalledPluginsRepositoryURL]/.install will be installed
     * (or updated) Plugins under [InstalledPluginsRepositoryURL]/.uninstall
     * will be uninstalled (permanently)
     *
     * @throws PluginException if getInstalledPluginsRepositoryURL() is not a
     * local folder URL.
     */
    @Override
    public void installAndUninstallAll() {
        File installFolder = getInstallFolder();
        File uninstallFolder = getUninstallFolder();

        File[] found = installFolder.listFiles();
        if (found != null) {
            for (File file : found) {
                file.renameTo(new File(folderPlugins.getUserFile(), file.getName()));
            }
        }
        found = uninstallFolder.listFiles();
        if (found != null) {
            for (File file : found) {
                File pluginFile = new File(folderPlugins.getUserFile(), file.getName());
                if (!pluginFile.exists() || pluginFile.delete()) {
                    file.delete();
                }
            }
        }

    }

    @Override
    public void markPluginForUninstall(String pluginId) throws PluginException {
        File uninstallFolder = getUninstallFolder();
        uninstallFolder.mkdirs();
        try {
            PRSPrivateIOUtils.mkdirs(uninstallFolder);
            File file = new File(uninstallFolder, pluginId + ".jar");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new PluginException(pluginId, e);
        }
    }

    @Override
    public File getInstallFolder() {
        return new File(new File(varFolder, "upgrades"), "install");
    }

    @Override
    public File getUninstallFolder() {
        return new File(new File(varFolder, "upgrades"), "uninstall");
    }

    public URL[] getPluginURLs() {
        ArrayList<URL> urls = new ArrayList<URL>();
        for (BlendedFile appFile : folderPlugins.listFiles(JAR_FILENAME_FILTER)) {
            try {
                urls.add(appFile.getFile().toURI().toURL());
            } catch (MalformedURLException e) {
                //ignore
            }
        }
        return urls.toArray(new URL[urls.size()]);
    }

    @Override
    public URL getRepositoryURL() {
        try {
            return folderPlugins.getUserFile().toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}

package net.vpc.common.prs.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 15/04/11
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public interface LocalRepository {
    PluginRepositoryInfo getPlugin(String id);

    void register(PluginRepositoryInfo info);

    PluginRepositoryInfo[] getPlugins();

    void unregister(String id);

    String[] getPluginsForInstall();

    URL addInstallable(InputStream inputStream, String name) throws IOException;

    String[] getPluginsForUninstall();

    void installAndUninstallAll();

    void markPluginForUninstall(String pluginId) throws PluginException;

    File getInstallFolder();

    File getUninstallFolder();

    URL[] getPluginURLs();

    URL getRepositoryURL();

    void load();

    void store() throws PluginException;
}

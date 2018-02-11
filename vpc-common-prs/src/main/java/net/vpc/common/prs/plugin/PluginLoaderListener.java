package net.vpc.common.prs.plugin;

public interface PluginLoaderListener {
    void pluginDescriptorCreated(PluginDescriptor pluginInfo);
    void prepareResources(PluginDescriptor pluginInfo, String newCRC);
    void pluginDescriptorReloaded(PluginDescriptor pluginInfo);
}

package net.vpc.common.prs.plugin;

/**
* Created by IntelliJ IDEA.
* User: vpc
* Date: 8 janv. 2010
* Time: 16:17:30
* To change this template use File | Settings | File Templates.
*/
public interface ValueProvider {
    Class getType();

    Object getValue(PluginDescriptor pluginDescriptor);
}

package net.vpc.common.prs.plugin;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 8 févr. 2010
 * Time: 10:10:11
 * To change this template use File | Settings | File Templates.
 */
class ImplementationCachePriorityComparator implements Comparator<PluginManagerCache.ImplementationCache> {

    private PluginManager pluginManager;

    ImplementationCachePriorityComparator(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public int compare(PluginManagerCache.ImplementationCache o1, PluginManagerCache.ImplementationCache o2) {
        int x;
        //higher priority first
        int p1 = o1.getPriority();
        int p2 = o2.getPriority();

        x = p2 - p1;
        if (x != 0) {
            return x;
        }
        x = o1.getIndex() - o2.getIndex();
        if (x != 0) {
            return x;
        }
        return o1.getImplementationName().compareTo(o2.getImplementationName());
    }
}

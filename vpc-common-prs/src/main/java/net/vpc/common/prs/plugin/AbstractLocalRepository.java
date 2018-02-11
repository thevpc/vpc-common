/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author vpc
 */
public abstract class AbstractLocalRepository implements LocalRepository {

    private final Map<String, PluginRepositoryInfo> repository = new HashMap<String, PluginRepositoryInfo>();

    @Override
    public PluginRepositoryInfo getPlugin(String id) {
        PluginRepositoryInfo repositoryInfo = repository.get(id);
        if (repositoryInfo == null) {
            throw new NoSuchElementException(id);
        }
        return repositoryInfo;
    }

    @Override
    public void register(PluginRepositoryInfo info) {
        repository.put(info.getId(), info);
    }

    @Override
    public PluginRepositoryInfo[] getPlugins() {
        return repository.values().toArray(new PluginRepositoryInfo[repository.size()]);
    }

    @Override
    public void unregister(String id) {
        repository.remove(id);
    }

    public void reset() {
        repository.clear();
    }

}

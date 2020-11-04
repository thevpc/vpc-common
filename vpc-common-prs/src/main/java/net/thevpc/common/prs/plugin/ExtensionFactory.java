package net.thevpc.common.prs.plugin;

import java.util.List;

public interface ExtensionFactory<T> {
    public List<T> createExtensions();
}

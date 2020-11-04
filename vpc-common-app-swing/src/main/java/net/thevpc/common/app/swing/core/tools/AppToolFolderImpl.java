package net.thevpc.common.app.swing.core.tools;

import net.thevpc.common.app.AbstractAppTool;
import net.thevpc.common.app.AppToolFolder;
import net.thevpc.common.app.ItemPath;

public class AppToolFolderImpl extends AbstractAppTool implements AppToolFolder {
    private ItemPath path;

    public AppToolFolderImpl(String path) {
        super(ItemPath.of("menuBar").child(path).toString());
        this.path = ItemPath.of("menuBar").child(path);
        title().set(ItemPath.of(path).name());
        enabled().set(true);
        visible().set(true);
    }

    @Override
    public ItemPath path() {
        return path;
    }
}

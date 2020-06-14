package net.vpc.common.app.swing.core.tools;

import net.vpc.common.app.AppToolFolder;
import net.vpc.common.app.ItemPath;
import net.vpc.common.app.AbstractAppTool;

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

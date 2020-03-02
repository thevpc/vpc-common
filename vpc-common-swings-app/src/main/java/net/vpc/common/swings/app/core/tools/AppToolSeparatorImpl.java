package net.vpc.common.swings.app.core.tools;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.swings.app.AppToolSeparator;
import net.vpc.common.swings.app.ItemPath;
import net.vpc.common.swings.app.core.AbstractAppTool;

public class AppToolSeparatorImpl extends AbstractAppTool implements AppToolSeparator {
    private final WritablePValue<Integer> width = Props.of("width").valueOf(Integer.class, 0);
    private final WritablePValue<Integer> height = Props.of("height").valueOf( Integer.class, 0);
    private ItemPath path;

    public AppToolSeparatorImpl(String path) {
        super(ItemPath.of("menuBar").child(path).toString());
        this.path = ItemPath.of("menuBar").child(path);
    }

    public WritablePValue<Integer> width() {
        return width;
    }

    public WritablePValue<Integer> height() {
        return height;
    }

    @Override
    public ItemPath path() {
        return path;
    }
}

package net.vpc.common.swings.app.core;

import net.vpc.common.swings.app.AppComponentRenderer;
import net.vpc.common.swings.app.AppTool;
import net.vpc.common.swings.app.AppToolComponent;
import net.vpc.common.swings.app.ItemPath;

public class DefaultAppToolComponent<T extends AppTool> implements AppToolComponent<T> {
    private T tool;

    private int order;

    private ItemPath path;
    private AppComponentRenderer renderer;

    public DefaultAppToolComponent(T tool, String path, int order, AppComponentRenderer renderer) {
        this.tool = tool;
        this.order = order;
        this.path = ItemPath.of(path);
        this.renderer = renderer;
    }

    @Override
    public T tool() {
        return tool;
    }

    @Override
    public int order() {
        return order;
    }

    @Override
    public ItemPath path() {
        return path;
    }

    @Override
    public AppComponentRenderer renderer() {
        return renderer;
    }
}

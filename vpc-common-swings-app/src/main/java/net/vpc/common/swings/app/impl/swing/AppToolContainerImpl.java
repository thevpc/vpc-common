package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.prpbind.PropertyListeners;
import net.vpc.common.swings.app.ItemPath;
import net.vpc.common.swings.app.*;
import net.vpc.common.swings.app.core.BindingNode;
import net.vpc.common.swings.app.core.DefaultAppToolsBase;
import net.vpc.common.prpbind.impl.AppPropertyBinding;
import net.vpc.common.prpbind.impl.PropertyContainerSupport;

public class AppToolContainerImpl implements AppToolContainer {
    private PropertyContainerSupport s;
    private BindingNode root;
    private Application application;
    private DefaultAppToolsBase tools;

    public AppToolContainerImpl(String rootPath, Object rootGuiElement, Application application) {
        this.application = application;
        s = new PropertyContainerSupport(rootPath.toString(), this);
        BindingNodeFactory factory = new BindingNodeFactory();

        tools=new DefaultAppToolsBase(application) {
            @Override
            public <T extends AppTool> void addTool(AppToolComponent<T> tool) {
                root.add(tool);
            }

            @Override
            public <T extends AppTool> void removeTool(AppToolComponent<T> tool) {

            }
        };
        this.root = factory.createBindingNode(null, rootGuiElement, AppToolComponent.of(null, rootPath), this, application, tools.components);
    }

    @Override
    public AppTools tools() {
        return tools;
    }

    @Override
    public ItemPath path() {
        return this.root.getPath();
    }

    @Override
    public AppComponentRenderer renderer() {
        return null;
    }

    public AppNode rootNode() {
        return root;
    }

    public Application getApplication() {
        return application;
    }

    public Object component() {
        return root.getGuiElement();
    }

    @Override
    public AppPropertyBinding[] getProperties() {
        return s.getProperties();
    }

    @Override
    public PropertyListeners listeners() {
        return s.listeners();
    }
}

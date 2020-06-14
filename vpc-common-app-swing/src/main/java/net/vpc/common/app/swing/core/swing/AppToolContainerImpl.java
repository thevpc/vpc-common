package net.vpc.common.app.swing.core.swing;

import net.vpc.common.app.Application;
import net.vpc.common.app.AppComponentRenderer;
import net.vpc.common.app.AppTools;
import net.vpc.common.app.AppToolContainer;
import net.vpc.common.app.AppNode;
import net.vpc.common.app.AppToolComponent;
import net.vpc.common.app.AppTool;
import net.vpc.common.props.PropertyListeners;
import net.vpc.common.app.ItemPath;
import net.vpc.common.app.swing.core.BindingNode;
import net.vpc.common.app.swing.core.DefaultAppToolsBase;
import net.vpc.common.props.impl.AppPropertyBinding;
import net.vpc.common.props.impl.PropertyContainerSupport;

public class AppToolContainerImpl implements AppToolContainer {
    private PropertyContainerSupport s;
    private BindingNode root;
    private Application application;
    private DefaultAppToolsBase tools;
    protected Object rootGuiElement;

    public AppToolContainerImpl(String rootPath, Object rootGuiElement, Application application) {
        this.application = application;
        this.rootGuiElement = rootGuiElement;
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

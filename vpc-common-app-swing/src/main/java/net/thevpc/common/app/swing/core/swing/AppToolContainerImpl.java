package net.thevpc.common.app.swing.core.swing;

import net.thevpc.common.props.PropertyListeners;
import net.thevpc.common.props.impl.AppPropertyBinding;
import net.thevpc.common.props.impl.PropertyContainerSupport;
import net.thevpc.common.app.Application;
import net.thevpc.common.app.AppComponentRenderer;
import net.thevpc.common.app.AppTools;
import net.thevpc.common.app.AppToolContainer;
import net.thevpc.common.app.AppNode;
import net.thevpc.common.app.AppToolComponent;
import net.thevpc.common.app.AppTool;
import net.thevpc.common.app.ItemPath;
import net.thevpc.common.app.swing.core.BindingNode;
import net.thevpc.common.app.swing.core.DefaultAppToolsBase;

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
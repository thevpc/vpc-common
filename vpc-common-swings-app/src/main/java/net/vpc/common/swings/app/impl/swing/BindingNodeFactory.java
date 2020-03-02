package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.prpbind.WritablePList;
import net.vpc.common.swings.app.*;
import net.vpc.common.swings.app.core.BindingNode;
import net.vpc.common.swings.app.core.GuiComponentNavigator;

import javax.swing.*;

public class BindingNodeFactory {
    private GuiComponentNavigator<JMenuBar> _JMenuBar = new JMenuBarGuiComponentNavigator();
    private GuiComponentNavigator<JToolBar> _JToolBar = new JToolBarGuiComponentNavigator();
    private GuiComponentNavigator<JToolbarGroup> _JToolbarGroup = new JToolbarGroupGuiComponentNavigator();
    private GuiComponentNavigator<JStatusBarGroup> _JStatusBarGroup = new JStatusBarGroupGuiComponentNavigator();
    private GuiComponentNavigator<JMenu> _JMenu = new JMenuGuiComponentNavigator();
    private GuiComponentNavigator<JComponent> _Fallback = new JComponentGuiComponentNavigator();
    private AppToolSeparatorComponent _AppToolSeparatorComponent = new AppToolSeparatorComponent();
    private AppToolFolderComponent _AppToolFolderComponent = new AppToolFolderComponent();
    private AppToolRadioBoxComponent _AppToolRadioBoxComponent = new AppToolRadioBoxComponent();
    private AppToolCheckBoxComponent _AppToolCheckBoxComponent = new AppToolCheckBoxComponent();
    private AppToolActionComponent _AppToolActionComponent = new AppToolActionComponent();

    public BindingNode createBindingNode(BindingNode parent, Object guiElement, AppToolComponent binding, AppComponent appComponent, Application application, WritablePList<AppComponent> components) {

        return new BindingNode(parent, guiElement, binding, appComponent, application, components, this, getNavigator(guiElement));
    }

    private GuiComponentNavigator getNavigator(Object guiElement) {
        if (guiElement instanceof JMenu) {
            return _JMenu;
        }
        if (guiElement instanceof JMenuBar) {
            return _JMenuBar;
        }
        if (guiElement instanceof JToolBar) {
            return _JToolBar;
        }
        if (guiElement instanceof JToolbarGroup) {
            return _JToolbarGroup;
        }
        if (guiElement instanceof JStatusBarGroup) {
            return _JStatusBarGroup;
        }
        if (guiElement instanceof JComponent) {
            return _Fallback;
        }
        throw new IllegalArgumentException("Unsupported BindingNodeFactory for " + guiElement.getClass().getName());
    }

    public Object createGuiComponent(Object parentGuiComponent, AppComponent appComponent, Application application) {
        AppComponentRenderer r = appComponent.renderer();
        Object guiComponent=null;
        if (r != null) {
            guiComponent = r.createGuiComponent(appComponent, parentGuiComponent, application);
            if (guiComponent != null) {
                return guiComponent;
            }
        }
        if (appComponent instanceof AppToolComponent) {
            AppToolComponent b = (AppToolComponent) appComponent;
            if (b.tool() instanceof AppToolSeparator) {
                guiComponent= _AppToolSeparatorComponent.createGuiElement(parentGuiComponent, appComponent, application);
                if (guiComponent != null) {
                    return guiComponent;
                }
            }
            if (b.tool() instanceof AppToolFolder) {
                guiComponent=_AppToolFolderComponent.createGuiElement(parentGuiComponent, appComponent, application);
                if (guiComponent != null) {
                    return guiComponent;
                }
            }
            if (b.tool() instanceof AppToolRadioBox) {
                guiComponent=_AppToolRadioBoxComponent.createGuiElement(parentGuiComponent, appComponent, application);
                if (guiComponent != null) {
                    return guiComponent;
                }
            }
            if (b.tool() instanceof AppToolCheckBox) {
                guiComponent=_AppToolCheckBoxComponent.createGuiElement(parentGuiComponent, appComponent, application);
                if (guiComponent != null) {
                    return guiComponent;
                }
            }
            if (b.tool() instanceof AppToolAction) {
                guiComponent=_AppToolActionComponent.createGuiElement(parentGuiComponent, appComponent, application);
                if (guiComponent != null) {
                    return guiComponent;
                }
            }
        }
        throw new IllegalArgumentException("Unsupported BindingNodeFactory for " + appComponent.getClass().getSimpleName()+" to gui "+parentGuiComponent.getClass().getSimpleName());
    }

}

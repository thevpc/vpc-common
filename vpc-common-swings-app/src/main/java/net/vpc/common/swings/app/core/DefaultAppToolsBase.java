package net.vpc.common.swings.app.core;

import net.vpc.common.prpbind.*;
import net.vpc.common.swings.app.*;
import net.vpc.common.swings.app.core.tools.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class DefaultAppToolsBase implements AppTools {
    public WritablePList<AppComponent> components = Props.of("components").listOf(AppComponent.class);
    protected Map<String, ToolInfo> toolsMap = new HashMap<>();
    protected Application application;
    protected ToolMapResolverPropertyListener toolMapResolverAppPropertyListener = new ToolMapResolverPropertyListener();
    private WritablePList<AppTool> toolsList = Props.of("tools").listOf(AppTool.class);
    private java.util.List tools0 = new ArrayList<>();

    public DefaultAppToolsBase(Application application) {
        this.application = application;
        this.application.status().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                AppStatus s = (AppStatus) event.getNewValue();
                if (s == AppStatus.INIT) {
                    for (Iterator<AppToolComponent> iterator = tools0.iterator(); iterator.hasNext(); ) {
                        AppToolComponent tool = iterator.next();
                        iterator.remove();
                        addTool(tool);
                    }
                }
            }
        });
    }

    @Override
    public PList<AppTool> all() {
        return toolsList.readOnly();
    }

    @Override
    public AppTool getTool(String id) {
        ToolInfo o = toolsMap.get(id);
        return o == null ? null : o.tool;
    }

    @Override
    public AppComponent[] getComponents(String id) {
        ToolInfo o = toolsMap.get(id);
        return o == null ? new AppComponent[0] : o.components.values().toArray(new AppComponent[0]);
    }

    @Override
    public AppToolComponent<AppToolFolder> addFolder(String id, String path) {
        AppToolComponent<AppToolFolder> a = AppToolComponent.of(new AppToolFolderImpl(path), path);
        addTool(a);
        return a;
    }

    @Override
    public AppToolComponent<AppToolFolder> addFolder(String path) {
        return addFolder(null, path);
    }

    @Override
    public AppToolComponent<AppToolSeparator> addSeparator(String id, String path) {
        AppToolComponent<AppToolSeparator> a = AppToolComponent.of(new AppToolSeparatorImpl(path), path);
        addTool(a);
        return a;
    }

    @Override
    public AppToolComponent<AppToolSeparator> addSeparator(String path) {
        return addSeparator(null, path);
    }

    @Override
    public AppToolComponent<AppToolAction> addAction(String id, String path) {
        ItemPath ipath = ItemPath.of(path);
        path = ipath.toString();
        if (id == null) {
            id = ipath.toString();
        }
        AppToolAction action = new AppToolActionImpl(id, null);
        action.title().set(ipath.name());
        AppToolComponent<AppToolAction> binding = AppToolComponent.of(action, path);
        addTool(binding);
        return binding;
    }

    @Override
    public AppToolComponent<AppToolRadioBox> addRadio(String id, String path) {
        ItemPath ipath = ItemPath.of(path);
        path = ipath.toString();
        if (id == null) {
            id = path.toString();
        }
        AppToolRadioBox action = new AppToolRadioBoxImpl(id, null);
        action.title().set(ipath.name());
        AppToolComponent<AppToolRadioBox> a = AppToolComponent.of(action, path);
        addTool(a);
        return a;
    }

    @Override
    public AppToolComponent<AppToolCheckBox> addCheck(String id, String path) {
        ItemPath ipath = ItemPath.of(path);
        path = ipath.toString();
        if (id == null) {
            id = path.toString();
        }
        AppToolCheckBox action = new AppToolCheckBoxImpl(id, null);
        action.title().set(ipath.name());
        AppToolComponent<AppToolCheckBox> a = AppToolComponent.of(action, path);
        addTool(a);
        return a;
    }

    @Override
    public AppToolComponent<AppToolAction> addAction(String path) {
        return addAction(null, path);
    }

    @Override
    public AppToolComponent<AppToolRadioBox> addRadio(String path) {
        return addRadio(null, path);
    }

    @Override
    public AppToolComponent<AppToolCheckBox> addCheck(String path) {
        return addCheck(null, path);
    }

    @Override
    public PList<AppComponent> components() {
        return components.readOnly();
    }

    public void addRootContainer(AppToolContainer c) {
        if (c != null) {
            c.tools().components().listeners().add(toolMapResolverAppPropertyListener);
        }
    }

    public void removeRootContainer(AppToolContainer c) {
        if (c != null) {
            c.tools().components().listeners().add(toolMapResolverAppPropertyListener);
        }
    }

    public static class ToolInfo {
        private String id;
        private AppTool tool;
        private Map<ItemPath, AppComponent> components;
    }

    private class ToolMapResolverPropertyListener implements PropertyListener {
        private AppToolComponent toAppToolBinding(Object oldValue) {
            AppComponent o = (AppComponent) oldValue;
            if (o != null) {
                if (o instanceof AppToolComponent) {
                    return (AppToolComponent) o;
                }
            }
            return null;
        }

        private void onRemove(Object oldValue) {
            AppToolComponent b = toAppToolBinding(oldValue);
            AppComponent o = (AppComponent) oldValue;
            if (b != null) {
                components.remove(b);
                AppTool t = b.tool();
                ToolInfo p = toolsMap.get(t.id());
                if (p != null) {
                    p.components.remove(b.path());
                    if (p.components.size() == 0) {
                        toolsMap.remove(t.id());
                        toolsList.remove(p.tool);
                    }
                }
            }
        }

        private void onAdd(Object oldValue) {
            AppToolComponent b = toAppToolBinding(oldValue);
            AppComponent o = (AppComponent) oldValue;
            if (b != null) {
                components.add(b);
                AppTool t = b.tool();
                ToolInfo p = toolsMap.get(t.id());
                if (p != null) {
                    p.components.put(b.path(), b);
                } else {
                    p = new ToolInfo();
                    p.id = t.id();
                    p.tool = t;
                    p.components = new HashMap<>();
                    p.components.put(b.path(), b);
                    toolsList.add(p.tool);
                }
            }
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            switch (event.getAction()) {
                case ADD: {
                    onAdd(event.getNewValue());
                    break;
                }
                case REMOVE: {
                    onRemove(event.getOldValue());
                    break;
                }
                case UPDATE: {
                    onRemove(event.getOldValue());
                    onAdd(event.getNewValue());
                    break;
                }
            }
        }


    }

}

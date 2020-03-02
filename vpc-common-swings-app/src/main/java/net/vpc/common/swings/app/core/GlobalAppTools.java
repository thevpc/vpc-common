package net.vpc.common.swings.app.core;

import net.vpc.common.swings.app.ItemPath;
import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.swings.app.*;

import java.util.*;

public class GlobalAppTools extends DefaultAppToolsBase {
    private List<AppToolComponent> tools0 = new ArrayList<>();

    public GlobalAppTools(DefaultApplication application) {
        super(application);
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
    public void addTool(AppToolComponent tool) {
        if (this.application.status().get() == AppStatus.NONE) {
            this.tools0.add(tool);
            return;
        }
        String first = tool.path().first();
        AppToolComponent subBinding = AppToolComponent.of(tool.tool(), tool.path().skipFirst().toString(), tool.order(), tool.renderer());
        Set<String> available=new HashSet<>();
        for (AppNode node : this.application.nodes()) {
            AppToolContainer c = (AppToolContainer) node.getComponent();
            ItemPath path = c.rootNode().getPath();
            available.add(path.first());
            if (path.first().equals(first)) {
                c.tools().addTool(subBinding);
                return;
            }
        }
        throw new IllegalArgumentException("Unable to resolve to a valid path '"+tool.path()+"' . root nodes start with one of : "+available);
    }

    @Override
    public void removeTool(AppToolComponent tool) {

    }

//    protected void addRootContainer(AppToolContainer c) {
//        if (c != null) {
//            c.components().listeners().add(toolMapResolverAppPropertyListener);
//        }
//    }
//
//    protected void removeRootContainer(AppToolContainer c) {
//        if (c != null) {
//            c.components().listeners().add(toolMapResolverAppPropertyListener);
//        }
//    }


}

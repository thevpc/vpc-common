package net.vpc.common.swings.app.core;

import net.vpc.common.swings.app.ItemPath;
import net.vpc.common.swings.app.*;

import java.util.*;

public class ContainerAppTools extends DefaultAppToolsBase {

    public ContainerAppTools(Application application) {
        super(application);
    }



    @Override
    public void addTool(AppToolComponent tool) {
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

}

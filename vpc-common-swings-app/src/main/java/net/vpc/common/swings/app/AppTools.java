package net.vpc.common.swings.app;

import net.vpc.common.prpbind.PList;

public interface AppTools {
    PList<AppTool> all();

    AppTool getTool(String id);

    AppComponent[] getComponents(String id);

    AppToolComponent<AppToolFolder> addFolder(String id, String path);

    AppToolComponent<AppToolFolder> addFolder(String path);

    AppToolComponent<AppToolSeparator> addSeparator(String id, String path);

    AppToolComponent<AppToolSeparator> addSeparator(String path);

    AppToolComponent<AppToolAction> addAction(String id, String path);

    AppToolComponent<AppToolRadioBox> addRadio(String id, String path);

    AppToolComponent<AppToolCheckBox> addCheck(String id, String path);

    AppToolComponent<AppToolAction> addAction(String path);

    AppToolComponent<AppToolRadioBox> addRadio(String path);

    AppToolComponent<AppToolCheckBox> addCheck(String path);

    <T extends AppTool> void addTool(AppToolComponent<T> tool);

    <T extends AppTool> void removeTool(AppToolComponent<T> tool);

    PList<AppComponent> components();
}

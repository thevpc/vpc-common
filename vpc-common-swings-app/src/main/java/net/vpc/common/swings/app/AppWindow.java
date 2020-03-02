package net.vpc.common.swings.app;

import net.vpc.common.prpbind.WritablePValue;

import javax.swing.*;

public interface AppWindow extends AppToolContainer {
    Application getApplication();

    WritablePValue<AppMenuBar> menuBar();

    WritablePValue<AppStatusBar> statusBar();

    WritablePValue<AppToolBar> toolBar();

    WritablePValue<AppWorkspace> workspace();

    WritablePValue<String> title();

    WritablePValue<ImageIcon> icon();

    AppWindowStateSetValue state();
}

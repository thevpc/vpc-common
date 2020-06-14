package net.vpc.common.app;

import net.vpc.common.iconset.IconSet;
import net.vpc.common.iconset.PIconSet;
import net.vpc.common.i18n.I18n;
import net.vpc.common.props.*;

public interface Application extends PropertyContainer {

    PValue<String> name();

    PValue<AppState> state();

    void start();

    WritablePList<AppShutdownVeto> shutdownVetos();

    void shutdown();

    AppTools tools();

    WritablePValue<AppWindow> mainWindow();

    AppNode[] nodes();

    ApplicationBuilder builder();

    AppHistory history();

    I18n i18n();

    AppMessages messages();

    AppLogs logs();

    WritablePLMap<String, IconSet> iconSets();

    AppIconSet iconSet();

    WritablePValue<AppPropertiesTree> activeProperties();

    void runFront(Runnable run);

    void runBack(Runnable run);

    AppErrors errors();

    WritablePValue<String> currentWorkingDirectory();

    AppComponentRendererFactory componentRendererFactory();
}

package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;


public interface AppTool {

    String id();

    WritablePValue<String> smallIcon();

    WritablePValue<String> largeIcon();

    WritablePValue<Boolean> enabled();

    WritablePValue<Boolean> visible();

    WritablePValue<String> title();

    <T extends AppTool> AppToolComponent<T> copyTo(AppTools tools, String newPath);
}
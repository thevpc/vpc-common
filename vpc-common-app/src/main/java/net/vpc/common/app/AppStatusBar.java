package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

public interface AppStatusBar extends AppToolContainer {

    WritablePValue<Boolean> visible();
}

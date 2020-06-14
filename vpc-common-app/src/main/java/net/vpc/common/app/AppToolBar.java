package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

public interface AppToolBar extends AppToolContainer {
    WritablePValue<Boolean> visible();
}

package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

public interface AppMenuBar extends AppToolContainer {
    WritablePValue<Boolean> visible();
}

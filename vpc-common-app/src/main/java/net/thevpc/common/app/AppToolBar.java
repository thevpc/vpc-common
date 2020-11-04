package net.thevpc.common.app;

import net.thevpc.common.props.WritablePValue;

public interface AppToolBar extends AppToolContainer {
    WritablePValue<Boolean> visible();
}

package net.thevpc.common.app;

import net.thevpc.common.props.WritablePValue;

public interface AppStatusBar extends AppToolContainer {

    WritablePValue<Boolean> visible();
}

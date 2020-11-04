package net.thevpc.common.app;

import net.thevpc.common.props.WritablePValue;

public interface AppMenuBar extends AppToolContainer {
    WritablePValue<Boolean> visible();
}

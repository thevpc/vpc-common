package net.thevpc.common.app;

import net.thevpc.common.props.WritablePValue;

public interface AppToolCheckBox extends AppTool{
    String id();

    WritablePValue<String> group();

    WritablePValue<Boolean> selected();
}

package net.thevpc.common.app;

import net.thevpc.common.props.WritablePValue;

public interface AppToolRadioBox extends AppTool {
    String id();

    WritablePValue<String> group();

    WritablePValue<Boolean> selected();
}

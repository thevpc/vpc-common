package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

public interface AppToolRadioBox extends AppTool {
    String id();

    WritablePValue<String> group();

    WritablePValue<Boolean> selected();
}

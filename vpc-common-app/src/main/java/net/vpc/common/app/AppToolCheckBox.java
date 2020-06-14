package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

public interface AppToolCheckBox extends AppTool{
    String id();

    WritablePValue<String> group();

    WritablePValue<Boolean> selected();
}

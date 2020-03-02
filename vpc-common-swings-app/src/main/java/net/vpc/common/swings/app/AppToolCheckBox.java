package net.vpc.common.swings.app;

import net.vpc.common.prpbind.WritablePValue;

public interface AppToolCheckBox extends AppTool{
    String id();

    WritablePValue<String> group();

    WritablePValue<Boolean> selected();
}

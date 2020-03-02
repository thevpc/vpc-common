package net.vpc.common.swings.app;

import net.vpc.common.prpbind.WritablePValue;

public interface AppToolRadioBox extends AppTool {
    String id();

    WritablePValue<String> group();

    WritablePValue<Boolean> selected();
}

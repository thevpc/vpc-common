package net.vpc.common.swings.app;

import net.vpc.common.prpbind.WritablePValue;

public interface AppToolSeparator extends AppTool {
    WritablePValue<Integer> width();

    WritablePValue<Integer> height();

    ItemPath path();
}

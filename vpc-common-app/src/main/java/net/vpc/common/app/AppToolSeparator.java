package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

public interface AppToolSeparator extends AppTool {
    WritablePValue<Integer> width();

    WritablePValue<Integer> height();

    ItemPath path();
}

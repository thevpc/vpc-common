package net.vpc.common.swings.app;

import net.vpc.common.prpbind.WritablePValue;

import java.awt.event.ActionListener;

public interface AppToolAction extends AppTool{
    String id();

    WritablePValue<ActionListener> action();
}

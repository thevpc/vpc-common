package net.vpc.common.app;

import net.vpc.common.props.WritablePValue;

import java.awt.event.ActionListener;

public interface AppToolAction extends AppTool{
    String id();

    WritablePValue<ActionListener> action();
}

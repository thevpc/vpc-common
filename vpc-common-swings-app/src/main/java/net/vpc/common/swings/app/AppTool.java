package net.vpc.common.swings.app;

import net.vpc.common.prpbind.WritablePValue;

import javax.swing.*;

public interface AppTool{
    String id();

    WritablePValue<ImageIcon> smallIcon();

    WritablePValue<ImageIcon> largeIcon();

    WritablePValue<Boolean> enabled();

    WritablePValue<Boolean> visible();

    WritablePValue<String> title();
}

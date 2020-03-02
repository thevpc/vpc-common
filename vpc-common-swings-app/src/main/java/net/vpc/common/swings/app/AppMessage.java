package net.vpc.common.swings.app;

import java.util.logging.Level;

public interface AppMessage {
    Level getLevel();

    String getText();
}

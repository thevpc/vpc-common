package net.vpc.common.mon;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public interface TaskMessage {
    Level getLevel();

    String getText();
}

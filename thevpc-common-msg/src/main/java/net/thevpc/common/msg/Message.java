package net.thevpc.common.msg;

import java.util.logging.Level;

public interface Message {
    Level getLevel();

    String getText();
}

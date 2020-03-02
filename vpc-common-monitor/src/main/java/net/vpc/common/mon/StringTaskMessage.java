package net.vpc.common.mon;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class StringTaskMessage implements TaskMessage {
    private Level level;
    private String message;

    public StringTaskMessage(Level level, String message) {

        this.message = message;
        this.level = level;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public String getText() {
        return message;
    }

    public String toString(){
        return message;
    }
}

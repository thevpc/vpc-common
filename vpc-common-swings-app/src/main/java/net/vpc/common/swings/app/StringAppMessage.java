package net.vpc.common.swings.app;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class StringAppMessage implements AppMessage {
    private Level level;
    private String message;

    public StringAppMessage(Level level, String message) {

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

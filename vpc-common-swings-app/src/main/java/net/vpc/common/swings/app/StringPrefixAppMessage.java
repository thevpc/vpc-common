package net.vpc.common.swings.app;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class StringPrefixAppMessage implements AppMessage {
    private String prefix;
    private AppMessage message;

    public StringPrefixAppMessage(String prefix, AppMessage message) {

        this.message = message;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public AppMessage getMessage() {
        return message;
    }

    @Override
    public Level getLevel() {
        return message.getLevel();
    }

    @Override
    public String getText() {
        return prefix+message.getText();
    }

    public String toString(){
        return getText();
    }
}

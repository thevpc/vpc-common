package net.vpc.common.mon;

import java.util.logging.Level;

/**
 * Created by vpc on 3/20/17.
 */
public class StringPrefixTaskMessage implements TaskMessage {
    private String prefix;
    private TaskMessage message;

    public StringPrefixTaskMessage(String prefix, TaskMessage message) {

        this.message = message;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public TaskMessage getMessage() {
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

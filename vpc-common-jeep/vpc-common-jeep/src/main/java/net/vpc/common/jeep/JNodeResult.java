package net.vpc.common.jeep;

import net.vpc.common.textsource.log.JMessageList;

/**
 * parse functions mainly return a Node within a list of messages
 * whether or not the node can be parsed.
 * @param <T>
 */
public class JNodeResult<T extends JNode> {
    private T node;
    private JMessageList messages;

    public JNodeResult(T node, JMessageList messages) {
        this.node = node;
        this.messages = messages;
    }

    public T getNode() {
        return node;
    }

    public JMessageList getMessages() {
        return messages;
    }
}

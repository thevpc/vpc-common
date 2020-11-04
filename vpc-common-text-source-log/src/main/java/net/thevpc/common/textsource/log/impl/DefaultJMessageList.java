package net.thevpc.common.textsource.log.impl;

import net.thevpc.common.textsource.JTextSourceToken;
import net.thevpc.common.textsource.log.JMessageList;
import net.thevpc.common.textsource.log.JSourceMessage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultJMessageList implements JMessageList {
    private List<JSourceMessage> messages = new ArrayList<>();

    @Override
    public void info(String id, String group, String message, JTextSourceToken token) {
        add(JSourceMessage.info(id, group, message, token));
    }

    @Override
    public void error(String id, String group, String message, JTextSourceToken token) {
        add(JSourceMessage.error(id, group, message, token));
    }

    @Override
    public void warn(String id, String group, String message, JTextSourceToken token) {
        add(JSourceMessage.warning(id, group, message, token));
    }

    @Override
    public void add(JSourceMessage message) {
        this.messages.add(message);
    }

    @Override
    public void addAll(Iterable<JSourceMessage> messages) {
        if (messages != null) {
            for (JSourceMessage message : messages) {
                add(message);
            }
        }
    }

    @Override
    public JMessageList clear() {
        this.messages.clear();
        return this;
    }

    @Override
    public Iterator<JSourceMessage> iterator() {
        return messages.iterator();
    }
}

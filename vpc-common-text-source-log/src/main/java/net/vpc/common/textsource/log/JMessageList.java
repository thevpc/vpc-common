package net.vpc.common.textsource.log;

import net.vpc.common.textsource.JTextSourceToken;

public interface JMessageList extends Iterable<JSourceMessage>{
    void info(String id, String group, String message, JTextSourceToken token);

    void error(String id, String group, String message, JTextSourceToken token);

    void warn(String id, String group, String message, JTextSourceToken token);

    void add(JSourceMessage message);

    void addAll(Iterable<JSourceMessage> messages);

    JMessageList clear();
}

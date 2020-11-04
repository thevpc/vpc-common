package net.thevpc.common.textsource;

public interface JTextSourceReport {
    void reportError(String id, String group, String message);

    void reportWarning(String id, String group, String message);

}

package net.thevpc.common.strings;

import java.util.List;

public interface StringCollection {

    void clear();

    boolean contains(String value);

    void add(String value);

    void remove(String value);

    List<String> getValues();

    int size();
}

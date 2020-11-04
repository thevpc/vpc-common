package net.thevpc.common.commandline.format;

import java.util.List;

public interface TreeModel<T> {

    T getRoot();

    List<T> getChildren(T o);
}

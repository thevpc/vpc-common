package net.thevpc.common.props;

public interface WritableListIndexSelection<T> extends WritableList<T>, ObservableListIndexSelection<T> {
    WritableList<Integer> indices();
}

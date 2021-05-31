package net.thevpc.common.props;

public interface ObservableListIndexSelection<T> extends ObservableList<T>{
    ObservableList<Integer> indices();
    ObservableListIndexSelection<T> readOnly();
}

package net.thevpc.common.props;

public interface ObservableListSelection<T> extends ObservableList<T> {
    ObservableBoolean multipleSelection();
    ObservableBoolean noSelection();
    ObservableListSelection<T> readOnly();
}

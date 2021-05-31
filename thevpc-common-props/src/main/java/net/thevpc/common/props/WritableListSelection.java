package net.thevpc.common.props;

public interface WritableListSelection<T> extends WritableList<T>,
        ObservableListSelection<T>{
    WritableBoolean multipleSelection();
    WritableBoolean noSelection();
}

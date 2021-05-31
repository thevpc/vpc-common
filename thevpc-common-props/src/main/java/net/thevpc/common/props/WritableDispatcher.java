package net.thevpc.common.props;

public interface WritableDispatcher<T> extends ObservableDispatcher<T>,WritableProperty{

    void add(T item);


}

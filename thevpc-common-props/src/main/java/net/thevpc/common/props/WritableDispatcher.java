package net.thevpc.common.props;

public interface WritableDispatcher<T> extends ObservableDispatcher<T>{

    void add(T item);


}

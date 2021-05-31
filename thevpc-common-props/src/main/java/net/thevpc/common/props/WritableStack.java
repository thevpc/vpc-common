package net.thevpc.common.props;


import java.util.function.Predicate;

public interface WritableStack<T> extends ObservableStack<T>,WritableProperty {

    T popIf(Predicate<T> a);

    void push(T v);

    T pop();

    void popAll();


    PropertyVetos vetos();

}

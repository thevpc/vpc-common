package net.vpc.common.props;


import java.util.function.Predicate;

public interface WritablePStack<T> extends PStack<T> {

    T popIf(Predicate<T> a);

    void push(T v);

    T pop();

    void popAll();


    PropertyVetos vetos();

}

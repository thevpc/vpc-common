package net.vpc.common.prpbind;


import java.util.function.Predicate;

public interface WritablePStack<T> extends PStack<T> {

    T popIf(Predicate<T> a);

    void push(T v);

    T pop();

    void popAll();

    PStack<T> readOnly();

    PropertyVetos vetos();

}

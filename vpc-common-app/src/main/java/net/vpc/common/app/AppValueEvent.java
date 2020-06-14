package net.vpc.common.app;


public interface AppValueEvent<T> extends AppEvent {
    T getValue();
}

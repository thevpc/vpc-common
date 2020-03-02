package net.vpc.common.swings.app;


public interface AppValueEvent<T> extends AppEvent {
    T getValue();
}

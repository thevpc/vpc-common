package net.thevpc.common.app;


public interface AppValueEvent<T> extends AppEvent {
    T getValue();
}

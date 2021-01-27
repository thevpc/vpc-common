package net.thevpc.common.swing.win;

public interface WindowInfoListener {
    default void onAddFrame(WindowInfo windowInfo){}
    default void onCloseFrame(WindowInfo windowInfo){}
}

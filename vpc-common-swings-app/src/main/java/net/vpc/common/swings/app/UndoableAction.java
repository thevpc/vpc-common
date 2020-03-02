package net.vpc.common.swings.app;

public interface UndoableAction {
    String getDescription();

    void doAction(AppEvent event);

    void undoAction(AppEvent event);
}

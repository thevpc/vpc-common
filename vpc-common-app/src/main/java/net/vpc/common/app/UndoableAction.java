package net.vpc.common.app;

import net.vpc.common.msg.Message;

public interface UndoableAction {

    Message doAction(AppEvent event);

    void redoAction(AppEvent event);

    void undoAction(AppEvent event);
}

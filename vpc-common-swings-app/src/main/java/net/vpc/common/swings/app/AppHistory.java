package net.vpc.common.swings.app;

import net.vpc.common.prpbind.PList;

public interface AppHistory {
    void doAction(UndoableAction action);

    void undoAction();

    PList<String> description();
}

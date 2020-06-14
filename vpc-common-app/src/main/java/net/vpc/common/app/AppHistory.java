package net.vpc.common.app;

import net.vpc.common.props.PList;
import net.vpc.common.msg.Message;

public interface AppHistory {

    void doAction(UndoableAction action);

    void undoAction();

    void redoAction();

    PList<Message> undoList();

    PList<Message> redoList();

}

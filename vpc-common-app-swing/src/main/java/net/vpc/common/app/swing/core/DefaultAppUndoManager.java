package net.vpc.common.app.swing.core;

import net.vpc.common.props.*;
import net.vpc.common.props.impl.WritablePStackImpl;
import net.vpc.common.app.AppHistory;
import net.vpc.common.app.Application;
import net.vpc.common.app.UndoableAction;
import net.vpc.common.msg.Message;

public class DefaultAppUndoManager implements AppHistory {

    private Application application;
    private int maxEntries = 1000;
    protected WritablePStack<UndoableAction> undoList = Props.of("undo").stackOf(UndoableAction.class);
    protected WritablePList<Message> undoListDescription = Props.of("undoDescription").listOf(Message.class);
    protected WritablePStack<UndoableAction> redoList = Props.of("redo").stackOf(UndoableAction.class);
    protected WritablePList<Message> redoListDescription = Props.of("redDescription").listOf(Message.class);

    public DefaultAppUndoManager(Application application) {
        this.application = application;
    }

    @Override
    public void doAction(UndoableAction action) {
        Message m = action.doAction(new DefaultAppEvent(application, null));
        if (m != null) {
            undoList.push(action);
            undoListDescription.add(m);
            if (maxEntries > 0) {
                while (undoList.size() > maxEntries) {
                    discardUndoFirstAction();
                }
            }
        }
    }

    public void discardUndoFirstAction() {
        ((WritablePStackImpl<UndoableAction>) undoList).remove(0);
        undoListDescription.remove(0);
    }

    public void discardRedoFirstAction() {
        ((WritablePStackImpl<UndoableAction>) redoList).remove(0);
        redoListDescription.remove(0);
    }

    @Override
    public void undoAction() {
        UndoableAction poped = undoList.pop();
        Message msg = undoListDescription.remove(undoListDescription.size() - 1);
        poped.undoAction(new DefaultAppEvent(application, null));

        redoList.push(poped);
        redoListDescription.add(msg);

        if (maxEntries > 0) {
            while (redoList.size() > maxEntries) {
                discardRedoFirstAction();
            }
        }
    }

    @Override
    public void redoAction() {
        UndoableAction action = redoList.pop();
        Message msg = redoListDescription.remove(redoListDescription.size() - 1);
        action.redoAction(new DefaultAppEvent(application, null));
        undoList.push(action);
        undoListDescription.add(msg);
        if (maxEntries > 0) {
            while (undoList.size() > maxEntries) {
                discardUndoFirstAction();
            }
        }
    }

    @Override
    public PList<Message> undoList() {
        return undoListDescription.readOnly();
    }

    @Override
    public PList<Message> redoList() {
        return redoListDescription.readOnly();
    }

}

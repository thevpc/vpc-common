package net.vpc.common.swings.app.core;

import net.vpc.common.prpbind.*;
import net.vpc.common.prpbind.impl.WritablePStackImpl;
import net.vpc.common.swings.app.AppHistory;
import net.vpc.common.swings.app.Application;
import net.vpc.common.swings.app.UndoableAction;

public class DefaultAppUndoManager implements AppHistory {
    private Application application;
    private int maxEntries=1000;
    protected WritablePStack<UndoableAction> history = Props.of("history").stackOf(UndoableAction.class);
    protected WritablePList<String> historyNames = Props.of("description").listOf(String.class);

    public DefaultAppUndoManager(Application application) {
        this.application = application;
        history.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()) {
                    case ADD: {
                        UndoableAction newValue = event.getNewValue();
                        historyNames.add(newValue.getDescription());
                        if(maxEntries>0) {
                            while (history.size() > maxEntries) {
                                ((WritablePStackImpl<UndoableAction>) history).remove(0);
                            }
                        }
                        break;
                    }
                    case REMOVE: {
                        UndoableAction newValue = event.getNewValue();
                        historyNames.remove(event.getIndex());
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void doAction(UndoableAction action) {
        history.push(action);
        action.doAction(new DefaultAppEvent(application, null));
    }

    @Override
    public void undoAction() {
        UndoableAction poped = history.pop();
        poped.undoAction(new DefaultAppEvent(application, null));
    }

    @Override
    public PList<String> description() {
        return historyNames;
    }

}

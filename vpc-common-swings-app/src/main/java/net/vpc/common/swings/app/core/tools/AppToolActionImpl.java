package net.vpc.common.swings.app.core.tools;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.swings.app.AppToolAction;
import net.vpc.common.swings.app.core.AbstractAppTool;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AppToolActionImpl extends AbstractAppTool implements AppToolAction {
    private String id;
    private WritablePValue<ActionListener> action = Props.of("action").valueOf(ActionListener.class, null);
    private SwingToProp swingToProp = new SwingToProp();

    public AppToolActionImpl(String id, ActionListener action) {
        super(id);
        this.id = id;
        this.action.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()) {
                    case UPDATE: {
                        Object oldAction = event.getOldValue();
                        if (oldAction instanceof Action) {
                            ((Action) oldAction).removePropertyChangeListener(swingToProp);
                            title().listeners().removeIf(x -> x instanceof PropToSwing);
                        }
                        Object newAction = event.getNewValue();
                        if (newAction instanceof Action) {
                            ((Action) newAction).addPropertyChangeListener(swingToProp);
                            title().listeners().add(new PropToSwing(newAction));
                        }
                        break;
                    }
                }
            }
        });
        this.action.set(action);

    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public WritablePValue<ActionListener> action() {
        return action;
    }

    private static class PropToSwing implements PropertyListener {
        private final Object newAction;

        public PropToSwing(Object newAction) {
            this.newAction = newAction;
        }

        @Override
        public void propertyUpdated(PropertyEvent event) {
            switch (event.getProperty().getPropertyName()) {
                case "title": {
                    ((Action) newAction).putValue(Action.SHORT_DESCRIPTION, event.getNewValue());
                    break;
                }
                case "smallIcon": {
                    ((Action) newAction).putValue(Action.SMALL_ICON, event.getNewValue());
                    break;
                }
            }
        }
    }

    private class SwingToProp implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            switch (evt.getPropertyName()) {
                case Action.SHORT_DESCRIPTION: {
                    title().set((String) evt.getNewValue());
                    break;
                }
                case Action.SMALL_ICON: {
                    Icon newValue = (Icon) evt.getNewValue();
                    if (newValue == null || (newValue instanceof ImageIcon)) {
                        smallIcon().set((ImageIcon) newValue);
                    }
                    break;
                }
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app.swing.actions;

import java.util.function.Supplier;
import java.util.logging.Level;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.app.AppEvent;
import net.vpc.common.app.UndoableAction;
import net.vpc.common.msg.FormattedMessage;
import net.vpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class PropUndoableAction<T> implements UndoableAction {

    private WritablePValue<T> prop;
    private Supplier<WritablePValue<T>> propSupp;
    private Supplier<T> newValue;
    private T oldValue;
    private String messageTemplate;

    public PropUndoableAction(Supplier<T> newValue, Supplier<WritablePValue<T>> propSupp, String messageTemplate) {
        this.propSupp = propSupp;
        this.newValue = newValue;
        this.messageTemplate = messageTemplate;
    }

    @Override
    public Message doAction(AppEvent event) {
        prop = propSupp.get();
        if (prop == null) {
            return null;
        }
        oldValue = prop.get();
        prop.set(newValue.get());
        postDo();
        return new FormattedMessage(Level.INFO, messageTemplate, new Object[]{prop.get()});
    }

    protected void postDo() {

    }

    protected void postUndo() {

    }

    @Override
    public void undoAction(AppEvent event) {
        prop.set(oldValue);
        postUndo();
    }

    @Override
    public void redoAction(AppEvent event) {
        prop.set(newValue.get());
        postDo();
    }

}

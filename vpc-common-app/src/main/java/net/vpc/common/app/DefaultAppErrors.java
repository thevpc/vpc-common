/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app;

import net.vpc.common.msg.ExceptionMessage;
import java.util.logging.Level;
import net.vpc.common.props.PDispatcher;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePDispatcher;
import net.vpc.common.props.impl.DelegateProperty;
import net.vpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class DefaultAppErrors extends DelegateProperty<Message> implements AppErrors {

    private final Application app;

    public DefaultAppErrors(Application app) {
        super(Props.of("errors").dispatcherOf(Message.class));
        this.app = app;
    }

    @Override
    public void add(String item) {
        if (item != null) {
            add(new RuntimeException(item));
        }
    }

    @Override
    public void add(Throwable item) {
        if (item != null) {
            add(new ExceptionMessage(Level.SEVERE, null, item));
        }
    }

    @Override
    public void add(Message item) {
        if (item != null) {
            base().add(item);
        }
    }

    @Override
    public PDispatcher<Message> readOnly() {
        return base().readOnly();
    }

    private WritablePDispatcher<Message> base() {
        return (WritablePDispatcher<Message>) base;
    }
}

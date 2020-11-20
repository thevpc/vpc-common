/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.app;

import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePList;
import net.thevpc.common.props.impl.ReadOnlyPList;
import net.thevpc.common.msg.Message;

/**
 *
 * @author vpc
 */
public class DefaultAppLogs extends ReadOnlyPList<Message> implements AppLogs {

    private int maxMessageEntries = 1000;
    private Application app;

    public DefaultAppLogs(Application app) {
        super(Props.of("logs").linkedListOf(Message.class));
        this.app = app;
        base().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()) {
                    case ADD: {
                        while (maxMessageEntries > 0 && base().size() > maxMessageEntries) {
                            base().remove(0);
                        }
                        break;
                    }
                }
            }
        });
    }

    private WritablePList<Message> base() {
        return (WritablePList<Message>) base;
    }

    @Override
    public void add(Message message) {
        base().add(message);
    }
}
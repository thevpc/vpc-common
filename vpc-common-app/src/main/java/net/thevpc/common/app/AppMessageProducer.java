package net.thevpc.common.app;

import net.thevpc.common.props.WritablePList;
import net.thevpc.common.msg.Message;

public interface AppMessageProducer {
    void produceMessages(Application application, WritablePList<Message> messages);
}

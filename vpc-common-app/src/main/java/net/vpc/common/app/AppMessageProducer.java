package net.vpc.common.app;

import net.vpc.common.props.WritablePList;
import net.vpc.common.msg.Message;

public interface AppMessageProducer {
    void produceMessages(Application application, WritablePList<Message> messages);
}

package net.vpc.common.swings.app;

import net.vpc.common.prpbind.WritablePList;

public interface AppMessageProducer {
    void produceMessages(Application application, WritablePList<AppMessage> messages);
}

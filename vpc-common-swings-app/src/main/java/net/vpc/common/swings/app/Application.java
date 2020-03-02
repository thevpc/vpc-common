package net.vpc.common.swings.app;

import net.vpc.common.prpbind.*;

public interface Application extends PropertyContainer {

    PValue<String> name();

    PValue<AppStatus> status();

    void start();

    void shutdown();

    AppTools tools();

    AppWindow mainWindow();

    AppNode[] nodes();

    ApplicationBuilder builder();

    AppHistory history();

    PList<AppMessage> messages();

    void updateMessages();

    WritablePList<AppMessage> log();

    WritablePList<AppMessageProducer> messageProducers();


}

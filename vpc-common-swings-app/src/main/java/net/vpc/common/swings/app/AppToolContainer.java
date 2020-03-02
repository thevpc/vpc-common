package net.vpc.common.swings.app;

import net.vpc.common.prpbind.PropertyContainer;

public interface AppToolContainer extends PropertyContainer, AppComponent {
    AppNode rootNode();

    AppTools tools();

}

package net.vpc.common.app;

import net.vpc.common.props.PropertyContainer;

public interface AppToolContainer extends PropertyContainer, AppComponent {
    AppNode rootNode();

    AppTools tools();

}

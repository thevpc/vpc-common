package net.thevpc.common.props;


import net.thevpc.common.props.impl.AppPropertyBinding;

public interface PropertyContainer extends WithListeners{
    AppPropertyBinding[] getProperties();

    PropertyListeners listeners();
}

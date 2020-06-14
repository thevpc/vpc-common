package net.vpc.common.props;


import net.vpc.common.props.impl.AppPropertyBinding;

public interface PropertyContainer extends WithListeners{
    AppPropertyBinding[] getProperties();

    PropertyListeners listeners();
}

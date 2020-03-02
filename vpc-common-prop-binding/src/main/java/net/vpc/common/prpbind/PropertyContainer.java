package net.vpc.common.prpbind;


import net.vpc.common.prpbind.impl.AppPropertyBinding;

public interface PropertyContainer extends WithListeners{
    AppPropertyBinding[] getProperties();

    PropertyListeners listeners();
}

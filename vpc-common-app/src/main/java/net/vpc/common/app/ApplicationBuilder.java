package net.vpc.common.app;

import net.vpc.common.props.PValue;

public interface ApplicationBuilder {
    PValue<AppWindowBuilder> mainWindowBuilder();
}

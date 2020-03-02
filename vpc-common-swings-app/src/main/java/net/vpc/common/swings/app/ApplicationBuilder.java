package net.vpc.common.swings.app;

import net.vpc.common.prpbind.PValue;

public interface ApplicationBuilder {
    PValue<AppWindowBuilder> mainWindowBuilder();
}

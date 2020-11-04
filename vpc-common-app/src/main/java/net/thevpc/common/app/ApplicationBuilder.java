package net.thevpc.common.app;

import net.thevpc.common.props.PValue;

public interface ApplicationBuilder {
    PValue<AppWindowBuilder> mainWindowBuilder();
}

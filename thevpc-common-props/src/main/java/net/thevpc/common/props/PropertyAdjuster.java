package net.thevpc.common.props;

import net.thevpc.common.props.impl.PropertyAdjusterContext;

public interface PropertyAdjuster {
    void adjust(PropertyAdjusterContext context);
}

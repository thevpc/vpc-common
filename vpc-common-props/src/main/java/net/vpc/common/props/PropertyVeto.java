package net.vpc.common.props;

public interface PropertyVeto {
    void vetoableChange(PropertyEvent event);
}

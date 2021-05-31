package net.thevpc.common.props;

public interface WritableProperty extends Property {
    //    @Override
    PropertyAdjusters adjusters();

    PropertyVetos vetos();
}

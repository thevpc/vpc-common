package net.thevpc.common.props;

import net.thevpc.common.props.impl.DisabledSelectionStrategy;

import java.util.function.Predicate;

public class ReadOnlyListIndexSelectionExt<T>
        extends ReadOnlyListIndexSelection<T>
        implements ObservableListIndexSelectionExt<T> {

    public ReadOnlyListIndexSelectionExt(ObservableListIndexSelectionExt<T> other) {
        super(other);
    }

    @Override
    public ObservableBoolean multipleSelection() {
        return (roBase()).multipleSelection().readOnly();
    }

    @Override
    public ObservableBoolean noSelection() {
        return roBase().noSelection().readOnly();
    }

    private ReadOnlyListIndexSelectionExt<T> roBase() {
        return (ReadOnlyListIndexSelectionExt) base;
    }

    @Override
    public ObservableValue<Predicate<T>> disablePredicate() {
        return roBase().disablePredicate().readOnly();
    }

    @Override
    public ObservableValue<Predicate<T>> effectiveDisablePredicate() {
        return roBase().effectiveDisablePredicate();
    }

    @Override
    public ObservableValue<DisabledSelectionStrategy> disableSelectionStrategy() {
        return roBase().disableSelectionStrategy().readOnly();
    }

    @Override
    public ObservableList<T> disabledValues() {
        return roBase().disabledValues().readOnly();
    }

    @Override
    public ObservableListIndexSelectionExt<T> readOnly() {
        return this;
    }
}

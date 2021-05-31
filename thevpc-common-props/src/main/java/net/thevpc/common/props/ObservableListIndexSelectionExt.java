package net.thevpc.common.props;

import net.thevpc.common.props.impl.DisabledSelectionStrategy;

import java.util.function.Predicate;

public interface ObservableListIndexSelectionExt<T>
        extends ObservableListIndexSelection<T>,
        ObservableListSelection<T> {
    ObservableBoolean multipleSelection();

    ObservableBoolean noSelection();

    ObservableValue<Predicate<T>> disablePredicate();

    ObservableList<T> disabledValues();

    ObservableValue<Predicate<T>> effectiveDisablePredicate();

    ObservableListIndexSelectionExt<T> readOnly();

    ObservableValue<DisabledSelectionStrategy> disableSelectionStrategy();

}

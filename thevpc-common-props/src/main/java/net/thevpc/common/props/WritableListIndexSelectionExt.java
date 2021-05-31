package net.thevpc.common.props;

import net.thevpc.common.props.impl.DisabledSelectionStrategy;

import java.util.function.Predicate;

public interface WritableListIndexSelectionExt<T>
        extends WritableListIndexSelection<T>,
        ObservableListIndexSelectionExt<T>,
        WritableListSelection<T> {

    WritableBoolean multipleSelection();

    WritableBoolean noSelection();

    WritableValue<Predicate<T>> disablePredicate();

    WritableList<T> disabledValues();

    WritableValue<DisabledSelectionStrategy> disableSelectionStrategy();

}

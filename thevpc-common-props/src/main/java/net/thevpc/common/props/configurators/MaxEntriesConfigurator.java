package net.thevpc.common.props.configurators;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.WritableList;

public class MaxEntriesConfigurator {
    public static <T> void ensureMaxEntries(WritableList<T> t, int maxMessageEntries) {
        while (maxMessageEntries > 0 && t.size() > maxMessageEntries) {
            t.removeAt(0);
        }
    }
    public static <T> void configureMaxEntries(WritableList<T> t, ObservableValue<Integer> maxMessageEntries) {
        OptionsConfigurator.configureObservableOptions(t,maxMessageEntries, (li, max) -> ensureMaxEntries(li, max-1));
    }

    public static <T> void configureMaxEntries(WritableList<T> t, int maxMessageEntries) {
        OptionsConfigurator.configureOptions(t,maxMessageEntries, (li, m) -> ensureMaxEntries(li, m-1));
    }
}

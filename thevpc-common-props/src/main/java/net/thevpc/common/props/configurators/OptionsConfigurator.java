package net.thevpc.common.props.configurators;

import net.thevpc.common.props.*;

public class OptionsConfigurator {
    public static <T, O> void configureOptions(WritableList<T> list, O options, OptionsApplier<T, O> optionsApplier) {
        optionsApplier.applyOptions(list, options);
        list.adjusters().add(context -> {
            if (context.event().eventType() == PropertyUpdate.ADD) {
                context.getBefore().add(() -> optionsApplier.applyOptions(list, options));
            }
        });
    }
    public static <T, O> void configureObservableOptions(WritableList<T> list, ObservableValue<O> options, OptionsApplier<T, O> optionsApplier) {
        optionsApplier.applyOptions(list, options.get());
        options.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                optionsApplier.applyOptions(list, options.get());
            }
        });
        list.adjusters().add(context -> {
            if (context.event().eventType() == PropertyUpdate.ADD) {
                context.getBefore().add(() -> optionsApplier.applyOptions(list, options.get()));
            }
        });
    }

    public interface OptionsApplier<T, O>{
        void applyOptions(WritableList<T> t, O options);
    }
}

package net.thevpc.common.props.configurators;

import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.WritableList;

import java.util.HashSet;

public class SetConfigurator {
    public static <T> void configureSet(WritableList<T> list) {
        HashSet<T> nonDuplicateValue=new HashSet<>();
        list.adjusters().add(context -> {
            T newValue = context.event().newValue();
            switch (context.event().eventType()) {
                case ADD: {
                    if (nonDuplicateValue.contains(newValue)) {
                        context.getBefore().clear();
                        context.getAfter().clear();
                        context.setIgnore(true);
                        context.setStop(true);
                    }
                    break;
                }
                case UPDATE: {
                    if (nonDuplicateValue.contains(newValue)) {
                        T oldValue = context.event().oldValue();
                        int oldIndex = list.findFirstIndexOf(oldValue);
                        int newIndex = context.event().index();
                        if (oldIndex < newIndex) {
                            context.getBefore().add(() -> list.remove(oldValue));
                            context.setIgnore(true);
                            context.getAfter().add(() -> list.set(newIndex - 1, newValue));
                        } else {
                            context.getBefore().add(() -> list.remove(oldValue));
                        }
                    }
                    break;
                }
            }
        });
        list.onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                T newValue = event.newValue();
                T oldValue = event.oldValue();
                switch (event.eventType()){
                    case ADD:{
                        nonDuplicateValue.add(newValue);
                        break;
                    }
                    case REMOVE:{
                        nonDuplicateValue.remove(oldValue);
                        break;
                    }
                    case UPDATE:{
                        nonDuplicateValue.remove(oldValue);
                        nonDuplicateValue.add(newValue);
                        break;
                    }
                }
            }
        });
    }

}

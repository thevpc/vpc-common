package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.util.stream.Collectors;

public class WritableListSelectionImpl<T> extends WritableSetImpl<T> implements WritableListSelection<T> {
    private WritableBoolean multipleSelection=Props.of("multipleSelection").booleanOf(true);
    private WritableBoolean noSelection=Props.of("noSelection").booleanOf(true);
    public WritableListSelectionImpl(String name, PropertyType elementType) {
        super(name, elementType);
        WritableListSelectionImpl<T> THIS = WritableListSelectionImpl.this;
        adjusters().add(context -> {
            if(context.event().eventType()==PropertyUpdate.ADD) {
                if (multipleSelection().get()) {
                    if(THIS.size()>0){
                        context.doInstead(
                                ()->THIS.set(THIS.size()-1,(T) context.newValue())
                        );
                    }
                }
            }
        });
        multipleSelection.onChange(event -> {
            boolean multi = event.newValue();
            if(!multi){
                while (size()>1){
                    removeAt(0);
                }
            }
        });
    }
    @Override
    public ObservableListSelection<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyListSelection<T>(this);
        }
        return (ReadOnlyListSelection<T>) ro;
    }

    @Override
    public WritableBoolean multipleSelection() {
        return multipleSelection;
    }

    @Override
    public WritableBoolean noSelection() {
        return noSelection;
    }

    @Override
    public String toString() {
        String p=isWritable()?"Writable":"ReadOnly";
        return p+"ListSelection("+fullPropertyName()+"){"
                + "multipleSelection=" + multipleSelection().get()
                + ", noSelection=" + noSelection().get()
                + ", values=[" + stream().map(Object::toString).collect(Collectors.joining(",")) + "]"
                + '}';
    }
}

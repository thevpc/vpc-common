package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.prpbind.PList;
import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;

import javax.swing.*;

public class PListComponentModel<T> extends AbstractListModel<T> {
    private PList<T> items;

    public PListComponentModel(PList<T> items) {
        super();
        this.items = items;
        items.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                switch (event.getAction()){
                    case ADD:{
                        fireIntervalAdded(PListComponentModel.this,event.getIndex(),event.getIndex());
                        break;
                    }
                    case REMOVE:{
                        fireIntervalRemoved(PListComponentModel.this,event.getIndex(),event.getIndex());
                        break;
                    }
                    case UPDATE:{
                        fireContentsChanged(PListComponentModel.this,event.getIndex(),event.getIndex());
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public T getElementAt(int index) {
        return items.get(index);
    }
}

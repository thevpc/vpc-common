package net.thevpc.common.props.impl;

import net.thevpc.common.props.*;

import java.util.ArrayList;
import java.util.List;

public class PropertyAdjustersImpl implements PropertyAdjusters {
    protected Property source;
    protected List<PropertyAdjuster> adjusters;
    protected boolean readOnly;

    public PropertyAdjustersImpl(Property source) {
        this.source = source;
    }

    public void readOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean readOnly() {
        return this.readOnly;
    }

    public void add(PropertyAdjuster listener) {
        if (readOnly) {
            throw new IllegalArgumentException("Read ONly");
        }
        if (listener != null) {
            if (adjusters == null) {
                adjusters = new ArrayList<>();
            }
            if (!adjusters.contains(listener)) {
                adjusters.add(listener);
            }
        }
    }

    public void remove(PropertyAdjuster listener) {
        if (readOnly) {
            throw new IllegalArgumentException("Read ONly");
        }
        if (listener != null) {
            if (adjusters != null) {
                adjusters.remove(listener);
            }
        }
    }

    public PropertyAdjuster[] getAll() {
        return adjusters == null ? new PropertyAdjuster[0] : adjusters.toArray(new PropertyAdjuster[0]);
    }

    public PropertyAdjusterContext firePropertyUpdated(PropertyEvent event) {
        PropertyAdjusterContext e2 = new PropertyAdjusterContext();
        e2.setEvent(event);
        adjust(e2);
        return e2;
    }

    public void adjust(PropertyAdjusterContext e2) {
        if (adjusters != null) {
            for (PropertyAdjuster adjuster : adjusters) {
                adjuster.adjust(e2);
                if(e2.isStop()){
                    break;
                }
            }
        }
    }

}

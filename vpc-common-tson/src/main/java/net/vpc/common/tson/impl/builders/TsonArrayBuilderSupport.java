package net.vpc.common.tson.impl.builders;

import net.vpc.common.tson.*;
import net.vpc.common.tson.impl.util.TsonUtils;
import net.vpc.common.tson.impl.util.UnmodifiableArrayList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TsonArrayBuilderSupport {
    private ArrayList<List<TsonElement>> rows = new ArrayList<>();
    private ArrayList<TsonElement> current = new ArrayList<>();
    private boolean multiRows = false;

    public void reset() {
        rows.clear();
        current.clear();
        multiRows = false;
    }

    public Iterator<TsonElement> iterator() {
        return getRows().iterator();
    }

    public void add(TsonElementBase element) {
        TsonElement e = Tson.elem(element).build();
        current.add(e);
    }

    public void remove(TsonElementBase element) {
        TsonElement e = Tson.elem(element).build();
        if (!current.remove(e)) {

        }
    }

    public void add(TsonElementBase element, int index) {
        current.add(index, Tson.elem(element).build());
    }

    public void removeAt(int index) {
        current.remove(index);
    }

    public void removeAll() {
        reset();
    }

    public List<TsonElement> getRows() {
        return buildElements();
    }


    public void addAll(TsonElement[] element) {
        for (TsonElement tsonElement : element) {
            add(tsonElement);
        }
    }

    public void addAll(TsonElementBase[] element) {
        for (TsonElementBase tsonElement : element) {
            add(tsonElement);
        }
    }

    public void addAll(Iterable<? extends TsonElementBase> element) {
        for (TsonElementBase tsonElement : element) {
            add(tsonElement);
        }
    }

    public void newRow() {
        multiRows = true;
        rows.add(new ArrayList<>(current));
        current.clear();
    }


    public List<TsonElement> buildElements() {

        if (multiRows) {
            List<TsonElement> e = new ArrayList<>();
            for (List<TsonElement> row : rows) {
                e.add(TsonUtils.toArray(row));
            }
            e.add(TsonUtils.toArray(current));
            return e;
        } else {
            return current;
        }
    }

    public void ensureElementsCapacity(int length) {
        rows.ensureCapacity(length);
        current.ensureCapacity(length);
    }
}

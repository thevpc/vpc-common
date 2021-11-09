/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.list;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author thevpc
 */
public class JComponentList<T> extends JPanel {

    private List<JComponent> allComponents = new ArrayList<>();
    private List<ObjectListModelListener> listeners = new ArrayList<>();
    private JComponentListItem<T> builder;
    ActionListener buttonClickedListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComponent b = (JComponent) e.getSource();
            Object object = b.getClientProperty("object");
            int index = (Integer) b.getClientProperty("index");
            for (ObjectListModelListener listener : listeners) {
                listener.onSelected(object, index);
            }
        }
    };
    private JComponentListLayout listLayout;

    public JComponentList(JComponentListItem builder) {
        this(builder, null);
    }

    public JComponentList(JComponentListItem builder, JComponentListLayout listLayout) {
        super(new BorderLayout());
        setListLayout(listLayout);
        this.builder = builder;
    }

    public JComponentListLayout getListLayout() {
        return listLayout;
    }

    public void setListLayout(JComponentListLayout listLayout) {
        this.listLayout = listLayout == null ? new Vertical() : listLayout;
        rebuild();
    }

    private void rebuild() {
        removeAll();
        this.add(listLayout.doLayout(allComponents.toArray(new JComponent[0]),
                i -> getObject(i)
        ), BorderLayout.PAGE_START);
        invalidate();
        revalidate();
        repaint();
    }

    public void addListener(ObjectListModelListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void setAllObjects(List<T> item) {
        while (objectSize() > item.size()) {
            removeObject(objectSize() - 1);
        }
        for (int i = 0; i < allComponents.size(); i++) {
            JComponent c = allComponents.get(i);
            T value = item.get(i);
            c.putClientProperty("object", value);
            builder.setComponentValue(c, value, i, item.size());
        }
        for (int i = allComponents.size(); i < item.size(); i++) {
            addObject(item.get(i));
        }
    }

    public void addObject(T item) {
        JComponent c = createComponent(item, allComponents.size(), allComponents.size() + 1);
        allComponents.add(c);
        _onComponentAdded(c);
    }

    public Object getObject(int index) {
        JComponent c = allComponents.get(index);
        return builder.getComponentValue(c, index);
    }

    public void setObject(int index, T value) {
        JComponent c = allComponents.get(index);
        builder.setComponentValue(c, value, index, allComponents.size());
    }

    public void removeAllObjects() {
        while (objectSize() > 0) {
            removeObject(0);
        }
    }

    public void removeObject(int index) {
        JComponent c = allComponents.get(index);
        c.putClientProperty("object", null);
        c.putClientProperty("name", null);
        builder.uninstallComponent(c);
        allComponents.remove(index);
        _onComponentRemoved(c);
        for (int i = index; i < allComponents.size(); i++) {
            T value = builder.getComponentValue(c, i);
            builder.setComponentValue(c, value, index, allComponents.size());
        }
    }

    protected JComponent createComponent(T value, int pos, int size) {
        JComponent b = builder.createComponent(pos, size);
        b.putClientProperty("index", pos);

        b.putClientProperty("object", value);
        builder.setComponentValue(b, value, pos, size);
        return b;
    }

    public int objectSize() {
        return allComponents.size();
    }

    private void _onComponentAdded(JComponent c) {
        rebuild();
    }

    private void _onComponentRemoved(JComponent c) {
        rebuild();
    }

    public void setEditable(boolean enabled) {
        for (int i = 0; i < allComponents.size(); i++) {
            builder.setEditable(allComponents.get(i), enabled, i, allComponents.size());
        }
    }

    public static class Vertical extends Simple {

        public Vertical() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        }
    }

    public static class Horizontal extends Simple {

        public Horizontal() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        }
    }

    public static class Grid extends Simple {

        public Grid(int x, int y) {
            setLayout(new GridLayout(
                    x <= 0 ? y <= 0 ? 1 : 0 : x,
                    y <= 0 ? 0 : y
            ));
        }
    }

    public static class GridBag extends Simple {

        public GridBag(boolean vertical) {
            if (vertical) {
                setLayout(new GridBagLayout());
            }
        }

        @Override
        public JComponent doLayout(JComponent[] allComponents, Function<Integer, Object> valueMapper) {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.anchor = GridBagConstraints.PAGE_START;
            for (int i = 0; i < allComponents.length; i++) {
                c.gridy = i;
                this.add(allComponents[i], c.clone());

            }
            c.gridheight = 1;
            c.gridwidth = 1;
            this.add(Box.createVerticalGlue(), c.clone());
            return this;
        }

    }

    public static class Simple extends JPanel implements JComponentListLayout {

        @Override
        public JComponent doLayout(JComponent[] allComponents, Function<Integer, Object> valueMapper) {
            this.removeAll();
            for (int i = 0; i < allComponents.length; i++) {
                this.add(allComponents[i]);
            }
            this.add(Box.createVerticalGlue());
            return this;
        }

    }

    public static class Tab extends JTabbedPane implements JComponentListLayout {

        private Function<Object, String> titleMapper;
        private Function<Object, Icon> iconMapper;

        @Override
        public JComponent doLayout(JComponent[] allComponents, Function<Integer, Object> valueMapper) {
            this.removeAll();
            for (int i = 0; i < allComponents.length; i++) {
                String elementTitle = "Element " + (i + 1);
                Icon icon = null;
                Object o = valueMapper.apply(i);
                if (titleMapper != null) {
                    String t = titleMapper.apply(o);
                    if (t != null) {
                        elementTitle = t;
                    }
                }
                if (iconMapper != null) {
                    Icon t = iconMapper.apply(o);
                    if (t != null) {
                        icon = t;
                    }
                }
                this.addTab(elementTitle, allComponents[i]);
                this.setIconAt(i, icon);
            }
            return this;
        }

    }

}

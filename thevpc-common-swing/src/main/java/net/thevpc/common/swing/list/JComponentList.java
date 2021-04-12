/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.list;

import net.thevpc.common.swing.ObjectListModelListener;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author vpc
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
    private JComponent componentsHolder;

    public JComponentList(JComponentListItem builder) {
        super(new BorderLayout());
        componentsHolder = new JPanel(
        );
        componentsHolder.setLayout(
                                new BoxLayout(componentsHolder, BoxLayout.Y_AXIS)
//                new GridBagLayout()

        );
//        componentsHolder.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        this.add(componentsHolder, BorderLayout.PAGE_START);
        this.builder = builder;
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
//        componentsHolder.add(c);
        _revalidateAll();
    }

    private void _onComponentRemoved(JComponent c) {
//        componentsHolder.remove(c);
        _revalidateAll();
    }

    private void _revalidateAll() {
        componentsHolder.removeAll();
        if (componentsHolder.getLayout() instanceof GridBagLayout) {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
//        c.weighty = 1;
            c.anchor = GridBagConstraints.PAGE_START;
            for (int i = 0; i < allComponents.size(); i++) {
                c.gridy = i;
                componentsHolder.add(allComponents.get(i), c.clone());

            }
            c.gridheight = 1;
            c.gridwidth = 1;
            componentsHolder.add(Box.createVerticalGlue(), c.clone());
//        c.fill = GridBagConstraints.BOTH;
//        c.weightx = 2;
//        c.weighty = 2;
//        c.gridheight = 5;
//        c.gridy = allComponents.size();
//        JComponent p = (JComponent)Box.createVerticalGlue();
//        p.setBorder(BorderFactory.createLineBorder(Color.gray, 3));
//        componentsHolder.add(p, c.clone());
        } else {
            for (int i = 0; i < allComponents.size(); i++) {
                componentsHolder.add(allComponents.get(i));
            }
            componentsHolder.add(Box.createVerticalGlue());
        }
        invalidate();
        revalidate();
    }

    public void setEditable(boolean enabled) {
        for (int i = 0; i < allComponents.size(); i++) {
            builder.setEditable(allComponents.get(i), enabled, i, allComponents.size());
        }
    }

}

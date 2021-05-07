/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.button;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.thevpc.common.swing.list.ObjectListModel;
import net.thevpc.common.swing.list.ObjectListModelListener;

/**
 *
 * @author vpc
 */
public class JBreadCrumb extends JPanel {

    ObjectListModel EMPTY_MODEL = new ObjectListModel() {
        @Override
        public String getName(Object obj) {
            return "";
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Object getObjectAt(int i) {
            return null;
        }
    };

    private List<JButton> all = new ArrayList<>();
    private List<ObjectListModelListener> listeners = new ArrayList<>();
    private ObjectListModel model;
    ActionListener buttonClickedListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            Object object = b.getClientProperty("object");
            int index = (Integer) b.getClientProperty("index");
            for (ObjectListModelListener listener : listeners) {
                listener.onSelected(object, index);
            }
        }
    };

    public JBreadCrumb() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.model = EMPTY_MODEL;
        setOpaque(false);
    }

    public ObjectListModel getModel() {
        return model;
    }

    public void addListener(ObjectListModelListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public JBreadCrumb setModel(ObjectListModel model) {
        ObjectListModel m = model == null ? EMPTY_MODEL : model;
        if (m != this.model) {
            this.model = m;
            modelChanged();
        }
        return this;
    }

    private void modelChanged() {
        int size = model.size();
        while (all.size() > size) {
            JButton b = all.remove(all.size() - 1);
            b.removeActionListener(buttonClickedListener);
            b.putClientProperty("object", null);
            b.putClientProperty("name", null);
            b.putClientProperty("index", null);
            this.remove(b);
        }
        for (int i = 0; i < all.size(); i++) {
            Object o = model.getObjectAt(i);
            JButton b = all.get(i);
            updateButton(b, i, size);
            String name = model.getName(o);
            b.setName(name);
            b.setText(name);
            b.putClientProperty("object", o);
            b.putClientProperty("name", name);
            b.putClientProperty("index", i);
        }
        for (int i = all.size(); i < size; i++) {
            addItem(model.getObjectAt(i));
        }
        repaint();
    }

    private void addItem(Object o) {
        String name = model.getName(o);
        int pos = model.size();
        JButton b = createButton(pos, pos + 1);
        updateButton(b, pos, pos + 1);
        b.setFont(b.getFont().deriveFont(b.getFont().getSize() * 0.8f));
        b.setName(name);
        b.setText(name);
        b.putClientProperty("object", o);
        b.putClientProperty("name", name);
        b.putClientProperty("index", all.size());
        b.addActionListener(buttonClickedListener);
        all.add(b);
        this.add(b);
    }

    protected JButton createButton(int pos, int size) {
        JButton b = new JButton();
        b.setOpaque(false);
        updateButton(b, pos, size);
        return b;
    }

    protected void updateButton(JButton b, int pos, int size) {
        b.setOpaque(false);
        b.setBackground(null);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setBorder(new JBreadCrumbBorder(5, pos == 0, pos == size - 1));
    }

}

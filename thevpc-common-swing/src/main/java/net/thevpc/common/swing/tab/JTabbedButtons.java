/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.tab;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.thevpc.common.swing.list.ObjectListModel;
import net.thevpc.common.swing.list.ObjectListModelListener;

/**
 *
 * @author vpc
 */
public class JTabbedButtons extends JPanel {

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

    private JTabbedPane pane;
    private List<ObjectListModelListener> listeners = new ArrayList<>();
    private ObjectListModel model;
    private boolean requirePostponeEvent;

    public JTabbedButtons() {
        super();
        setLayout(new BorderLayout());
        pane = new JTabbedPane() {
            public void setSize(Dimension d) {
                super.setSize(d);
            }

            public void setSize(int w, int h) {
                super.setSize(w, h);
            }

            public Dimension getSize(Dimension d) {
                return super.getSize(d);
            }

            @Override
            public int getWidth() {
                return super.getWidth(); //To change body of generated methods, choose Tools | Templates.
            }
            
            @Override
            public int getHeight() {
                return super.getHeight(); //To change body of generated methods, choose Tools | Templates.
            }
            

        };
        pane.setMaximumSize(new Dimension(0,0));
        pane.setPreferredSize(new Dimension(0,0));
        pane.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI() {
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                super.paintContentBorder(g, tabPlacement, selectedIndex);
            }
        });
        pane.setMinimumSize(new Dimension(1, 1));
        add(pane);
        this.model = EMPTY_MODEL;
        pane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!requirePostponeEvent) {
                    int index = pane.getSelectedIndex();
                    Object object = model.getObjectAt(index);
                    for (ObjectListModelListener listener : listeners) {
                        listener.onSelected(object, index);
                    }
                }
            }
        });
    }

    public ObjectListModel getModel() {
        return model;
    }

    public void addListener(ObjectListModelListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void setModel(ObjectListModel model) {
        ObjectListModel m = model == null ? EMPTY_MODEL : model;
        if (m != this.model) {
            this.model = m;
            modelChanged();
        }
    }

    private void modelChanged() {
        int oldSelectedIndex = pane.getSelectedIndex();
        Object oldSelectedObject = oldSelectedIndex >= 0 ? model.getObjectAt(oldSelectedIndex) : null;
        try {
            requirePostponeEvent = true;
            int size = model.size();
            while (pane.getTabCount() > size) {
                pane.removeTabAt(pane.getTabCount() - 1);
                System.out.println("remove:pane.getTabCount()=" + pane.getTabCount());
            }
            for (int i = 0; i < pane.getTabCount(); i++) {
                if (i < model.size()) {
                    Object o = model.getObjectAt(i);
                    EmptyComponent b = (EmptyComponent) pane.getTabComponentAt(i);
                    String name = model.getName(o);
                    pane.setTitleAt(i, name);
                    if (b == null) {
                        b = new EmptyComponent();
                        //pane.setComponentAt(i, b);
                    } else {
                        b.setName(name);
                        b.putClientProperty("object", o);
                        b.putClientProperty("name", name);
                        b.putClientProperty("index", i);
                    }
                }
            }
            for (int i = pane.getTabCount(); i < size; i++) {
                addItem(model.getObjectAt(i));
            }
        } finally {
            requirePostponeEvent = false;
            int newSelectedIndex = pane.getSelectedIndex();
            Object newSelectedObject = oldSelectedIndex >= 0 ? model.getObjectAt(oldSelectedIndex) : null;
            if (newSelectedIndex != oldSelectedIndex || !Objects.equals(newSelectedObject, oldSelectedObject)) {
                for (ObjectListModelListener listener : listeners) {
                    listener.onSelected(newSelectedObject, newSelectedIndex);
                }
            }
        }
        repaint();
    }

    private void addItem(Object o) {
        String name = model.getName(o);
        EmptyComponent b = new EmptyComponent();
        b.setName(name);
        b.putClientProperty("object", o);
        b.putClientProperty("name", name);
        b.putClientProperty("index", pane.getTabCount() - 1);
        pane.addTab(name, b);
//        pane.setForegroundAt(0, Color.BLUE);

        System.out.println("add:pane.getTabCount()=" + pane.getTabCount());
        SwingUtilities.invokeLater(() -> JTabbedButtons.this.repaint());
    }

    public static class EmptyComponent extends JPanel {

        public EmptyComponent() {
            setSize(new Dimension(0, 0));
            setMaximumSize(new Dimension(0, 0));
//            setBorder(BorderFactory.createLineBorder(Color.yellow));
            setVisible(false);
//            setBackground(Color.RED);
        }

        @Override
        public int getWidth() {
            return 0;
        }

        @Override
        public int getHeight() {
            return 0;
        }

    }

}

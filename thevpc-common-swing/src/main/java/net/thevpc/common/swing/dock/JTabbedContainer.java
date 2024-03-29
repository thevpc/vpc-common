/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.dock;

import net.thevpc.common.swing.util.collections.IndexedMap;

import javax.swing.*;
import java.awt.*;
import java.util.NoSuchElementException;

/**
 * @author thevpc
 */
public class JTabbedContainer extends JPanel {
    private IndexedMap<String, TabInfo> components = new IndexedMap<>();
    private boolean _closing;

    public JTabbedContainer() {
        super(new BorderLayout());
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }
//    private TabRemoveHandler tabRemoveHandler = new TabRemoveHandler();

    public boolean containsId(String id) {
        return components.containsKey(id);
    }

    public String getSelectedTabId() {
        if (isEmpty()) {
            return null;
        }
        JComponent c = getCenter();
        if (c instanceof JTabbedPane) {
            int i = ((JTabbedPane) c).getSelectedIndex();
            return components.getKeyAt(i);
        } else {
            return components.getKeyAt(0);
        }
    }

    public void remove(String id) {
        if (!_closing) {
            _closing = true;
            try {
                remove0(id, true);
            } finally {
                _closing = false;
            }
        } else {
            remove0(id, true);
        }
    }

    public void remove0(String id, boolean removeTab) {
        if (components.containsKey(id)) {
            if (components.size() == 1) {
                components.clear();
                setCenter(null);
            } else {
                JTabbedPane tabs = (JTabbedPane) getCenter();
                int index = components.indexOfKey(id);
                if (removeTab) {
                    tabs.removeTabAt(index);
                }
                components.remove(id);
                if (components.size() == 1) {
                    setCenter(components.getValueAt(0).node);
                } else {
                    for (int i = 0; i < components.size(); i++) {
                        prepareTab(tabs, id, index);
                    }
                }
            }
        }
    }

    private JComponent getCenter() {
        return isEmpty() ? null : (JComponent) getComponent(0);
    }

    private void setCenter(JComponent c) {
        removeAll();
        if (c != null) {
//            System.out.println("set-center "+c.getName());
            add(c, BorderLayout.CENTER);
        }
    }

    public void setTabTitle(String id, String title) {
        TabInfo ti = components.get(id);
        if (ti == null) {
            throw new NoSuchElementException();
        }
        ti.title = title;
        rebuildTab(id);
    }

    public void setTabIcon(String id, Icon icon) {
        TabInfo ti = components.get(id);
        if (ti == null) {
            throw new NoSuchElementException();
        }
        ti.icon = icon;
        rebuildTab(id);
    }

    public void setTabActive(String id, boolean active) {
        TabInfo ti = components.get(id);
        if (ti == null) {
            throw new NoSuchElementException();
        }
        if (active) {
            JComponent c = getCenter();
            if (c instanceof JTabbedPane) {
                JTabbedPane jtb = (JTabbedPane) c;
                int index = components.indexOfKey(id);
                jtb.setSelectedIndex(index);
            }
        }
    }

    public void setTabClosable(String id, boolean closable) {
        TabInfo ti = components.get(id);
        if (ti == null) {
            throw new NoSuchElementException();
        }
        ti.closable = closable;
        rebuildTab(id);
    }

    protected void rebuildTab(String id) {
        if (components.size() > 1) {
            int index = components.indexOfKey(id);
            if (index > 0) {
                prepareTab((JTabbedPane) getCenter(), id, index);
            }
        }
    }

    public void prepareTab(JTabbedPane tab, String id, int index) {
        TabInfo ti = components.getValueAt(index);
        tab.setTitleAt(index, ti.title);
        tab.setIconAt(index, ti.icon);
//        tab.getProperties().put("tabIndex", index);
    }

    public void add(String id, JComponent n, String title, Icon icon, boolean closable) {
        if (components.containsKey(id)) {
            TabInfo ti = components.get(id);
            ti.id = id;
            ti.title = title;
            ti.icon = icon;
            ti.closable = closable;
            ti.node = n;
            if (components.size() == 1) {
                components.put(id, ti);
                setCenter(n);
            } else {
                JTabbedPane p = (JTabbedPane) getCenter();
                int index = components.indexOfKey(id);
                p.setTabComponentAt(index, n);
                prepareTab(p, id, index);
            }
        } else {
            TabInfo ti = new TabInfo();
            ti.id = id;
            ti.title = title;
            ti.icon = icon;
            ti.closable = closable;
            ti.node = n;
            if (components.isEmpty()) {
                components.put(id, ti);
                setCenter(n);
            } else {
                JTabbedPane tabs = null;
                if (components.size() == 1) {
                    tabs = new JTabbedPane();
                    String k0 = components.getKeyAt(0);
                    TabInfo ti0 = components.getValueAt(0);
                    tabs.addTab(ti0.title, ti0.node);
                    prepareTab(tabs, k0, 0);
                    setCenter(tabs);
                } else {
                    tabs = (JTabbedPane) getCenter();
                }
                //Tab tab = new Tab();
                //tab.setOnClosed(tabRemoveHandler);
                //tabs.getTabs().add(tab);
                tabs.addTab(ti.title, ti.node);
                components.put(id, ti);
                prepareTab(tabs, id, components.size() - 1);
            }
        }
    }


    //    private class TabRemoveHandler implements EventHandler<Event> {
//
//        public TabRemoveHandler() {
//        }
//
//        @Override
//        public void handle(Event t) {
//            Tab tab = (Tab) t.getSource();
//            if (!_closing) {
//                int i = (int) tab.getProperties().get("tabIndex");
//                String theId = components.getKeyAt(i);
//                remove0(theId, false);
//            }
//        }
//    }
    public interface TitleResolver {

        String getTitle(int index, String id, JComponent n);

    }

    private class TabInfo {

        String id;
        String title;
        JComponent node;
        Icon icon;
        boolean closable;
    }
}

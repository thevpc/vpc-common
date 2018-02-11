/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.swings;

import net.vpc.common.swings.util.StringShellFilter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 19 oct. 2006 00:16:49
 */
public class JListCardPanel extends JCardPanel {
    private JPanel main = new JPanel(new CardLayout());
    private JTextField filterText = new JTextField();
    private JList list = new JList(new FiltredListModel());
    private JSplitPane splitPane;

//    public static void main(String[] args) {
//        JFrame f = new JFrame();
//        JListCardPanel p = new JListCardPanel();
//        int[] v = {SwingConstants.BOTTOM, SwingConstants.TOP, SwingConstants.CENTER};
//        String[] sv = {"BOTTOM", "TOP", "CENTER"};
//        int[] h = {SwingConstants.LEFT, SwingConstants.RIGHT, SwingConstants.CENTER};
//        String[] sh = {"LEFT", "RIGHT", "CENTER"};
//        for (int vi1 = 0; vi1 < v.length; vi1++) {
//            for (int hi1 = 0; hi1 < h.length; hi1++) {
//                for (int vi2 = 0; vi2 < v.length; vi2++) {
//                    for (int hi2 = 0; hi2 < h.length; hi2++) {
//                        String id = sv[vi1] + "/" + sv[vi2] + ";" + sh[hi1] + "/" + sh[hi2];
//                        JLabel jLabel = new JLabel("Hello " + id);
//                        jLabel.setVerticalTextPosition(v[vi1]);
//                        jLabel.setVerticalAlignment(v[vi2]);
//                        jLabel.setHorizontalAlignment(h[hi1]);
//                        jLabel.setHorizontalTextPosition(h[hi2]);
//                        ImageIcon icon = new ImageIcon("/home/vpc/xprojects/apps/dbclient/dbclient-plugins/dbclient-system/src/net/vpc/app/dbclient/plugin/system/viewmanager/iconset/dbclient-iconset-default-images/CheckVersion.gif");
//                        jLabel.setIcon(icon);
//                        p.addPage(id, "Hello "+id, icon, jLabel);
//                    }
//                }
//            }
//        }
//        f.add(p);
//        f.pack();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setVisible(true);
//    }

    public JListCardPanel() {
        super(new BorderLayout());
        JScrollPane spane = new JScrollPane(list);
        JPanel p = new JPanel(new BorderLayout());
        p.add(filterText, BorderLayout.PAGE_START);
        filterText.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                anyUpdate(e);
            }

            public void removeUpdate(DocumentEvent e) {
                anyUpdate(e);
            }

            public void changedUpdate(DocumentEvent e) {
                anyUpdate(e);
            }

            public void anyUpdate(DocumentEvent e) {
                ((FiltredListModel) (list.getModel())).setFilter(new TitleFiltredListModelFilter(filterText.getText()));
            }
        });
        p.add(spane, BorderLayout.CENTER);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p, main);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.3);
        add(splitPane, BorderLayout.CENTER);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                PanelPage selectedValue = (PanelPage) list.getSelectedValue();
                if (selectedValue != null) {
                    ((CardLayout) main.getLayout()).show(main, selectedValue.id);
                }
            }
        });
        list.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                PanelPage i = (PanelPage) value;
                super.getListCellRendererComponent(list, i.getTitle(), index, isSelected, cellHasFocus);
                setIcon(i.getIcon());
                return this;
            }
        });
    }

    public void addPage(String id, String title, Icon icon, JComponent c) {
        ((FiltredListModel) list.getModel()).addElement(new PanelPage(c, id, title, icon));
        main.add(c, id);
        if (list.getModel().getSize() == 1) {
            list.setSelectedIndex(list.getModel().getSize() - 1);
        }
    }

    public JComponent[] getPageComponents() {
        FiltredListModel listModel = ((FiltredListModel) list.getModel());
        PanelPage[] pages = listModel.toArray();
        JComponent[] all = new JComponent[pages.length];
        for (int i = 0; i < pages.length; i++) {
            all[i] = pages[i].component;
        }
        return all;
    }

    public JComponent getPageComponent(String id) {
        PanelPage panelPage = getPage(id);
        return panelPage == null ? null : panelPage.component;
    }

    private PanelPage getPage(String id) {
        FiltredListModel listModel = ((FiltredListModel) list.getModel());
        for (Object o : listModel.toArray()) {
            PanelPage p = (PanelPage) o;
            if (p.id.equals(id)) {
                return p;
            }
        }
        return null;
    }

    public JComponent getPageComponentAt(int i) {
        PanelPage o = ((FiltredListModel) list.getModel()).getElementAt(i);
        return o.component;
    }

    public JList getList() {
        return list;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public static class PanelPage implements Serializable {
        private String id;
        private String title;
        private Icon icon;
        private JComponent component;

        public PanelPage(JComponent component, String id, String title, Icon icon) {
            this.id = id;
            this.title = title;
            this.component = component;
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Icon getIcon() {
            return icon;
        }

        public JComponent getComponent() {
            return component;
        }
    }

    private static class NoFiltredListModelFilter implements FiltredListModelFilter {

        public boolean accept(PanelPage o) {
            return true;
        }
    }

    private static class TitleFiltredListModelFilter implements FiltredListModelFilter {
        private String pattern;

        public TitleFiltredListModelFilter(String pattern) {
            pattern = pattern.toLowerCase();
            if (!pattern.startsWith("*")) {
                pattern = "*" + pattern;
            }
            if (!pattern.endsWith("*")) {
                pattern = pattern + "*";
            }
            this.pattern = StringShellFilter.shellToRegexpPattern(pattern);
        }

        public boolean accept(PanelPage o) {
            return o.title.toLowerCase().matches(pattern);
        }
    }

    private static interface FiltredListModelFilter {
        public boolean accept(PanelPage o);
    }

    private static class FiltredListModel extends AbstractListModel {
        private FiltredListModelFilter filter = new NoFiltredListModelFilter();
        private Vector<PanelPage> delegate = new Vector<PanelPage>();
        private Vector<PanelPage> realDelegate = new Vector<PanelPage>();


        public PanelPage getElementAt(int index) {
            return delegate.get(index);
        }

        public int getSize() {
            return delegate.size();
        }

        public FiltredListModelFilter getFilter() {
            return filter;
        }

        public void setFilter(FiltredListModelFilter filter) {
            if (filter == null) {
                filter = new NoFiltredListModelFilter();
            }
            this.filter = filter;
            rebuild();
        }

        private void rebuild() {
            if (filter != null) {
                int index = delegate.size();
                if (index > 0) {
                    delegate.clear();
                    fireIntervalRemoved(this, 0, index - 1);
                }
                for (PanelPage o : realDelegate) {
                    if (filter.accept(o)) {
                        delegate.add(o);
                    }
                }
                if (delegate.size() > 0) {
                    fireIntervalAdded(this, 0, delegate.size() - 1);
                }
            }
        }

        private void addElement(PanelPage object) {
            for (PanelPage panelPage : delegate) {
                if (panelPage.id.equals(object.id)) {
                    object.id = object.id + "'";
                    object.title = object.title + "'";
//                    throw new IllegalArgumentException(object.id+" Already exists");
                }
            }
            realDelegate.add(object);
            if (filter.accept(object)) {
                int index = delegate.size();
                delegate.add(object);
                fireIntervalAdded(this, index, index);
            }
        }

        public PanelPage[] toArray() {
            return realDelegate.toArray(new PanelPage[realDelegate.size()]);
        }

        public FiltredListModel() {
        }
    }

}

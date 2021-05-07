/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.swing.panel;

import net.thevpc.common.swing.tree.JTreeUtil;
import net.thevpc.common.swing.tree.TreeCellTextRenderer;
import net.thevpc.common.swing.tree.TreeFilterDecorator;
import net.thevpc.common.swing.util.SwingsStringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 19 oct. 2006 00:16:49
 */
public class JTreeCardPanel extends JCardPanel {

    private JPanel main = new JPanel(new CardLayout());
//    private JTextField filterText = new JTextField();
    private JTree tree = new JTree();
    private JSplitPane splitPane;
    private DefaultMutableTreeNode treeRoot;
    private LinkedList<DefaultMutableTreeNode> nodesList = new LinkedList<DefaultMutableTreeNode>();

//    public static void main(String[] args) {
//        JFrame f = new JFrame();
//        JTreeCardPanel p = new JTreeCardPanel();
//        p.addPage("/d/e/f", "Hello d", null, new JLabel("d"));
//        p.addPage("/a/b/c", "Hello c", null, new JLabel("c"));
//        f.add(p);
//        f.pack();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setVisible(true);
//    }
    public JTreeCardPanel() {
        super(new BorderLayout());
        treeRoot = createNode(null, "/", "/", "/", null);
        main.add(createDefaultNodeComponent("", "", "", null), "");
//        JScrollPane spane = new JScrollPane(list);
//        JPanel p = new JPanel(new BorderLayout());
//        p.add(filterText, BorderLayout.PAGE_START);
//        filterText.getDocument().addDocumentListener(new DocumentListener() {
//            public void insertUpdate(DocumentEvent e) {
//                anyUpdate(e);
//            }
//
//            public void removeUpdate(DocumentEvent e) {
//                anyUpdate(e);
//            }
//
//            public void changedUpdate(DocumentEvent e) {
//                anyUpdate(e);
//            }
//
//            public void anyUpdate(DocumentEvent e) {
//                ((FiltredListModel) (list.getModel())).setFilter(new TitleFiltredListModelFilter(filterText.getText()));
//            }
//        });
//        p.add(spane, BorderLayout.CENTER);
//        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p, main);
//        splitPane.setOneTouchExpandable(true);
//        splitPane.setDividerLocation(0.3);
//        add(splitPane, BorderLayout.CENTER);
//        list.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                PanelPage selectedValue = (PanelPage) list.getSelectedValue();
//                if (selectedValue != null) {
//                    ((CardLayout) main.getLayout()).show(main, selectedValue.id);
//                }
//            }
//        });
//        list.setCellRenderer(new DefaultListCellRenderer() {
//            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                PanelPage i = (PanelPage) value;
//                super.getListCellRendererComponent(list, i.getTitle(), index, isSelected, cellHasFocus);
//                setIcon(i.getIcon());
//                return this;
//            }
//        });
//        
        tree.setModel(new DefaultTreeModel(treeRoot));
        TreeFilterDecorator filterDecorator = TreeFilterDecorator.decorator(tree).setTextRenderer(new TreeCellTextRenderer() {
            @Override
            public String renderText(Object value) {
                if (value instanceof DefaultMutableTreeNode) {
                    value = ((DefaultMutableTreeNode) value).getUserObject();
                }
                if (value instanceof PanelPage) {
                    value = ((PanelPage) value).getTitle();
                }
                return value == null ? "" : String.valueOf(value);
            }
        }).decorate();
        tree.setLargeModel(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath p = e.getNewLeadSelectionPath();
                if (p != null) {
                    Object tn = p.getLastPathComponent();
                    if (tn != null && tn instanceof DefaultMutableTreeNode) {
                        tn = ((DefaultMutableTreeNode) tn).getUserObject();
                    }
                    if (tn instanceof PanelPage) {
                        PanelPage selectedValue = (PanelPage) tn;
                        ((CardLayout) main.getLayout()).show(main, selectedValue.id);
                        return;
                    }
                }
                ((CardLayout) main.getLayout()).show(main, "");
            }
        });
        JTreeUtil.setTreeExpandedState(tree, true);
        JPanel pp = new JPanel(new BorderLayout());
        pp.add(filterDecorator.getFilterField(), BorderLayout.NORTH);
        pp.add(new JScrollPane(tree), BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pp, new JScrollPane(main));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.3);
        this.add(splitPane, BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode getOrNull(String path) {
        return get(path, false, null);
    }

    private DefaultMutableTreeNode get(String path, boolean create, boolean[] refCreated) {
        return get(splitPath("/", path), create, refCreated);
    }

    private DefaultMutableTreeNode get(String[] path, boolean create, boolean[] refCreated) {
        if (path.length == 0) {
            return treeRoot;
        }
        DefaultMutableTreeNode pp = treeRoot;
        for (int i = 0; i < path.length; i++) {
            String curr = path[i];
            PanelPage puo = (PanelPage) pp.getUserObject();
            int count = pp.getChildCount();
            DefaultMutableTreeNode found = null;
            for (int j = 0; j < count; j++) {
                DefaultMutableTreeNode n = (DefaultMutableTreeNode) pp.getChildAt(j);
                PanelPage uo = (PanelPage) n.getUserObject();
                if (uo.name.equals(curr)) {
                    found = n;
                    break;
                }
            }
            if (found != null) {
                pp = found;
            } else {
                if (create) {
                    String currPath = join(Arrays.asList(Arrays.copyOfRange(path, 0, i + 1)));
                    DefaultMutableTreeNode v = createNode(null, currPath, curr, curr, null);
                    int c = pp.getChildCount();
                    pp.add(v);
                    ((DefaultTreeModel) tree.getModel()).nodesWereInserted(pp, new int[]{c});
//                    System.out.println("[EFFECTIVE] Created  " + currPath);
                    pp = v;
                    if (refCreated != null) {
                        refCreated[0] = true;
                    }
                } else {
                    return null;
                }
            }
        }
        return pp;
    }

    protected JComponent createDefaultNodeComponent(String id, String name, String title, Icon icon) {
        return new JLabel("");
//        return new JLabel("<DEFAULT> " + id);
    }

    protected DefaultMutableTreeNode createNode(JComponent component, String id, String name, String title, Icon icon) {
        if (component == null) {
            component = createDefaultNodeComponent(id, name, title, icon);
        }
        PanelPage uo = new PanelPage(component, id, name, title, icon);
        DefaultMutableTreeNode v = new DefaultMutableTreeNode(uo, true);
        nodesList.add(v);
        main.add(component, id);
        return v;
    }

//    private int indexOfId(String id) {
//        int size = list.getModel().getSize();
//        for (int i = 0; i < size; i++) {
//            PanelPage elementAt = (PanelPage) list.getModel().getElementAt(i);
//            if (id.equals(elementAt.id)) {
//                return i;
//            }
//        }
//        return -1;
//    }
    private String validateId(String id) {
        if (SwingsStringUtils.isEmpty(id)) {
            id = "#";
        }
        int i = 0;
        while (true) {
            String n = i == 0 ? id : id + "(" + (i + 2) + ")";
            if (getOrNull(n) == null) {
                return n;
            }
            i++;
        }
    }

    @Override
    public String addPage(String id, String title, Icon icon, JComponent c) {
//        System.out.println("addPage " + id);
        String newId = validateId(id);
        if (newId == null) {
            return null;
        }
        boolean[] refCreated = new boolean[1];
        DefaultMutableTreeNode t = get(id, true, refCreated);
        PanelPage pp = (PanelPage) t.getUserObject();
        if (refCreated[0]) {
//            System.out.println("NOW CREATED " + id + " as " + title);
            pp.title = title;
            pp.icon = icon;
            pp.setComponent(c);
        }else{
//            System.out.println("NOW UPDATING " + id + " as " +pp.title +"->"+ title);
            pp.title = title;
            pp.icon = icon;
            pp.setComponent(c);
        }
        main.add(c, pp.id);
        tree.setSelectionPath(new TreePath(t.getPath()));
//        if (list.getModel().getSize() == 1) {
//            list.setSelectedIndex(list.getModel().getSize() - 1);
//        }
        return newId;
    }

    public Component getPageComponent(int index) {
        DefaultMutableTreeNode x = nodesList.get(index);
        return ((PanelPage) x.getUserObject()).getComponent();
    }

    public Component[] getPageComponents() {
        java.util.List<Component> all = new ArrayList<Component>();
        for (DefaultMutableTreeNode x : nodesList) {
            all.add(((PanelPage) x.getUserObject()).getComponent());
        }
        return all.toArray(new Component[0]);
    }

    public int getPageComponentsCount() {
        return nodesList.size();
    }

    public void removePageAt(String id) {
        String[] p = id.split("/");
        String[] p0 = Arrays.copyOfRange(p, 0, p.length - 1);
        DefaultMutableTreeNode pn = get(p0, false, null);
        DefaultMutableTreeNode cn = get(p, false, null);
        if (pn != null && cn != null) {
//            int indexRemoved = -1;
            for (int i = 0; i < pn.getChildCount(); i++) {
                DefaultMutableTreeNode object = (DefaultMutableTreeNode) pn.getChildAt(i);
                if (object == cn) {
                    pn.remove(i);
                    ((DefaultTreeModel) tree.getModel()).nodesWereRemoved(pn, new int[]{i}, new Object[]{cn});
                    break;
                }
            }
            nodesList.remove(cn);
            main.remove(((PanelPage) cn.getUserObject()).getComponent());
        }
    }

//    public void removePageAt(int index) {
//        FiltredListModel model = getFilteredListModel();
//        model.removeBaseAt(index);
//        main.remove(index);
//    }
//
//    private FiltredListModel getFilteredListModel() {
//        return (FiltredListModel) list.getModel();
//    }
//    public void setPageAt(int index, String id, String title, Icon icon, JComponent c) {
//        FiltredListModel model = getFilteredListModel();
//        if (index <= -1 || index >= model.getBaseSize()) {
//            addPage(id, title, icon, c);
//            return;
//        }
//        int oldIndex = indexOfId(id);
//        if (oldIndex != index) {
//            id = validateId(id);
//        }
//        model.setElementAt(index, new PanelPage(c, id, title, icon));
//        main.add(c, id);
//        if (list.getModel().getSize() == 1) {
//            list.setSelectedIndex(list.getModel().getSize() - 1);
//        }
//    }
//    public JComponent getPageComponent(int index) {
//        return getPageComponents()[index];
//    }
//    public int getPageComponentsCount() {
//        return getPageComponents().length;
//    }
//    public JComponent[] getPageComponents() {
//        FiltredListModel listModel = ((FiltredListModel) list.getModel());
//        PanelPage[] pages = listModel.toArray();
//        JComponent[] all = new JComponent[pages.length];
//        for (int i = 0; i < pages.length; i++) {
//            all[i] = pages[i].component;
//        }
//        return all;
//    }
    public JComponent getPageComponent(String id) {
        PanelPage panelPage = getPage(id);
        return panelPage == null ? null : panelPage.getComponent();
    }

    private PanelPage getPage(String id) {
        if (id == null) {
            id = "";
        }
        DefaultMutableTreeNode t = getOrNull(id);
        if (t != null) {
            return (PanelPage) t.getUserObject();
        }
        return null;
    }

//    public JComponent getPageComponentAt(int i) {
//        PanelPage o = ((FiltredListModel) list.getModel()).getElementAt(i);
//        return o.component;
//    }
//
//    public JList getList() {
//        return list;
//    }
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public static class PanelPage implements Serializable {

        private String id;
        private String name;
        private String title;
        private Icon icon;
        private JComponent component;

        public PanelPage(JComponent component, String id, String name, String title, Icon icon) {
            this.id = id;
            this.name = name;
            this.title = title;
            this.icon = icon;
            setComponent(component);
        }

        public String getName() {
            return name;
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

        public void setComponent(JComponent component) {
            if (this.component != null) {
                this.component.putClientProperty("$PanelPage", null);
            }
            this.component = component;
            if (this.component != null) {
                this.component.putClientProperty("$PanelPage", this);
            }
        }

    }

    private static String[] splitPath(String sep, String str) {
        java.util.List<String> p = new ArrayList<String>();
        if (str != null) {
            StringTokenizer st = new StringTokenizer(str, sep);
            while (st.hasMoreTokens()) {
                p.add(st.nextToken());
            }
        }
        return p.toArray(new String[0]);
    }

    private static String join(Collection<String> items) {
        if (items.isEmpty()) {
            return "/";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> i = items.iterator();
        while (i.hasNext()) {
            sb.append("/");
            sb.append(i.next());
        }
        return sb.toString();
    }

}

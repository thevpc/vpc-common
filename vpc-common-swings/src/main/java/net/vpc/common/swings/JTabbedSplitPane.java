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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 12 aout 2005
 */
public class JTabbedSplitPane extends JPanel {
//    public static void main(String[] args) {
//        final JTabbedSplitPane tabbedSplitPane = new JTabbedSplitPane(JSplitPane.VERTICAL_SPLIT);
//        tabbedSplitPane.setBorder(null);
//        tabbedSplitPane.setTopComponent(new JTextArea());
//        tabbedSplitPane.setSelectorVisible(false);
//        JComponent p1 = new JLabel("comp1");
//        tabbedSplitPane.addBottomPage("SQLCommandPane.MessagesPanel", p1);
//        JComponent p2 = new JLabel("comp2");
//        tabbedSplitPane.addBottomPage("SQLCommandPane.GridsPanel", p2);
//        tabbedSplitPane.setPreferredSize(new Dimension(500, 500));
//        JToolBar b = new JToolBar();
//        b.add(tabbedSplitPane.getButton("SQLCommandPane.MessagesPanel"));
//        b.add(tabbedSplitPane.getButton("SQLCommandPane.GridsPanel"));
//
//        JButton bt1 = new JButton("1");
//        bt1.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                tabbedSplitPane.setSelectedPage("SQLCommandPane.MessagesPanel");
//            }
//        });
//        b.add(bt1);
//        JButton bt2 = new JButton("2");
//        bt2.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                tabbedSplitPane.setSelectedPage("SQLCommandPane.GridsPanel");
//            }
//        });
//        b.add(bt2);
//        JButton bt3 = new JButton("null");
//        bt3.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                tabbedSplitPane.setSelectedPage((String) null);
//            }
//        });
//        b.add(bt3);
//        JPanel pp = new JPanel(new BorderLayout());
//        pp.add(tabbedSplitPane, BorderLayout.CENTER);
//        pp.add(b, BorderLayout.NORTH);
//        JOptionPane.showConfirmDialog(null, pp);
//
//    }

    private boolean valueAdjusting;
    private String selectedPageName;
    private JSplitPane split;

    private JToolBar bottomBar = new JToolBar();

    private Hashtable<String, TabberPage> map = new Hashtable<String, TabberPage>();

    private PropertyChangeListener pLDispatcher = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    };

    public JToggleButton getButton(String name) {
        return map.get(name).button;
    }

    public void setSelectedPage(String name) {
        setSelectedPage(name == null ? null : map.get(name));
    }

    public void setSelectedPage(TabberPage tabberPage) {
        String oldVal = selectedPageName;
        if (tabberPage == null) {
            if (selectedPageName != null) {
                split.setDividerLocation(1.0);
                selectedPageName = null;
            }
        } else {
            CardLayout manager = (CardLayout) tabberPage.getTabber().getLayout();
            selectedPageName = tabberPage.getName();
            if (!selectedPageName.equals(oldVal)) {
                manager.show(tabberPage.getTabber(), tabberPage.getName());
                double currentLocation = split.getDividerLocation() / (split.getHeight() - split.getDividerSize());
                if (currentLocation >= 1) {
                    split.setDividerLocation(split.getLastDividerLocation());
                }
            }
        }
        valueAdjusting = true;
        try {
            firePropertyChange("selectedPageName", oldVal, selectedPageName);
        } finally {
            valueAdjusting = false;
        }
    }

    public JTabbedSplitPane() {
        this(JSplitPane.HORIZONTAL_SPLIT);
    }

    public void setDividerLocation(double value) {
        split.setDividerLocation(value);
    }

    public void setResizeWeight(double value) {
        split.setResizeWeight(value);
    }

    public void setDividerLocation(int value) {
        split.setDividerLocation(value);
    }

    public JTabbedSplitPane(int newOrientation) {
        super(new BorderLayout());
        split = new JSplitPane(newOrientation);
        split.setBorder(null);
        split.setOneTouchExpandable(false);
        bottomBar.setFloatable(false);
        add(split, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.PAGE_END);
        split.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, pLDispatcher);
    }

    public JSplitPane getSplit() {
        return split;
    }

    public void setBottomComponent(Component comp) {
        split.setBottomComponent(comp);
    }

    public void setTopComponent(Component comp) {
        split.setTopComponent(comp);
    }

    public void addBottomPage(final String name, String title, String buttonText, Icon buttonIcon, Component comp) {
        Component c = split.getBottomComponent();
        if (!(c instanceof Tabber)) {
            c = new Tabber();
            split.setBottomComponent(c);
        }
        final JToggleButton b = new JToggleButton(buttonText, buttonIcon);
        TabberPage page = new TabberPage(name, title, b, comp);
        ((Tabber) c).add(page, page.getName());
        b.putClientProperty("TabberPage", page);
        b.putClientProperty("Tabber", c);
        b.setName(page.getName());
        bottomBar.add(b);
        b.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (!valueAdjusting) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        setSelectedPage(name);
                    } else {
                        String n = null;
                        setSelectedPage(n);
                    }
                }
            }
        });
        JTabbedSplitPane.this.addPropertyChangeListener("selectedPageName", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                b.setSelected(name.equals(selectedPageName));
            }
        });
        map.put(name, page);
        showBottomPage(name);
        setSelectedPage(name);
    }

    public void addBottomPage(String name, Component comp) {
        addBottomPage(name, name, name, null, comp);
    }

    public void showBottomPage(String page) {
        Tabber tabber = (Tabber) split.getBottomComponent();
        CardLayout manager = (CardLayout) tabber.getLayout();
        manager.show(tabber, page);
    }

    public void showTopPage(String page) {
        Tabber tabber = (Tabber) split.getTopComponent();
        CardLayout manager = (CardLayout) tabber.getLayout();
        manager.show(tabber, page);
    }

    public void setSelectorVisible(boolean visible) {
        bottomBar.setVisible(visible);
    }

    public boolean isSelectorVisible() {
        return bottomBar.isVisible();
    }

    private class Tabber extends JPanel {
        public Tabber() {
            setLayout(new CardLayout());
        }
    }

    public double getSplitPaneDividerLocationQuotient() {
        if (split.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
            double dividerLocation = split.getDividerLocation();
            double height = split.getHeight();
            double dividerSize = split.getDividerSize();
            return dividerLocation / (height - dividerSize);
        } else {
            double dividerLocation = split.getDividerLocation();
            double width = split.getWidth();
            double dividerSize = split.getDividerSize();
            return dividerLocation / (width - dividerSize);
        }
    }


    private class TabberPage extends JPanel {
        String id;
        Component title;
        JToggleButton button;
        Component component;

        public TabberPage(String name, String title, JToggleButton button, Component component) {
            this(name, new JLabel(title), button, component);

        }

        public TabberPage(String name, Component title, JToggleButton button, Component component) {
            super(new BorderLayout());
            this.title = title;
            this.button = button;
            this.component = component;
            setName(name);
            add(component, BorderLayout.CENTER);
        }

        public JToggleButton getButton() {
            return button;
        }

        public void setTitle(Component title) {
            this.title = title;
            add(title, BorderLayout.PAGE_START);
        }

        public void setComponent(Component component) {
            this.component = component;
            add(component, BorderLayout.CENTER);
        }

        public Tabber getTabber() {
            return (Tabber) getParent();
        }
    }

    public boolean isValueAdjusting() {
        return valueAdjusting;
    }

}

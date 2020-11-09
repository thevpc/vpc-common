/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
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
package net.thevpc.common.swings.pluginmanager;

import net.thevpc.common.prs.plugin.Plugin;
import net.thevpc.common.prs.plugin.PluginDependency;
import net.thevpc.common.prs.plugin.PluginDescriptor;
import net.thevpc.common.swings.dialog.MessageDialogType;
import net.thevpc.common.swings.iswing.IJTable;
import net.thevpc.common.swings.prs.PRSManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 21 oct. 2007 00:24:09
 */
class InstalledPluginsPanel extends JPanel {

    private IJTable pluginsTable;
    private PluginManagerEditor pluginManagerEditor;
    private PluginPropertiesPanel pluginPropertiesPanel;
    public InstalledPluginsPanel(PluginManagerEditor _pluginManagerEditor) {
        this.pluginManagerEditor = _pluginManagerEditor;
        pluginsTable = pluginManagerEditor.getFactory().newInstance(IJTable.class);
        final Plugin[] plugins = pluginManagerEditor.getPluginManager().getAllPlugins();
        pluginsTable.setModel(new PluginsModel(pluginManagerEditor, plugins, null));
        TreeSet<String> categories = new TreeSet<String>();
        for (Plugin plugin : plugins) {
            categories.add(plugin.getDescriptor().getCategory());
        }
        ArrayList<String> categories2 = new ArrayList<String>(categories);
        categories2.add(0, "*");
        final JTextField filter = new JTextField();
        filter.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                doUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                doUpdate();
            }

            public void changedUpdate(DocumentEvent e) {
                doUpdate();
            }

            private void doUpdate() {
                pluginsTable.setModel(new PluginsModel(pluginManagerEditor, plugins, filter.getText()));
                if (pluginsTable.getModel().getRowCount() > 0) {
                    pluginsTable.getSelectionModel().setSelectionInterval(0, 0);
                }
            }
        });
        JScrollPane jsp = new JScrollPane(pluginsTable.getComponent());
        jsp.setPreferredSize(new Dimension(600, 300));


        pluginPropertiesPanel = new PluginPropertiesPanel(pluginManagerEditor);
        JSplitPane jsplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                jsp,
                pluginPropertiesPanel);
        jsplit.setResizeWeight(0.4);
        jsplit.setDividerLocation(0.4);
        pluginsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        pluginsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int index = pluginsTable.convertRowIndexToModel(pluginsTable.getSelectedRow());
                if (index >= 0) {
                    PluginsModel pm = (PluginsModel) pluginsTable.getModel();
                    pluginPropertiesPanel.setPlugin(pm.plugins[index]);
                }
            }
        });
        pluginsTable.getComponent().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    int index = pluginsTable.convertRowIndexToModel(pluginsTable.getSelectedRow());
                    if (index >= 0) {
                        PluginsModel pm = (PluginsModel) pluginsTable.getModel();
                        Plugin plugin = pm.plugins[index];
                        if (!plugin.getDescriptor().isSystem() && !plugin.getDescriptor().isForUninstall()) {
                            plugin.setEnabled(!plugin.isEnabled());
                            pluginPropertiesPanel.setPlugin(pm.plugins[index]);
                            pluginsTable.getComponent().repaint();
                            pluginPropertiesPanel.repaint();
                        }
                    }
                }
            }
        });
        if(pluginsTable.getComponent() instanceof JComponent){
            ((JComponent)pluginsTable.getComponent()).setComponentPopupMenu(createComponentPopupMenu());
        }
        pluginsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

            Color initialFG = getForeground();
            Color initialBG = getBackground();

            //            Color initialBg=getBackground();
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (column == 5) {//status
                    value = (value == null || (value instanceof String))? null : pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Status." + value);
                }
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                PluginsModel pm = (PluginsModel) table.getModel();
                row = pluginsTable.convertRowIndexToModel(row);
                Plugin plugin = pm.plugins[row];
                if (plugin.getDescriptor().isSystem()) {
                    setForeground(Color.GREEN.darker());
                } else if (plugin.getDescriptor().isForUninstall()) {
                    setForeground(Color.RED);
                } else if (!plugin.getDescriptor().isValid()) {
                    setForeground(Color.RED.darker());
                } else if (!plugin.getDescriptor().isValid()) {
                    setForeground(Color.GRAY);
                } else {
                    setForeground(initialFG);
                }
                if (!isSelected || column == 5) {
                    PluginDescriptor.Status s = (PluginDescriptor.Status) pluginManagerEditor.getStatus().get(pm.plugins[row].getId());
                    if (s == null) {
                        setBackground(initialBG);
                    } else {
                        switch (s) {
                            case INSTALLED: {
                                setBackground(initialBG);
                            }
                            case UPDATABLE:
                            case FOR_INSTALL:
                            case INSTALLABLE: {
                                setBackground(Color.GREEN.brighter().brighter());
                            }
                            case INAPPROPRIATE:
                            case OBSOLETE: {
                                setBackground(Color.RED.brighter().brighter());
                            }
                        }
                    }
                }
                return this;
            }
        });
        //JToolBar b = new JToolBar();
        //b.setFloatable(false);
        Box b = Box.createHorizontalBox();

        b.add(Box.createHorizontalStrut(5));
        b.add(PRSManager.createLabel("PluginManagerEditor.Filter"));
        b.add(Box.createHorizontalStrut(5));
        b.add(filter);

        b.add(Box.createHorizontalStrut(5));
        Font font0 = new JButton().getFont();
        Font font1 = font0.deriveFont(Font.ITALIC, font0.getSize() * 0.8f);

//        final JButton check = PRSManager.createButton("PluginManagerEditor.CheckForUpdates");
//        pluginManagerEditor.getMonitor().add(check);
//        check.setFont(font1);
//        check.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                new Thread() {
//                    public void run() {
//                        int id=pluginManagerEditor.getMonitor().startIndeterminate();
//                        try {
//                            checkForUpdates();
//                        } finally {
//                            pluginManagerEditor.getMonitor().stop(id);
//                        }
//                    }
//                }.start();
//            }
//        });
//        b.add(check);

        final JButton uninstall = PRSManager.createButton("PluginManagerEditor.Uninstall");
        pluginManagerEditor.getMonitor().add(uninstall);
        uninstall.setFont(font1);
        uninstall.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new Thread() {

                    public void run() {
                        int id = pluginManagerEditor.getMonitor().startIndeterminate();
                        try {
                            doUninstall();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            pluginManagerEditor.getMonitor().stop(id);
                        }
                    }
                }.start();
            }
        });
        b.add(uninstall);

        b.add(Box.createHorizontalStrut(5));

        pluginsTable.setRowSelectionInterval(0, 0);

        this.setLayout(new BorderLayout());
        this.add(b, BorderLayout.PAGE_START);
        this.add(jsplit, BorderLayout.CENTER);
        PRSManager.updateOnFirstComponentShown(this, pluginManagerEditor.getResourceSetHolder());
    }

    public Plugin[] getSelectedPlugins() {
        int[] index = pluginsTable.getSelectedRows();
        for (int i = 0; i < index.length; i++) {
            index[i]=pluginsTable.convertRowIndexToModel(index[i]);
        }
        PluginsModel pm = (PluginsModel) pluginsTable.getModel();
        Plugin[] pp = new Plugin[index.length];
        for (int i = 0; i < pp.length; i++) {
            pp[i] = pm.plugins[index[i]];
        }
        return pp;
    }

    private void doUninstall() {
        int x = 0;
        for (Plugin plugin : getSelectedPlugins()) {
            try {
                pluginManagerEditor.getPluginManager().uninstallPlugin(plugin.getId());
                for (PluginDependency s : plugin.getDescriptor().getDependencies()) {
                    try {
                        pluginManagerEditor.getPluginManager().getPlugin(s.getId()).setEnabled(false);
                        pluginsTable.getComponent().repaint();
                        pluginManagerEditor.pluginStatusChanged();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                x++;
            } catch (Throwable e) {
                pluginManagerEditor.getMessageDialogManager().showMessage(this, null, MessageDialogType.ERROR, null, e);
            }
        }
        pluginsTable.getComponent().repaint();
        pluginManagerEditor.pluginStatusChanged();
//            availableTable.setModel(new InstallablePluginInfoModel(dbclient.getPluginManager().getInstallablePluginInfos()));
    }

    private void doEnable(boolean enable) {
        for (Plugin plugin : getSelectedPlugins()) {
            try {
                plugin.setEnabled(enable);
                pluginsTable.getComponent().repaint();
                pluginManagerEditor.pluginStatusChanged();
            } catch (Throwable e) {
                pluginManagerEditor.getMessageDialogManager().showMessage(this, null, MessageDialogType.ERROR, null, e);
            }
        }
        pluginsTable.getComponent().repaint();
        pluginManagerEditor.pluginStatusChanged();
//            availableTable.setModel(new InstallablePluginInfoModel(dbclient.getPluginManager().getInstallablePluginInfos()));
    }

    private void checkForUpdates() {
        try {
            pluginManagerEditor.getStatus().clear();
            PluginDescriptor[] pluginInfos = pluginManagerEditor.getPluginManager().getAvailablePluginDescriptors();
            for (Plugin pluginInfo : pluginManagerEditor.getPluginManager().getAllPlugins()) {
                pluginManagerEditor.getStatus().put(pluginInfo.getId(), PluginDescriptor.Status.INSTALLED);
            }
            for (PluginDescriptor pluginInfo : pluginInfos) {
                pluginManagerEditor.getStatus().put(pluginInfo.getId(), pluginInfo.getStatus());
            }
            pluginsTable.getComponent().invalidate();
            //pluginsTable.toComponent().revalidate();
            pluginsTable.getComponent().repaint();
        } catch (Throwable e) {
            pluginManagerEditor.getMessageDialogManager().showMessage(this, "Unable to check for updates : " + e.getMessage(), MessageDialogType.ERROR, null, e);
        }
    }

    private JPopupMenu createComponentPopupMenu(){
        //pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Status." + value)
        JPopupMenu p=new JPopupMenu();
        JMenuItem enableSelected=new JMenuItem("PluginManagerEditor.Action.EnableSelected");
        enableSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doEnable(true);
            }
        });
        JMenuItem disableSelected=new JMenuItem("PluginManagerEditor.Action.DisableSelected");
        disableSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doEnable(false);
            }
        });
        JMenuItem uninstallSelected=new JMenuItem("PluginManagerEditor.Action.UninstallSelected");
        uninstallSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doUninstall();
            }
        });
        p.add(enableSelected);
        p.add(disableSelected);
        p.add(uninstallSelected);
        return p;
    }
}

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
package net.thevpc.common.swings.pluginmanager;

import net.thevpc.common.prs.plugin.PluginDescriptor;
import net.thevpc.common.swings.DumbGridBagLayout;
import net.thevpc.common.swings.JURLTextField;
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
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 21 oct. 2007 00:35:45
 */
class AvailablePluginsPanel extends JPanel {

    private IJTable availableTable;
    private PluginDescriptor[] pluginDescriptors = new PluginDescriptor[0];
    private JTextField filter;
    private PluginManagerEditor pluginManagerEditor;
    private TreeSet<PluginDescriptor.Status> visibleStatus;

    public AvailablePluginsPanel(PluginManagerEditor _pluginManagerEditor, PluginDescriptor.Status... visible) {
        this.pluginManagerEditor = _pluginManagerEditor;
        visibleStatus = new TreeSet<PluginDescriptor.Status>(Arrays.asList(visible));
        availableTable = pluginManagerEditor.getFactory().newInstance(IJTable.class);
        availableTable.setModel(new PluginDescriptorModel(pluginManagerEditor, new PluginDescriptor[0], null));


        filter = new JTextField();
        //filter.setMinimumSize(new Dimension(100,8));
        //JToolBar toolBar = new JToolBar();
        Box toolBar = Box.createHorizontalBox();
        //toolBar.setFloatable(false);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(PRSManager.createLabel("PluginManagerEditor.Filter"));
        toolBar.add(Box.createHorizontalStrut(5));

        toolBar.add(filter);
        filter.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                doUpdate2();
            }

            public void removeUpdate(DocumentEvent e) {
                doUpdate2();
            }

            public void changedUpdate(DocumentEvent e) {
                doUpdate2();
            }

            private void doUpdate2() {
                pluginManagerEditor.pluginStatusChanged();
            }
        });
        JScrollPane jsp = new JScrollPane(availableTable.getComponent());
        jsp.setPreferredSize(new Dimension(600, 300));
        if (visibleStatus.contains(PluginDescriptor.Status.INSTALLABLE) || visibleStatus.contains(PluginDescriptor.Status.UPDATABLE)) {
            final JButton manualInstall = PRSManager.createButton("PluginManagerEditor.ManualInstall");
            toolBar.add(manualInstall);
            pluginManagerEditor.getMonitor().add(manualInstall);
            Font font1 = manualInstall.getFont().deriveFont(Font.ITALIC, manualInstall.getFont().getSize() * 0.8f);
            manualInstall.setFont(font1);
            final JButton install = PRSManager.createButton("PluginManagerEditor.Install");
            toolBar.add(install);
            pluginManagerEditor.getMonitor().add(install);
            install.setFont(font1);
            install.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new Thread() {

                        @Override
                        public void run() {
                            int id = pluginManagerEditor.getMonitor().startIndeterminate();
                            try {
                                doInstall();
                            } finally {
                                pluginManagerEditor.getMonitor().stop(id);
                            }
                        }
                    }.start();
                }
            });
            manualInstall.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new Thread() {

                        @Override
                        public void run() {
                            int id = pluginManagerEditor.getMonitor().startIndeterminate();
                            try {
                                doManualInstall();
                            } finally {
                                pluginManagerEditor.getMonitor().stop(id);
                            }
                        }
                    }.start();
                }
            });
        }
        if (visibleStatus.contains(PluginDescriptor.Status.FOR_INSTALL) || visibleStatus.contains(PluginDescriptor.Status.FOR_UNINSTALL)) {
            final JButton rollBackButton = PRSManager.createButton("PluginManagerEditor.RollBack");
            toolBar.add(rollBackButton);
            pluginManagerEditor.getMonitor().add(rollBackButton);
            Font font1 = rollBackButton.getFont().deriveFont(Font.ITALIC, rollBackButton.getFont().getSize() * 0.8f);
            rollBackButton.setFont(font1);
            ActionListener aa = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new Thread() {

                        @Override
                        public void run() {
                            int id = pluginManagerEditor.getMonitor().startIndeterminate();
                            try {
                                doRollBack();
                            } finally {
                                pluginManagerEditor.getMonitor().stop(id);
                            }
                        }
                    }.start();
                }
            };
            rollBackButton.addActionListener(aa);
            rollBackButton.setEnabled(false);

        }

        final PluginDescriptorPropertiesPanel pluginDescriptorPropertiesPanel = new PluginDescriptorPropertiesPanel(pluginManagerEditor);
        JSplitPane jsplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                jsp,
                pluginDescriptorPropertiesPanel);
        jsplit.setResizeWeight(0.4);
        jsplit.setDividerLocation(0.4);
        availableTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int index = availableTable.getSelectedRow();
                index = availableTable.convertRowIndexToModel(index);
                if (index >= 0) {
                    PluginDescriptorModel pm = (PluginDescriptorModel) availableTable.getModel();
                    pluginDescriptorPropertiesPanel.setPlugin(pm.descriptors[index]);
                }
            }
        });
        availableTable.getComponent().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2 && !pluginManagerEditor.getMonitor().isRunning()) {
                    int index = availableTable.getSelectedRow();
                    index = availableTable.convertRowIndexToModel(index);
                    if (index >= 0) {
                        new Thread() {

                            @Override
                            public void run() {
                                int id = pluginManagerEditor.getMonitor().startIndeterminate();
                                try {
                                    doInstall();
                                } finally {
                                    pluginManagerEditor.getMonitor().stop(id);
                                }
                            }
                        }.start();
                    }
                }
            }
        });
        availableTable.setDefaultRenderer(String.class, new AvailableTableCellRenderer());
        availableTable.setDefaultRenderer(Long.class, new AvailableTableCellRenderer());


        toolBar.add(Box.createHorizontalStrut(5));

        if (availableTable.getModel().getRowCount() > 0) {
            availableTable.setRowSelectionInterval(0, 0);
        }
        this.setLayout(new BorderLayout());
        this.add(toolBar, BorderLayout.PAGE_START);
        this.add(jsplit, BorderLayout.CENTER);

        new Thread() {

            @Override
            public void run() {
                int id = pluginManagerEditor.getMonitor().startIndeterminate();
                try {
                    pluginManagerEditor.pluginStatusChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    pluginManagerEditor.getMonitor().stop(id);
                }
            }
        }.start();
        PRSManager.updateOnFirstComponentShown(this, pluginManagerEditor.getResourceSetHolder());
    }

    public PluginDescriptor[] getSelectedInstallablePlugins() {
        int[] index = availableTable.getSelectedRows();
        for (int i = 0; i < index.length; i++) {
            index[i]=availableTable.convertRowIndexToModel(index[i]);
        }
        PluginDescriptorModel pm = (PluginDescriptorModel) availableTable.getModel();
        PluginDescriptor[] pp = new PluginDescriptor[index.length];
        for (int i = 0; i < pp.length; i++) {
            pp[i] = pm.descriptors[index[i]];
        }
        return pp;
    }

    private void doInstall() {
        int x = 0;
        for (PluginDescriptor plugin : getSelectedInstallablePlugins()) {
            try {
                pluginManagerEditor.getPluginManager().installPlugin(plugin.getId(), null, true);
                pluginManagerEditor.pluginStatusChanged();
                x++;
            } catch (Throwable e) {
                pluginManagerEditor.getMessageDialogManager().showMessage(this, null, MessageDialogType.ERROR, null, e);
            }
        }
        if (x > 0) {
//                dbclient.getMessageDialogManager().showMessage(this, "Installing new plugins requires Application to be restarted to take effect", MessageDialogType.WARNING, null, null, null);
        }
    }

    private void doRollBack() {
    //what to do?
    }

    private void doManualInstall() {
        final JURLTextField url = new JURLTextField();
        final JTextField id = new JTextField();
        final JRadioButton isUrl = new JRadioButton(pluginManagerEditor.getMessageSet().get("PluginManagerEditor.ManualInstall.PluginURL"));
        final JRadioButton isId = new JRadioButton(pluginManagerEditor.getMessageSet().get("PluginManagerEditor.ManualInstall.PluginId"));
        ButtonGroup bg = new ButtonGroup();
        bg.add(isUrl);
        bg.add(isId);
        isUrl.setSelected(true);
        url.setEnabled(true);
        id.setEnabled(false);
        ItemListener listener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                url.setEnabled(isUrl.isSelected());
                id.setEnabled(isId.isSelected());
            }
        };
        isId.addItemListener(listener);
        isUrl.addItemListener(listener);
        JPanel p = new JPanel(new DumbGridBagLayout().addLine("[<isUrl][<=-url]").addLine("[<isId][<=-id]"));
        p.add(id, "id");
        p.add(url, "url");
        p.add(isId, "isId");
        p.add(isUrl, "isUrl");
        int r = JOptionPane.showConfirmDialog(this, p, pluginManagerEditor.getMessageSet().get("PluginManagerEditor.ManualInstall.Select"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.OK_OPTION) {
            try {
                if (isId.isSelected()) {
                    String pluginUrl = id.getText();
                    if (pluginUrl != null && pluginUrl.length() > 0) {
                        pluginManagerEditor.getPluginManager().installPlugin(pluginUrl, null, true);
                    }
                } else {
                    URL pluginUrl = url.getURL();
                    if (pluginUrl != null) {
                        pluginManagerEditor.getPluginManager().installPlugin(pluginUrl, true);
                    }
                }
            } catch (Throwable e) {
                pluginManagerEditor.getMessageDialogManager().showMessage(this, null, MessageDialogType.ERROR, null, e);
            }
            pluginManagerEditor.pluginStatusChanged();
        }
    }

    private class AvailableTableCellRenderer extends DefaultTableCellRenderer {

        Color initialFG = getForeground();
        Color initialBG = getBackground();

        //            Color initialBg=getBackground();
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (column == 6) {//status
//                        value = "uptodate".equals(value)
//                                ? dbclient.getMessageSet().get("PluginManagerEditor.CurrentUptodate") :
//                                "newer".equals(value) ? dbclient.getMessageSet().get("PluginManagerEditor.CurrentNewer") :
//                                        "older".equals(value) ? dbclient.getMessageSet().get("PluginManagerEditor.CurrentOlder") :
//                                                "";
            } else if (column == 5) {
//                        long ll=(Long) value;
//                        if(ll<1024){
//                            value=ll+" bytes";
//                        }else if(ll < 1024*1024){
//                            value=(ll/1024)+" Kb";
//                        }else {
//                            value=((ll*10/(1024*1024))/10.0)+" Mb";
//                        }
            } else if (column == 4 && (value instanceof Long)) {
                long ll = (Long) value;
                if (ll < 1024) {
                    value = ll + " bytes";
                } else if (ll < 1024 * 1024) {
                    value = ((ll * 10 / 1024) / 10.0) + " Ko";
                } else {
                    value = ((ll * 10 / (1024 * 1024)) / 10.0) + " Mo";
                }
            }
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            PluginDescriptorModel pm = (PluginDescriptorModel) table.getModel();
            row = availableTable.convertRowIndexToModel(row);
            PluginDescriptor plugin = pm.descriptors[row];
            switch (plugin.getStatus()) {
                case INSTALLED: {
                    setForeground(initialFG);
                    break;
                }
                case FOR_INSTALL: {
                    setForeground(Color.ORANGE.darker());
                    break;
                }
                case INSTALLABLE: {
                    setForeground(Color.GREEN.darker());
                    break;
                }
                case OBSOLETE: {
                    setForeground(Color.RED.darker());
                    break;
                }
                case UPDATABLE: {
                    setForeground(Color.GREEN.darker());
                    break;
                }
            }
            return this;
        }
    }

    void sourcesUpdated() {
        ArrayList<PluginDescriptor> i = new ArrayList<PluginDescriptor>();
        for (PluginDescriptor descriptor : pluginManagerEditor.getPluginManager().getAvailablePluginDescriptors()) {
            if (visibleStatus.contains(descriptor.getStatus())) {
                i.add(descriptor);
            }
        }
        pluginDescriptors = i.toArray(new PluginDescriptor[i.size()]);
        availableTable.setModel(new PluginDescriptorModel(pluginManagerEditor, pluginDescriptors, filter.getText()));
        if (availableTable.getModel().getRowCount() > 0) {
            availableTable.getSelectionModel().setSelectionInterval(0, 0);
        }
        availableTable.getComponent().repaint();
    }
}

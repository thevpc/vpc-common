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
package net.vpc.common.swings.pluginmanager;

import net.vpc.common.swings.PRSManager;
import net.vpc.common.prs.plugin.PluginRepository;
import net.vpc.common.swings.iswing.IJTable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 21 oct. 2007 00:32:56
*/
class PluginRepositoriesPanel extends JPanel {
    private IJTable sourcesTable;
    private PluginManagerEditor pluginManagerEditor;
    private JTextField filter;

    public PluginRepositoriesPanel(PluginManagerEditor _pluginManagerEditor) {
        this.pluginManagerEditor = _pluginManagerEditor;
        sourcesTable = pluginManagerEditor.getFactory().newInstance(IJTable.class);
        PluginRepository[] plugins = pluginManagerEditor.getPluginManager().getPluginRepositories();
        sourcesTable.setModel(new PluginRepositoryModel(pluginManagerEditor, plugins, ""));
        JScrollPane jsp = new JScrollPane(sourcesTable.getComponent());
        sourcesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (sourcesTable.getModel().getRowCount() > 0) {
            sourcesTable.setRowSelectionInterval(0, 0);
        }
        setLayout(new BorderLayout());
        this.add(jsp, BorderLayout.CENTER);
        filter = new JTextField();
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
                doRefreshSource();
            }
        });



        final JButton refresh = PRSManager.createButton("PluginManagerEditor.RefreshSource");
        Font font1 = refresh.getFont().deriveFont(Font.ITALIC, refresh.getFont().getSize() * 0.8f);
        pluginManagerEditor.getMonitor().add(refresh);
        refresh.setFont(font1);
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        int id=pluginManagerEditor.getMonitor().startIndeterminate();
                        try {
                            doRefreshSource();
                        } finally {
                            pluginManagerEditor.getMonitor().stop(id);
                        }
                    }
                }.start();
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
        b.add(refresh);
        b.add(Box.createHorizontalStrut(5));
        this.add(b, BorderLayout.PAGE_START);
        PRSManager.updateOnFirstComponentShown(this, pluginManagerEditor.getResourceSetHolder());
    }

    private void doRefreshSource(){
        for (PluginRepository pluginRepository : getSelectedInstallablePlugins()) {
            pluginRepository.refresh();
        }
        PluginRepository[] plugins = pluginManagerEditor.getPluginManager().getPluginRepositories();
        sourcesTable.setModel(new PluginRepositoryModel(pluginManagerEditor, plugins,filter.getText()));
        pluginManagerEditor.pluginStatusChanged();
    }

    public PluginRepository[] getSelectedInstallablePlugins() {
        int[] index = sourcesTable.getSelectedRows();
        for (int i = 0; i < index.length; i++) {
            index[i]=sourcesTable.convertRowIndexToModel(index[i]);
        }
        PluginRepositoryModel pm = (PluginRepositoryModel) sourcesTable.getModel();
        PluginRepository[] pp = new PluginRepository[index.length];
        for (int i = 0; i < pp.length; i++) {
            pp[i] = pm.getPluginRepository(index[i]);
        }
        return pp;
    }

}

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

import net.thevpc.common.prs.plugin.PluginRepository;
import net.thevpc.common.swings.iswing.IJTable;
import net.thevpc.common.swings.prs.PRSManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* %creationtime 21 oct. 2007 00:32:56
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

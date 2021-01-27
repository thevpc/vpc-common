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
package net.thevpc.common.swing.pluginmanager;

import net.thevpc.common.prs.plugin.Plugin;
import net.thevpc.common.prs.plugin.PluginDependency;
import net.thevpc.common.swing.DumbGridBagLayout;
import net.thevpc.common.swing.iswing.IJTable;
import net.thevpc.common.swing.prs.PRSManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 30 dec. 2006 19:09:06
 */
class PluginPropertiesPanel extends JPanel {
    private JTextField id = new JTextField();
    private JTextField title = new JTextField();
    private JTextField version = new JTextField();
    private JTextArea description = new JTextArea();
    private JTextArea log = new JTextArea();
    private JTextField author = new JTextField();
    private JTextField contributors = new JTextField();
    private JTextField url = new JTextField();
    private JTextField category = new JTextField("");
    private JTextField dependencies = new JTextField("");
    private JTextField reverseDependencies = new JTextField("");
    private JCheckBox system = PRSManager.createCheck("PluginPropertiesPanel.PluginSystem", false);
    private JCheckBox pluginEnabled = PRSManager.createCheck("PluginPropertiesPanel.PluginEnabled", false);
    private JCheckBox valid = PRSManager.createCheck("PluginPropertiesPanel.PluginValid", false);
    private JCheckBox pluginLoaded = PRSManager.createCheck("PluginPropertiesPanel.PluginLoaded", false);

    private Plugin plugin;
    private JPanel properties;
    private IJTable mappingTable;
    private PluginManagerEditor pluginManagerEditor;

    public PluginPropertiesPanel(PluginManagerEditor _pluginManagerEditor) {
        super(new BorderLayout());
        this.pluginManagerEditor = _pluginManagerEditor;
        id.setEditable(false);
        title.setEditable(false);
        contributors.setEditable(false);
        version.setEditable(false);
        description.setEditable(false);
        log.setEditable(false);
        category.setEditable(false);
        dependencies.setEditable(false);
        author.setEditable(false);
        url.setEditable(false);
        reverseDependencies.setEditable(false);
        system.setEnabled(false);
        pluginEnabled.setEnabled(true);
        pluginLoaded.setEnabled(true);
        valid.setEnabled(false);

        properties = new JPanel(new DumbGridBagLayout(
                "[<idLabel][<id+=][<versionLabel][<version+=][<system+=]\n" +
                        "[<titleLabel][<title+=][<categoryLabel][<category+=][<loaded+=]\n" +
                        "[<urlLabel][<url+=::][<enabled]\n" +
                        "[<authorLabel][<author+=::][<valid+=]\n" +
                        "[<contributorsLabel][<contributors+= :::]\n" +
                        "[<dependenciesLabel][<dependencies+= :::]\n" +
                        "[<revdependenciesLabel][<revdependencies+= :::]\n" +
                        ""
        )
                .setInsets(".*", new Insets(4, 4, 4, 4))
                .setInsets("idLabel", new Insets(8, 4, 4, 4))
                .setInsets("description", new Insets(4, 4, 8, 4))
        );

        properties.add("idLabel", PRSManager.createLabel("PluginPropertiesPanel.Id"));
        properties.add("titleLabel", PRSManager.createLabel("PluginPropertiesPanel.Title"));
        properties.add("authorLabel", PRSManager.createLabel("PluginPropertiesPanel.Author"));
        properties.add("contributorsLabel", PRSManager.createLabel("PluginPropertiesPanel.Contributors"));
        properties.add("categoryLabel", PRSManager.createLabel("PluginPropertiesPanel.Category"));
        properties.add("versionLabel", PRSManager.createLabel("PluginPropertiesPanel.Version"));
        properties.add("dependenciesLabel", PRSManager.createLabel("PluginPropertiesPanel.Dependencies"));
        properties.add("revdependenciesLabel", PRSManager.createLabel("PluginPropertiesPanel.ReverseDependencies"));
        properties.add("urlLabel", PRSManager.createLabel("PluginPropertiesPanel.URL"));

//        properties.add("descriptionLabel", PRSManager.createLabel("PluginPropertiesPanel.Description"));

        properties.add("id", id);
        properties.add("title", title);
        properties.add("author", author);
        properties.add("category", category);
        properties.add("dependencies", dependencies);
        properties.add("revdependencies", reverseDependencies);
        properties.add("version", version);
        properties.add("contributors", contributors);
        properties.add("url", url);
        properties.add("system", system);
        properties.add("enabled", pluginEnabled);
        properties.add("loaded", pluginLoaded);
        properties.add("valid", valid);


        JTabbedPane jtp = new JTabbedPane();
        PRSManager.addSupport(jtp, "PluginPropertiesPanel.TabbedPane");
        jtp.addTab("1", properties);

        mappingTable = pluginManagerEditor.getFactory().newInstance(IJTable.class);
        JScrollPane mappingJsp = new JScrollPane(mappingTable.getComponent());
        mappingTable.setDefaultRenderer(Class.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                Class c1 = (Class) table.getModel().getValueAt(row, 0);
//                Class c2 = (Class) table.getModel().getValueAt(row, 1);
                super.getTableCellRendererComponent(table, ((Class) value).getSimpleName(), isSelected, hasFocus, row, column);
                return this;
            }
        });

        mappingJsp.setPreferredSize(new Dimension(600, 100));
        add(jtp);
        pluginEnabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (plugin != null && !updating) {
                    plugin.setEnabled(pluginEnabled.isSelected());
                    valid.setSelected(plugin.getDescriptor().isValid());
                }
            }
        });

        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JScrollPane(description));

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.add(new JScrollPane(log));

        jtp.addTab("2", descPanel);
        jtp.addTab("3", mappingJsp);
        jtp.addTab("4", logPanel);

        PRSManager.update(this, pluginManagerEditor.getResourceSetHolder());
        setPlugin(pluginManagerEditor.getPluginManager().getSystemPlugins()[0]);
    }

    private boolean updating = false;

    public void setPlugin(Plugin plugin) {
        try {
            updating = true;
            this.plugin = plugin;
            pluginEnabled.setEnabled(!(plugin == null || plugin.getDescriptor().isSystem()));
            valid.setEnabled(false);
            pluginLoaded.setEnabled(false);
            java.util.List<ClassMappingModel.Mapping> mappings = new ArrayList<ClassMappingModel.Mapping>();
            if (plugin != null) {
                Set<String> interfaces = plugin.getDescriptor().getInterfaces();
                for (String cls : interfaces) {
                    Set<String> implementations = plugin.getDescriptor().getImplementations(cls);
                    if (implementations.size() > 0) {
                        for (String impl : implementations) {
                            mappings.add(new ClassMappingModel.Mapping(cls, impl));
                        }
                    } else {
                        mappings.add(new ClassMappingModel.Mapping(cls, null));
                    }
                }
            }
            mappingTable.setModel(new ClassMappingModel(pluginManagerEditor.getPluginManager().getSystemPlugins()[0], mappings));
            id.setText(plugin == null ? "" : plugin.getId());
            title.setText(plugin == null ? "" : plugin.getDescriptor().getTitle());
            author.setText(plugin == null ? "" : plugin.getDescriptor().getAuthor());
            version.setText(plugin == null ? "" : plugin.getDescriptor().getVersion().toString());
            category.setText(plugin == null ? "" : plugin.getDescriptor().getCategory());
            StringBuilder dependenciesString = new StringBuilder();
            for (PluginDependency s : plugin.getDescriptor().getDependencies()) {
                if (s != null) {
                    if (dependenciesString.length() > 0) {
                        dependenciesString.append(", ");
                    }
                    dependenciesString.append(s);
                }
            }
            dependencies.setText(plugin == null ? "" : dependenciesString.toString());
            StringBuilder reverseDependenciesString = new StringBuilder();
            for (String s : plugin.getDescriptor().getReverseDependencies()) {
                if (s != null) {
                    if (reverseDependenciesString.length() > 0) {
                        reverseDependenciesString.append(", ");
                    }
                    reverseDependenciesString.append(s);
                }
            }
            reverseDependencies.setText(plugin == null ? "" : reverseDependenciesString.toString());
            contributors.setText(plugin == null ? "" : plugin.getDescriptor().getContributors());
            description.setText(plugin == null ? "" : plugin.getDescriptor().getDescription());
            log.setText(plugin == null ? "" : plugin.getDescriptor().getLog().tail(100000));
            url.setText(plugin == null ? "" : plugin.getDescriptor().getHomeUrl());
            pluginEnabled.setSelected(plugin != null && plugin.isEnabled());
            pluginLoaded.setSelected(plugin != null && plugin.getDescriptor().isValid());
            system.setSelected(plugin != null && plugin.getDescriptor().isSystem());
            valid.setSelected(plugin != null && plugin.getDescriptor().isValid());
        } finally {
            updating = false;
        }
    }
}

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
import net.vpc.common.prs.plugin.PluginDependency;
import net.vpc.common.prs.plugin.PluginDescriptor;
import net.vpc.common.swings.DumbGridBagLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 30 dec. 2006 19:09:06
 */
class PluginDescriptorPropertiesPanel extends JPanel {

    private JTextField id = new JTextField();
    private JTextField title = new JTextField();
    private JTextField version = new JTextField();
    private JTextArea description = new JTextArea();
    private JTextField author = new JTextField();
    private JTextField contributors = new JTextField();
    private JTextField url = new JTextField();
    private JTextField category = new JTextField("");
    private JTextField dependencies = new JTextField("");
    private PluginDescriptor plugin;
    private JPanel properties;
    private PluginManagerEditor pluginManagerEditor;

    public PluginDescriptorPropertiesPanel(PluginManagerEditor _pluginManagerEditor) {
        super(new BorderLayout());
        this.pluginManagerEditor = _pluginManagerEditor;
        id.setEditable(false);
        title.setEditable(false);
        contributors.setEditable(false);
        version.setEditable(false);
        description.setEditable(false);
        category.setEditable(false);
        dependencies.setEditable(false);
        author.setEditable(false);
        url.setEditable(false);

        properties = new JPanel(new DumbGridBagLayout("[<idLabel][<id+=][<versionLabel][<version+=]\n" +
                "[<titleLabel][<title+=][<categoryLabel][<category+=]\n" +
                "[<urlLabel][<url+=::]\n" +
                "[<authorLabel][<author+=:::]\n" +
                "[<contributorsLabel][<contributors+= :::]\n" +
                "[<dependenciesLabel][<dependencies+= :::]\n" +
                "").setInsets(".*", new Insets(4, 4, 4, 4)).setInsets("idLabel", new Insets(8, 4, 4, 4)).setInsets("description", new Insets(4, 4, 8, 4)));

        properties.add("idLabel", PRSManager.createLabel("PluginPropertiesPanel.Id"));
        properties.add("titleLabel", PRSManager.createLabel("PluginPropertiesPanel.Title"));
        properties.add("authorLabel", PRSManager.createLabel("PluginPropertiesPanel.Author"));
        properties.add("contributorsLabel", PRSManager.createLabel("PluginPropertiesPanel.Contributors"));
        properties.add("categoryLabel", PRSManager.createLabel("PluginPropertiesPanel.Category"));
        properties.add("versionLabel", PRSManager.createLabel("PluginPropertiesPanel.Version"));
        properties.add("dependenciesLabel", PRSManager.createLabel("PluginPropertiesPanel.Dependencies"));
        properties.add("urlLabel", PRSManager.createLabel("PluginPropertiesPanel.URL"));

        //        properties.add("descriptionLabel", PRSManager.createLabel("PluginPropertiesPanel.Description"));

        properties.add("id", id);
        properties.add("title", title);
        properties.add("author", author);
        properties.add("category", category);
        properties.add("dependencies", dependencies);
        properties.add("version", version);
        properties.add("contributors", contributors);
        properties.add("url", url);

        JTabbedPane jtp = new JTabbedPane();
        PRSManager.addSupport(jtp, "PluginPropertiesPanel.TabbedPane");
        jtp.addTab("1", properties);

        add(jtp);

        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JScrollPane(description));
        jtp.addTab("3", descPanel);

        PRSManager.update(this, pluginManagerEditor.getResourceSetHolder());
    }
    private boolean updating = false;

    public void setPlugin(PluginDescriptor plugin) {
        try {
            updating = true;
            this.plugin = plugin;
            id.setText(plugin == null ? "" : plugin.getId());
            title.setText(plugin == null ? "" : plugin.getTitle());
            author.setText(plugin == null ? "" : plugin.getAuthor());
            version.setText(plugin == null ? "" : plugin.getVersion().toString());
            category.setText(plugin == null ? "" : plugin.getCategory());
            StringBuilder dd = new StringBuilder();
            PluginDependency[] deps = plugin==null?null:plugin.getDependencies();
            if (deps!=null) {
                for (PluginDependency s : deps) {
                    if (dd.length() > 0) {
                        dd.append(", ");
                    }
                    dd.append(s);
                }
            }
            dependencies.setText(plugin == null ? "" : dd.toString());
            contributors.setText(plugin == null ? "" : plugin.getContributors());
            description.setText(plugin == null ? "" : plugin.getDescription());
            url.setText(plugin == null ? "" : plugin.getHomeUrl());
        } finally {
            updating = false;
        }
    }
}
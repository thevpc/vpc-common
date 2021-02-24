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

import net.thevpc.common.prs.ResourceSetHolder;
import net.thevpc.common.prs.factory.Factory;
import net.thevpc.common.prs.messageset.MessageSet;
import net.thevpc.common.prs.plugin.PluginDescriptor;
import net.thevpc.common.prs.plugin.PluginManager;
import net.thevpc.common.swings.SwingUtilities3;
import net.thevpc.common.swings.dialog.MessageDialogManager;
import net.thevpc.common.swings.prs.PRSManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 15 dec. 2006 01:41:47
 */
public class PluginManagerEditor<App> extends JPanel {
    //    private JTable table;
    private App application;
    private Map<String, PluginDescriptor.Status> status = new HashMap<String, PluginDescriptor.Status>();
    private PluginTaskMonitor monitor = new PluginTaskMonitor();
    public PluginManager pluginManager;
    public Factory factory;
    public MessageDialogManager messageDialogManager;
    private InstalledPluginsPanel installedPluginsPanel;
    private AvailablePluginsPanel availablePluginsPanel;
    private AvailablePluginsPanel pendingPluginsPanel;
    private PluginRepositoriesPanel pluginRepositoriesPanel;
    private PluginSummaryPanel pluginSummaryPanel;
    private boolean firstShow;
    private ResourceSetHolder resourceSetHolder;

    public PluginManagerEditor() {
        super(new BorderLayout());
    }
    
    public PluginManagerEditor(App app, ResourceSetHolder resourceSetHolder,PluginManager pluginManager, Factory factory, MessageDialogManager messageDialogManager) {
        super(new BorderLayout());
        init(app, resourceSetHolder,pluginManager, factory, messageDialogManager);
    }
    
    protected void init(App app, ResourceSetHolder resourceSetHolder,PluginManager pluginManager, Factory componentManager, MessageDialogManager messageDialogManager) {
        this.application = app;
        this.resourceSetHolder = resourceSetHolder;
        this.pluginManager = pluginManager;
        this.factory = componentManager;
        this.messageDialogManager = messageDialogManager;


        pluginSummaryPanel = new PluginSummaryPanel(this);
        installedPluginsPanel = new InstalledPluginsPanel(this);
        availablePluginsPanel = new AvailablePluginsPanel(this, PluginDescriptor.Status.INSTALLABLE, PluginDescriptor.Status.UPDATABLE);
        pendingPluginsPanel = new AvailablePluginsPanel(this, PluginDescriptor.Status.FOR_INSTALL, PluginDescriptor.Status.FOR_UNINSTALL);
        pluginRepositoriesPanel = new PluginRepositoriesPanel(this);
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent event) {
                if (!firstShow) {
                    firstShow = true;
                    PRSManager.update(PluginManagerEditor.this, getResourceSetHolder());
                }
            }
        });
        add(createMainComponent());
        PRSManager.update(this,getResourceSetHolder());
    }

    public ResourceSetHolder getResourceSetHolder() {
        return resourceSetHolder;
    }

    protected Component createMainComponent() {
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("Summary", pluginSummaryPanel);
        pane.addTab("Installed", installedPluginsPanel);
        pane.addTab("Available", availablePluginsPanel);
        pane.addTab("Pending", pendingPluginsPanel);
        pane.addTab("Sources", pluginRepositoriesPanel);
        PRSManager.addSupport(pane,"PluginManagerEditor.MainTabbedPane");
        JPanel p = new JPanel(new BorderLayout());
        p.add(pane, BorderLayout.CENTER);
        p.add(getMonitor().getComponent(), BorderLayout.PAGE_END);
        return p;
    }

    boolean matchesPluginDescriptor(String s, PluginDescriptor pi) {
        String[] all = new String[]{pi.getId(), pi.getAuthor(), pi.getCategory(), pi.getContributors(), pi.getDescription(), pi.getTitle(), getMessageSet().get("PluginManagerEditor.Status." + pi.getStatus())};
        for (int i = 0; i < all.length; i++) {
            all[i] = all[i] == null ? "" : all[i].toLowerCase();
        }
        for (StringTokenizer stringTokenizer = new StringTokenizer(s, " +;,"); stringTokenizer.hasMoreTokens();) {
            String s1 = stringTokenizer.nextToken().toLowerCase();
            boolean ok = false;
            for (String s2 : all) {
                if (s2.contains(s1)) {
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                return false;
            }
        }
        return true;
    }

    Map<String, PluginDescriptor.Status> getStatus() {
        return status;
    }

    public App getApplication() {
        return application;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public Factory getFactory() {
        return factory;
    }

    public MessageSet getMessageSet() {
        return getResourceSetHolder().getMessageSet();
    }

    public MessageDialogManager getMessageDialogManager() {
        return messageDialogManager;
    }

    public PluginTaskMonitor getMonitor() {
        return monitor;
    }

    public void pluginStatusChanged() {
        getPluginManager().getAvailablePluginDescriptors();
        SwingUtilities3.invokeLater(new Runnable() {

            public void run() {
                if (pendingPluginsPanel != null) {
                    pendingPluginsPanel.sourcesUpdated();
                }
                if (availablePluginsPanel != null) {
                    availablePluginsPanel.sourcesUpdated();
                }
                if (pluginSummaryPanel != null) {
                    pluginSummaryPanel.doUpdateValues();
                }
            }
        });
    }
}

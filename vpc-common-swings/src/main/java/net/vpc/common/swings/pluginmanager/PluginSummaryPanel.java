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
import net.vpc.common.prs.messageset.MessageSet;
import net.vpc.common.prs.plugin.LocalRepository;
import net.vpc.common.prs.plugin.Plugin;
import net.vpc.common.prs.plugin.PluginDescriptor;
import net.vpc.common.swings.DumbGridBagLayout;
import net.vpc.common.swings.SwingUtilities3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime Oct 22, 2007 7:19:02 PM
 */
class PluginSummaryPanel extends JPanel {
    PluginManagerEditor pluginManagerEditor;
    JTextField repository=new JTextField();
    JLabel pluginEnabledCount=new JLabel();
    JLabel pluginDisabledCount=new JLabel();
    JLabel pluginInvalidCount=new JLabel();
    JLabel pluginInstallableCount=new JLabel();
    JLabel pluginUpdatableCount=new JLabel();
    JLabel pluginPendingInstallCount=new JLabel();
    JLabel pluginPendingUninstallCount=new JLabel();
    JButton openRepository=new JButton("...");
    PluginSummaryPanel(PluginManagerEditor _pluginManagerEditor) {
        this.pluginManagerEditor=_pluginManagerEditor;
        setLayout(
                new DumbGridBagLayout()
                .addLine("[<+==infoLabel.]")
                .addLine("[<repositoryLabel.]")
                .addLine("[<-=repository.]")
                .addLine("[<pluginEnabledCount][<-=pluginEnabledCountLabel.]")
                .addLine("[<pluginDisabledCount][<-=pluginDisabledCountLabel.]")
                .addLine("[<pluginInvalidCount][<-=pluginInvalidCountLabel.]")
                .addLine("[<pluginInstallableCount][<-=pluginInstallableCountLabel.]")
                .addLine("[<pluginUpdatableCount][<-=pluginUpdatableCountLabel.]")
                .addLine("[<pluginPendingInstallCount][<-=pluginPendingInstallCountLabel.]")
                .addLine("[<pluginPendingUninstallCount][<-=pluginPendingUninstallCountLabel.]")
                .setInsets(".*",new Insets(3,10,3,10))
        );
        JLabel infoLabel= PRSManager.createLabel("PluginSummaryPanel.infoLabel");
        JLabel repositoryLabel= PRSManager.createLabel("PluginSummaryPanel.repositoryLabel");
        JLabel pluginEnabledCountLabel = PRSManager.createLabel("PluginSummaryPanel.pluginEnabledCountLabel");
        JLabel pluginDisabledCountLabel= PRSManager.createLabel("PluginSummaryPanel.pluginDisabledCountLabel");
        JLabel pluginInvalidCountLabel= PRSManager.createLabel("PluginSummaryPanel.pluginInvalidCountLabel");
        JLabel pluginInstallableCountLabel = PRSManager.createLabel("PluginSummaryPanel.pluginInstallableCountLabel");
        JLabel pluginUpdatableCountLabel = PRSManager.createLabel("PluginSummaryPanel.pluginUpdatableCountLabel");
        JLabel pluginPendingInstallCountLabel= PRSManager.createLabel("PluginSummaryPanel.pluginPendingInstallCountLabel");
        JLabel pluginPendingUninstallCountLabel= PRSManager.createLabel("PluginSummaryPanel.pluginPendingUninstallCountLabel");
        

        add(infoLabel,"infoLabel");
        add(repositoryLabel,"repositoryLabel");
        add(pluginEnabledCountLabel,"pluginEnabledCountLabel");
        add(pluginDisabledCountLabel,"pluginDisabledCountLabel");
        add(pluginInvalidCountLabel,"pluginInvalidCountLabel");
        add(pluginInstallableCountLabel,"pluginInstallableCountLabel");
        add(pluginUpdatableCountLabel,"pluginUpdatableCountLabel");
        add(pluginPendingInstallCountLabel,"pluginPendingInstallCountLabel");
        add(pluginPendingUninstallCountLabel,"pluginPendingUninstallCountLabel");
        
        Box box=Box.createHorizontalBox();
        box.add(repository);
        box.add(openRepository);
        repository.setEditable(false);
        openRepository.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    LocalRepository localRepository = pluginManagerEditor.getPluginManager().getLocalRepository();
                    URL repoURL = localRepository.getRepositoryURL();
                    if("file".equalsIgnoreCase(repoURL.getProtocol())){
                        SwingUtilities3.openFile(new File(repoURL.getFile()));
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,e.getMessage());
                }
            }
        });
        openRepository.setMargin(new Insets(0,0,0,0));
        add(box,"repository");
        add(pluginEnabledCount,"pluginEnabledCount");
        add(pluginDisabledCount,"pluginDisabledCount");
        add(pluginInvalidCount,"pluginInvalidCount");
        add(pluginInstallableCount,"pluginInstallableCount");
        add(pluginUpdatableCount,"pluginUpdatableCount");
        add(pluginPendingInstallCount,"pluginPendingInstallCount");
        add(pluginPendingUninstallCount,"pluginPendingUninstallCount");
        PRSManager.updateOnFirstComponentShown(this, pluginManagerEditor.getResourceSetHolder());
        doUpdateValues(false);
    }

    void doUpdateValues(){
        doUpdateValues(true);
    }
    
    void doUpdateValues(boolean doWait){

        pluginEnabledCount.setText("...");
        pluginDisabledCount.setText("...");
        pluginInvalidCount.setText("...");
        pluginInstallableCount.setText("...");
        pluginUpdatableCount.setText("...");
        pluginPendingInstallCount.setText("...");
        pluginPendingUninstallCount.setText("...");
        repository.setText(pluginManagerEditor.getPluginManager().getLocalRepository().getRepositoryURL().toString());
        int enabledPlugins=0;
        int disabledPlugins=0;
        int invalidPlugins=0;
        for (Plugin plugin : getPluginManagerEditor().getPluginManager().getAllPlugins()) {
            if(plugin.isEnabled()){
                enabledPlugins++;
            }else if(plugin.getDescriptor().isValid()){
                disabledPlugins++;
            }else{
                invalidPlugins++;
            }
        }
        pluginEnabledCount.setText(String.valueOf(enabledPlugins));
        pluginDisabledCount.setText(String.valueOf(disabledPlugins));
        pluginInvalidCount.setText(String.valueOf(invalidPlugins));
        int installableCount=0;
        int updatableCount=0;
        int pendingInstallCount=0;
        int pendingUninstallCount=0;
        if(doWait){
            for (PluginDescriptor pluginInfo : pluginManagerEditor.getPluginManager().getAvailablePluginDescriptors()) {
                switch (pluginInfo.getStatus()){
                    case FOR_INSTALL:{
                         pendingInstallCount++;
                        break;
                    }
                    case FOR_UNINSTALL:
                    {
                        pendingUninstallCount ++;
                        break;
                    }
                    case INSTALLABLE:
                    {
                        installableCount++;
                        break;
                    }
                    case UPDATABLE:
                    {
                        updatableCount++;
                        break;
                    }
                }
            }
        }
        MessageSet ms = pluginManagerEditor.getResourceSetHolder().getMessageSet();
        pluginInstallableCount.setText(ms.get("PluginSummaryPanel.pluginInstallableCount",new Object[]{installableCount}));
        pluginUpdatableCount.setText(ms.get("PluginSummaryPanel.pluginUpdatableCount",new Object[]{updatableCount}));
        pluginPendingInstallCount.setText(ms.get("PluginSummaryPanel.pluginPendingInstallCount",new Object[]{pendingInstallCount}));
        pluginPendingUninstallCount.setText(ms.get("PluginSummaryPanel.pluginPendingUninstallCount",new Object[]{pendingUninstallCount}));
    }

    public PluginManagerEditor getPluginManagerEditor() {
        return pluginManagerEditor;
    }
}

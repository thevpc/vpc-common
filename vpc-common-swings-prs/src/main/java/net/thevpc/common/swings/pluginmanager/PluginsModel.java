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

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 21 oct. 2007 00:24:52
*/
class PluginsModel extends AbstractTableModel {
    Plugin[] plugins;
    private PluginManagerEditor pluginManagerEditor;

    public PluginsModel(PluginManagerEditor pluginManagerEditor, Plugin[] plugins, String filter) {
        this.pluginManagerEditor = pluginManagerEditor;
        if (filter == null || filter.length() == 0 || "*".equals(filter)) {
            this.plugins = plugins;
        } else {
            ArrayList<Plugin> all = new ArrayList<Plugin>();
            for (Plugin plugin : plugins) {
                if (pluginManagerEditor.matchesPluginDescriptor(filter, plugin.getDescriptor())) {
                    all.add(plugin);
                }
            }
            this.plugins = all.toArray(new Plugin[all.size()]);
        }
    }

    public int getRowCount() {
        return plugins == null ? 0 : plugins.length;
    }

    public int getColumnCount() {
        return 6;//6;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Title");
            }
            case 1: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Version");
            }
            case 2: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Author");
            }
            case 3: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Category");
            }
            case 4: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Enabled");
            }
            case 5: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Valid");
            }
            case 6: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Status");
            }
        }
        return super.getColumnName(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return plugins[rowIndex].getDescriptor().getTitle();
            }
            case 1: {
                return plugins[rowIndex].getDescriptor().getVersion();
            }
            case 2: {
                return plugins[rowIndex].getDescriptor().getAuthor();
            }
            case 3: {
                return plugins[rowIndex].getDescriptor().getCategory();
            }
            case 4: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Enabled." + plugins[rowIndex].isEnabled());
            }
            case 5: {
                Plugin p = plugins[rowIndex];
                boolean valid = p.getDescriptor().isValid();
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Valid." + valid);
            }
            case 6: {
                return pluginManagerEditor.getStatus().get(plugins[rowIndex].getId());
            }
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
}

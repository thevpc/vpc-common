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

import net.vpc.common.prs.plugin.Plugin;

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

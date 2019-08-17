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

import net.vpc.common.prs.plugin.PluginDescriptor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 21 oct. 2007 00:35:57
*/
public class PluginDescriptorModel extends AbstractTableModel {
    PluginDescriptor[] descriptors;
    private PluginManagerEditor pluginManagerEditor;

    public PluginDescriptorModel(PluginManagerEditor pluginManagerEditor, PluginDescriptor[] descriptors, String filter) {
        this.pluginManagerEditor = pluginManagerEditor;
        if (filter == null || filter.length() == 0 || "*".equals(filter)) {
            this.descriptors = descriptors;
        } else {
            ArrayList<PluginDescriptor> all = new ArrayList<PluginDescriptor>();
            for (PluginDescriptor plugin : descriptors) {
                if (pluginManagerEditor.matchesPluginDescriptor(filter, plugin)) {
                    all.add(plugin);
                }
            }
            this.descriptors = all.toArray(new PluginDescriptor[all.size()]);
        }
    }

    public int getRowCount() {
        return descriptors == null ? 0 : descriptors.length;
    }

    public int getColumnCount() {
        return 6;
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
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Size");
            }
            case 5: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Status");
            }
        }
        return super.getColumnName(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return descriptors[rowIndex].getTitle();
            }
            case 1: {
                return descriptors[rowIndex].getVersion();
            }
            case 2: {
                return descriptors[rowIndex].getAuthor();
            }
            case 3: {
                return descriptors[rowIndex].getCategory();
            }
            case 4: {
                return descriptors[rowIndex].getBinarySize();
            }
            case 5: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Status." + descriptors[rowIndex].getStatus());
            }
        }
        return null;
    }

    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 4) {
            return Long.class;
        }
        return String.class;
    }
}

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

import net.thevpc.common.prs.plugin.PluginDescriptor;

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

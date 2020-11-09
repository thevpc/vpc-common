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

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 21 oct. 2007 00:33:08
*/
class PluginRepositoryModel extends AbstractTableModel {
    private PluginRepository[] repositories;
    private PluginManagerEditor pluginManagerEditor;

    public PluginRepositoryModel(PluginManagerEditor pluginManagerEditor, PluginRepository[] repositories,String filter) {
        this.pluginManagerEditor = pluginManagerEditor;
        if (filter == null || filter.length() == 0 || "*".equals(filter)) {
            this.repositories = repositories;
        } else {
            ArrayList<PluginRepository> all = new ArrayList<PluginRepository>();
            for (PluginRepository plugin : repositories) {
                if (matchesPluginDescriptor(filter, plugin)) {
                    all.add(plugin);
                }
            }
            this.repositories = all.toArray(new PluginRepository[all.size()]);
        }
        this.repositories = repositories;
    }

    public int getRowCount() {
        return repositories == null ? 0 : repositories.length;
    }

    public int getColumnCount() {
        return 3;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Url");
            }
            case 1: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Load");
            }
            case 2: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.Size");
            }
        }
        return super.getColumnName(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return repositories[rowIndex].getURL();
            }
            case 1: {
                return localizeLoad(repositories[rowIndex].getLoad());
            }
            case 2: {
                return repositories[rowIndex].getLoad()!=PluginRepository.UNKNOWN? repositories[rowIndex].size():0;
            }
        }
        return null;
    }

    private String localizeLoad(int load){
        switch (load) {
            case PluginRepository.UNKNOWN: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.PluginRepository.UNKNOWN");
            }
            case PluginRepository.UNREACHABLE: {
                return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.PluginRepository.UNREACHABLE");
            }
            default: {
                if(load<3){
                    return pluginManagerEditor.getMessageSet().get("PluginManagerEditor.PluginRepository.EXCELLENT");
                }
                double v = 1000.0 / load;
                v=Math.floor(v*100)/100;
                return String.valueOf(v);
            }
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex==2){
            return Integer.class;
        }
        return String.class;
    }

    public PluginRepository getPluginRepository(int index){
        return repositories[index];
    }
    boolean matchesPluginDescriptor(String s, PluginRepository pi) {
        String[] all = new String[]{pi.getURL().toString(),localizeLoad(pi.getLoad()),String.valueOf(pi.size())};
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

}

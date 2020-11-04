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

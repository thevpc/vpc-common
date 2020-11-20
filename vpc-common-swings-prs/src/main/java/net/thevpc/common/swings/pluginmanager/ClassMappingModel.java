/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* %creationtime 30 dec. 2006 19:15:19
*/
class ClassMappingModel extends AbstractTableModel {
    public static class Mapping{
        private String implemtation;
        private String contract;

        public Mapping(String contract,String implemtation) {
            this.implemtation = implemtation;
            this.contract = contract;
        }
    }
    private Mapping[] mappings;
    private Plugin plugin;

    public ClassMappingModel(Plugin plugin, List<Mapping> entries) {
        this(plugin, entries.toArray(new Mapping[entries.size()]));
    }

    public ClassMappingModel(Plugin plugin,Mapping[] entries) {
        this.plugin=plugin;
        mappings= entries;
    }

    public ClassMappingModel(Mapping[] plugins) {
        this.mappings = plugins;
    }

    public int getRowCount() {
        return mappings ==null?0: mappings.length;
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0:{
                return plugin.getMessageSet().get("ClassMappingModel.Type");
            }
            case 1:{
                return plugin.getMessageSet().get("ClassMappingModel.Implementation");
            }
        }
        return super.getColumnName(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:{
                return mappings[rowIndex].contract;
            }
            case 1:{
                return mappings[rowIndex].implemtation;
            }
        }
        return null;
    }


    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
}

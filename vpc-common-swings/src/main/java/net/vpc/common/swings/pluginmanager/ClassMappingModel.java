/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
* @creationtime 30 dec. 2006 19:15:19
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

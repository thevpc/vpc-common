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
package net.vpc.common.swings.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import net.vpc.common.swings.SwingUtilities3;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 mai 2006 16:55:23
 */
public class TableFilterModel extends AbstractTableModel {
    private boolean rebuildInProgess;
    private boolean rebuildOneMoreTime;
    private int[] visibleColumns;
    private int[] visibleRows;
    private int newColumnCount;
    private int newRowCount;
    private TableModel baseModel;
    private TableRowFilter tableRowFilter;
    private TableColumnFilter tableColumnFilter;

    public TableFilterModel(TableModel baseModel) {
        this.baseModel = baseModel;
        rebuild();
        baseModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
//                TableModelEvent e0=new TableModelEvent(TableFilterModel.this);
                rebuild();
            }
        });
    }

    public int getColumnCount() {
        return newColumnCount;
    }

    public int getRowCount() {
        return newRowCount;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return baseModel.getValueAt(visibleRows[rowIndex], visibleColumns[columnIndex]);
    }

    public synchronized void rebuild() {
        new Thread(){
            @Override
            public void run() {
                rebuild0();
            }
            
        }.start();
    }
    
    public void rebuild0() {
        if(rebuildInProgess){
            rebuildOneMoreTime=true;
            return;
        }
        rebuildInProgess=true;
        try {
            int[] visibleColumns;
            int[] visibleRows;
            int newColumnCount;
            int newRowCount;
            if (tableColumnFilter != null) {
                int[] x = new int[baseModel.getColumnCount()];
                newColumnCount = 0;
                for (int i = 0; i < x.length; i++) {
                    if (tableColumnFilter.acceptColumn(i, baseModel)) {
                        x[newColumnCount] = i;
                        newColumnCount++;
                    }
                }
                visibleColumns = new int[newColumnCount];
                System.arraycopy(x, 0, visibleColumns, 0, newColumnCount);
            } else {
                visibleColumns = new int[baseModel.getColumnCount()];
                for (int i = 0; i < visibleColumns.length; i++) {
                    visibleColumns[i] = i;
                }
                newColumnCount = visibleColumns.length;
            }
            if (tableRowFilter != null) {
                int[] x = new int[baseModel.getRowCount()];
                newRowCount = 0;
                for (int i = 0; i < x.length; i++) {
                    if (tableRowFilter.acceptRow(i, baseModel)) {
                        x[newRowCount] = i;
                        newRowCount++;
                    }
                }
                visibleRows = new int[newRowCount];
                System.arraycopy(x, 0, visibleRows, 0, newRowCount);
            } else {
                visibleRows = new int[baseModel.getRowCount()];
                for (int i = 0; i < visibleRows.length; i++) {
                    visibleRows[i] = i;
                }
                newRowCount = visibleRows.length;
            }

            this.visibleColumns=visibleColumns;
            this.visibleRows=visibleRows;
            this.newColumnCount=newColumnCount;
            this.newRowCount=newRowCount;
        } finally {
            rebuildInProgess=false;
//            fireTableChanged(null);
        }

        SwingUtilities3.invokeLater(new Runnable() {
            public void run() {
                fireTableStructureChanged();
            }
        });
        if(rebuildOneMoreTime){
            rebuildOneMoreTime=false;
            rebuild0();
        }
    }

    public TableColumnFilter getTableColumnFilter() {
        return tableColumnFilter;
    }

    public void setTableColumnFilter(TableColumnFilter tableColumnFilter) {
        this.tableColumnFilter = tableColumnFilter;
        rebuild();
    }

    public TableRowFilter getTableRowFilter() {
        return tableRowFilter;
    }

    public void setTableRowFilter(TableRowFilter tableRowFilter) {
        this.tableRowFilter = tableRowFilter;
        rebuild();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return baseModel.getColumnClass(visibleColumns[columnIndex]);
    }

    @Override
    public String getColumnName(int column) {
        return baseModel.getColumnName(visibleColumns[column]);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        baseModel.setValueAt(aValue, visibleRows[rowIndex], visibleColumns[columnIndex]);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return baseModel.isCellEditable(visibleRows[rowIndex], visibleColumns[columnIndex]);
    }

    public TableModel getBaseModel() {
        return baseModel;
    }

    public int getBaseRowIndex(int index) {
        return visibleRows[index];
    }

    public int getBaseColumnsIndex(int index) {
        return visibleColumns[index];
    }
}

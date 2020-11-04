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

package net.thevpc.common.swings.table;

import javax.swing.table.TableModel;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 mai 2006 19:07:10
 */
public class TableRowFilterByCell implements TableRowFilter {
    private TableCellFilter cellFilter;

    public TableRowFilterByCell(TableCellFilter cellFilter) {
        this.cellFilter = cellFilter;
    }

    public boolean acceptRow(int rowIndex, TableModel model) {
        int columns = model.getColumnCount();
        boolean accept = false;
        for (int i = 0; i < columns; i++) {
            Object o = model.getValueAt(rowIndex, i);
            if (cellFilter == null || cellFilter.acceptCell(o, rowIndex, i, model)) {
                accept = true;
            }
            if (accept) {
                break;
            }
        }
        return accept;
    }

    public TableCellFilter getCellFilter() {
        return cellFilter;
    }

    public void setCellFilter(TableCellFilter cellFilter) {
        this.cellFilter = cellFilter;
    }
}

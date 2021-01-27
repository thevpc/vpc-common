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

package net.thevpc.common.swing.table;

import javax.swing.table.TableModel;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 6 mai 2006 19:07:10
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

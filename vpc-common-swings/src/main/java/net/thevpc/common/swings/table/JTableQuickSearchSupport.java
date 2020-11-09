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
package net.thevpc.common.swings.table;

import net.thevpc.common.swings.iswing.IJTable;

import javax.swing.*;
import java.util.regex.Pattern;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 mai 2006 18:47:41
 */
public class JTableQuickSearchSupport {
    private boolean cellLookupCaseSensitive;
    private int cellLookupFilter;
    public static final int LOOK_UP_CELL_BY_PREFIX = 0;
    public static final int LOOK_UP_CELL_BY_SUFFIX = 1;
    public static final int LOOK_UP_CELL_BY_PORTION = 2;
    public static final int LOOK_UP_CELL_BY_REGEXP = 3;
    public static int RESIZE_MAX = 1000;
    public static int RESIZE_MIN = 0;

    protected IJTable jTable;
    Pattern regexpPattern;

    public JTableQuickSearchSupport(IJTable jTable) {
        this.jTable = jTable;
    }

    public void setCellLookupStrategy(int strategy) {
        cellLookupFilter = strategy;
    }

    public int getCellLookupStrategy() {
        return cellLookupFilter;
    }

    public boolean isCellLookupCaseSensitive() {
        return cellLookupCaseSensitive;
    }

    public void setCellLookupCaseSensitive(boolean enable) {
        cellLookupCaseSensitive = enable;
    }

    public int setFirstSelectedByFilter(String prefix) {
        //TODO    JTableHelper tableHelper = (JTableHelper) session.getFactory().newInstance(JTableHelper.class);

        return setSelectedByFilter(prefix, jTable.getSelectedRow(), jTable.convertColumnIndexToModel(jTable.getSelectedColumn()));
    }

    public int setNextSelectedByFilter(String prefix) {
        //TODO    JTableHelper tableHelper = (JTableHelper) session.getFactory().newInstance(JTableHelper.class);
        return setSelectedByFilter(prefix, jTable.getSelectedRow() + 1, jTable.convertColumnIndexToModel(jTable.getSelectedColumn()));
    }

    public void setLastLineSelected() {
        try {
            JViewport p = (JViewport) jTable.getComponent().getParent();
            if (p == null) {
                return;
            }
            JScrollPane pp = (JScrollPane) p.getParent();
            if (pp == null) {
                return;
            }
            JScrollBar jsb = pp.getVerticalScrollBar();
            if (jsb == null) {
                return;
            }
            int row = jTable.getModel().getRowCount() - 1;
            if (row >= 0) {
                jTable.setRowSelectionInterval(row, row);
                jsb.setValue(jsb.getMaximum());
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            //Log.error("Parent is not as JScrollPane but a " + jTable.getParent().getClass().getActionName() + " on " + jTable.getParent().getParent().getClass().getActionName());
        }
    }

    public boolean cellValueAccepted(String cellValue, String filter) {
        if (!cellLookupCaseSensitive) {
            cellValue = cellValue.toUpperCase();
            filter = filter.toUpperCase();
        }
        switch (getCellLookupStrategy()) {
            case LOOK_UP_CELL_BY_PREFIX: {
                return cellValue.startsWith(filter);
            }
            case LOOK_UP_CELL_BY_SUFFIX: {
                return cellValue.endsWith(filter);
            }
            case LOOK_UP_CELL_BY_PORTION: {
                return cellValue.indexOf(filter) >= 0;
            }
            case LOOK_UP_CELL_BY_REGEXP: {
                return regexpPattern.matcher(cellValue).matches();
            }
            default:
                throw new RuntimeException("Incorrect LOOK_UP_STRATEGY : " + cellLookupFilter);
        }
        //return true;
    }

    public int setSelectedByFilter(String prefix, int startSearchRow, int searchColumn) {
        if (prefix == null){
            prefix = "";
        }
        regexpPattern=Pattern.compile(prefix,cellLookupCaseSensitive?0:Pattern.CASE_INSENSITIVE);
        int colIndex = searchColumn;
        int rowIndex = startSearchRow;
        if (rowIndex < 0 || rowIndex >= jTable.getModel().getRowCount()) {
            rowIndex = 0;
        }
        if (colIndex < 0 || colIndex >= jTable.getModel().getColumnCount()) {
            colIndex = 0;
        }
        if (rowIndex < 0 || rowIndex >= jTable.getModel().getRowCount()) {
            return -1;
        }
        if (colIndex < 0 || colIndex >= jTable.getModel().getColumnCount()) {
            return -1;
        }
        int row = rowIndex;
        boolean firstLoop = true;
        while (rowIndex < jTable.getModel().getRowCount()) {
            Object oval = jTable.getModel().getValueAt(row, colIndex);
            String val = oval != null ? String.valueOf(oval) : "";
            if (cellValueAccepted(val, prefix)) {
                jTable.setRowSelectionInterval(row, row);
                try {
                    JViewport p = (JViewport) jTable.getComponent().getParent();
                    if (p != null) {
                        JScrollPane pp = (JScrollPane) p.getParent();
                        JScrollBar jsb = pp.getVerticalScrollBar();
                        int _max = jsb.getMaximum();
                        int _min = jsb.getMinimum();
                        jsb.setValue(((_max - _min) * row) / jTable.getModel().getRowCount() + _min);
                    }
                } catch (ClassCastException e) {
//                    e.printStackTrace();
                }
                return row;
            } else {
//                System.out.println("not accepted = '" + val+"' / '"+prefix+"'");
            }
            if (++row < jTable.getModel().getRowCount()) {
                continue;
            }
            if (!firstLoop) {
                break;
            }
            firstLoop = false;
            row = 0;
        }
        return -1;
    }

    public IJTable getTable() {
        return jTable;
    }
}

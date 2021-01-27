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
package net.thevpc.common.swing.iswing;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 *
 * @author thevpc
 */
public interface IJTable extends IJComponent {

    public int convertRowIndexToView(int i);

    public int convertRowIndexToModel(int i);

    public int convertColumnIndexToView(int i);

    public int convertColumnIndexToModel(int i);

    public void setModel(TableModel dataModel);

    public TableModel getModel();

    public Component getComponent();

    /**
     * Sets the table's selection mode to allow only single selections, a single
     * contiguous interval, or multiple intervals.
     * <P>
     * Note:
     * <code>JTable</code> provides all the methods for handling
     * column and row selection.  When setting states,
     * such as <code>setSelectionMode</code>, it not only
     * updates the mode for the row selection model but also sets similar
     * values in the selection model of the <code>columnModel</code>.
     * If you want to have the row and column selection models operating
     * in different modes, set them both directly.
     * <p>
     * Both the row and column selection models for <code>JTable</code>
     * default to using a <code>DefaultListSelectionModel</code>
     * so that <code>JTable</code> works the same way as the
     * <code>JList</code>. See the <code>setSelectionMode</code> method
     * in <code>JList</code> for details about the modes.
     *
     * @see JList#setSelectionMode
     * description: The selection mode used by the row and column selection models.
     *        enum: SINGLE_SELECTION            ListSelectionModel.SINGLE_SELECTION
     *              SINGLE_INTERVAL_SELECTION   ListSelectionModel.SINGLE_INTERVAL_SELECTION
     *              MULTIPLE_INTERVAL_SELECTION ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
     */
    public void setSelectionMode(int selectionMode);

    //public int getSelectionMode();

    /**
     * Sets whether the rows in this model can be selected.
     *
     * @param rowSelectionAllowed   true if this model will allow row selection
     *  bound: true
     *    attribute: visualUpdate true
     *  description: If true, an entire row is selected for each selected cell.
     */
    public void setRowSelectionAllowed(boolean rowSelectionAllowed);

    //public boolean isRowSelectionAllowed();

    /**
     * Returns the <code>ListSelectionModel</code> that is used to maintain row
     * selection state.
     *
     * @return  the object that provides row selection state, <code>null</code>
     *          if row selection is not allowed
     */
    public ListSelectionModel getSelectionModel();

    /**
     * Returns the index of the first selected row, -1 if no row is selected.
     * @return the index of the first selected row
     */
    public int getSelectedRow();

    /**
     * Returns the index of the first selected column,
     * -1 if no column is selected.
     * @return the index of the first selected column
     */
    public int getSelectedColumn();

    /**
     * Sets a default cell renderer to be used if no renderer has been set in
     * a <code>TableColumn</code>. If renderer is <code>null</code>,
     * removes the default renderer for this column class.
     *
     * @param  columnClass     set the default cell renderer for this columnClass
     * @param  renderer        default cell renderer to be used for this
     *			       columnClass
     */
    public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer);
    
    
    /**
     * Selects the rows from <code>index0</code> to <code>index1</code>,
     * inclusive.
     *
     * @exception IllegalArgumentException      if <code>index0</code> or
     *						<code>index1</code> lie outside
     *                                          [0, <code>getRowCount()</code>-1]
     * @param   index0 one end of the interval
     * @param   index1 the other end of the interval
     */
    public void setRowSelectionInterval(int index0, int index1);
    
    
    /**
     * Returns a rectangle for the cell that lies at the intersection of
     * <code>row</code> and <code>column</code>.
     * If <code>includeSpacing</code> is true then the value returned
     * has the full height and width of the row and column
     * specified. If it is false, the returned rectangle is inset by the
     * intercell spacing to return the true bounds of the rendering or
     * editing component as it will be set during rendering.
     * <p>
     * If the column index is valid but the row index is less
     * than zero the method returns a rectangle with the
     * <code>y</code> and <code>height</code> values set appropriately
     * and the <code>x</code> and <code>width</code> values both set
     * to zero. In general, when either the row or column indices indicate a
     * cell outside the appropriate range, the method returns a rectangle
     * depicting the closest edge of the closest cell that is within
     * the table's range. When both row and column indices are out
     * of range the returned rectangle covers the closest
     * point of the closest cell.
     * <p>
     * In all cases, calculations that use this method to calculate
     * results along one axis will not fail because of anomalies in
     * calculations along the other axis. When the cell is not valid
     * the <code>includeSpacing</code> parameter is ignored.
     *
     * @param   row                   the row index where the desired cell
     *                                is located
     * @param   column                the column index where the desired cell
     *                                is located in the display; this is not
     *                                necessarily the same as the column index
     *                                in the data model for the table; the
     *                                {@link #convertColumnIndexToView(int)}
     *                                method may be used to convert a data
     *                                model column index to a display
     *                                column index
     * @param   includeSpacing        if false, return the true cell bounds -
     *                                computed by subtracting the intercell
     *				      spacing from the height and widths of
     *				      the column and row models
     *
     * @return  the rectangle containing the cell at location
     *          <code>row</code>,<code>column</code>
     */
    public Rectangle getCellRect(int row, int column, boolean includeSpacing);
    
    
    
    /**
     * Returns the index of the column that <code>point</code> lies in,
     * or -1 if the result is not in the range
     * [0, <code>getColumnCount()</code>-1].
     *
     * @param   point   the location of interest
     * @return  the index of the column that <code>point</code> lies in,
     *		or -1 if the result is not in the range
     *		[0, <code>getColumnCount()</code>-1]
     * @see     #rowAtPoint
     */
    public int columnAtPoint(Point point);
    
    /**
     * Returns the index of the row that <code>point</code> lies in,
     * or -1 if the result is not in the range
     * [0, <code>getRowCount()</code>-1].
     *
     * @param   point   the location of interest
     * @return  the index of the row that <code>point</code> lies in,
     *          or -1 if the result is not in the range
     *          [0, <code>getRowCount()</code>-1]
     * @see     #columnAtPoint
     */
    public int rowAtPoint(Point point);
    

    /**
     * Registers the text to display in a tool tip.
     * The text displays when the cursor lingers over the component.
     * <p>
     * See <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/tooltip.html">How to Use Tool Tips</a>
     * in <em>The Java Tutorial</em>
     * for further documentation.
     *
     * @param text  the string to display; if the text is <code>null</code>,
     *              the tool tip is turned off for this component
     *   preferred: true
     * description: The text to display in a tool tip.
     */
    public void setToolTipText(String text);
    
    /**
     * Returns the <code>TableColumnModel</code> that contains all column information
     * of this table.
     *
     * @return  the object that provides the column state of the table
     */
    public TableColumnModel getColumnModel();
    
    /**
     * Returns the indices of all selected rows.
     *
     * @return an array of integers containing the indices of all selected rows,
     *         or an empty array if no row is selected
     * @see #getSelectedRow
     */
    public int[] getSelectedRows();
}

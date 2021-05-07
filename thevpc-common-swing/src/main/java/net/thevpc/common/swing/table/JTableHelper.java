package net.thevpc.common.swing.table;

import javax.swing.*;
import net.thevpc.common.swing.SwingUtilities3;

public class JTableHelper {

    private JTable table;
    private JScrollPane pane;

    public JTable getTable() {
        return table;
    }

    public JScrollPane getPane() {
        return pane;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public void setPane(JScrollPane pane) {
        this.pane = pane;
    }

    public void addTableClickListener(JTableClickListener listener) {
        SwingUtilities3.addTableClickListener(table, listener);
    }

    public void setTableColumnWidthPercent(double... weigths) {
        SwingUtilities3.setTableColumnWidthPercent(table, weigths);
    }

}

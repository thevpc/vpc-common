package net.thevpc.common.swing;

import javax.swing.*;

public class JTableHelper {
    JTable table;
    JScrollPane pane;

    public JTable getTable() {
        return table;
    }

    public JScrollPane getPane() {
        return pane;
    }

    public void addTableClickListener(JTableClickListener listener) {
        SwingUtilities3.addTableClickListener(table,listener);
    }

    public void setTableColumnWidthPercent(double... weigths) {
        SwingUtilities3.setTableColumnWidthPercent(table,weigths);
    }

}

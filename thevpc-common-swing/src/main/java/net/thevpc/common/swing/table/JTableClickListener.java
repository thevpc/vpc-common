package net.thevpc.common.swing.table;

import javax.swing.*;
import java.awt.event.MouseEvent;

public interface JTableClickListener {
    void onMousePressed(int row, int col, JTable tab, MouseEvent mouseEvent);
}

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
package net.vpc.common.swings;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 oct. 2006 21:45:48
 */
public class SimpleRowHeaderRenderer extends JLabel implements ListCellRenderer {
    private JTable table;

    public SimpleRowHeaderRenderer(JTable table) {
        this.table = table;
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        JTableHeader header = table.getTableHeader();
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
        setMinimumSize(new Dimension(30, 5));
        setFocusable(false);
    }

    public Component getListCellRendererComponent(JList list,
                                                  Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(String.valueOf(index + 1));
        JTableHeader header = table.getTableHeader();
        if (isSelected) {
            setForeground(header.getBackground());
            setBackground(header.getForeground());
        } else {
            setForeground(header.getForeground());
            setBackground(header.getBackground());
        }
        return this;
    }

    /**
     * should be called in JTable.configureEnclosingScrollPane() 
     */
    public void install(){
        Container p = table.getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                JViewport viewport = scrollPane.getViewport();
                if (viewport == null || viewport.getView() != table) {
                    return;
                }
                //                scrollPane.setColumnHeaderView(getTableHeader());
                JList list = (JList) getClientProperty("JList");
                if (list != null) {
                    //already configured
                    return;
                }
                list = new JList(new AbstractListModel() {
                    public int getSize() {
                        return table.getRowCount();
                    }

                    public Object getElementAt(int index) {
                        return (index + 1);
                    }
                });
                putClientProperty("JList", list);
                list.setFixedCellWidth(table.getRowCount() == 0 ? 5 : -1);
                //list.setFixedCellWidth(50);
                list.setMinimumSize(new Dimension(30, 5));
                list.setFixedCellHeight(table.getRowHeight());
                list.setEnabled(false);
                list.setCellRenderer(this);
                list.setBackground(table.getTableHeader().getBackground());
                scrollPane.setRowHeaderView(list);
            }
        }
    }
}

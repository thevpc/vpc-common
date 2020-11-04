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
package net.thevpc.common.swings;

import net.thevpc.common.swings.iswing.IJTable;
import net.thevpc.common.swings.table.JTableImpl;
import net.thevpc.common.swings.util.SwingPrivateIOUtils;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;

public class SystemPropertiesPanel extends JPanel {

    public SystemPropertiesPanel() {
        super(new BorderLayout());
    }

    protected void init() {
        final IJTable table = createTable();
        table.setModel(new SystemPropertiesPanel.SystemPropertiesModel());
        JScrollPane jsp = new JScrollPane(table.getComponent());
        jsp.setPreferredSize(new Dimension(600, 300));


        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            Color initialColor;

            public Component getTableCellRendererComponent(JTable jtable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (initialColor == null) {
                    initialColor = getForeground();
                }
                super.getTableCellRendererComponent(jtable, value, isSelected, hasFocus, row, column);
                SystemPropertiesPanel.SystemPropertiesModel pm = (SystemPropertiesPanel.SystemPropertiesModel) jtable.getModel();
                row = table.convertRowIndexToModel(row);
                String[] keyValue = pm.keys[row];
                if (keyValue[0].startsWith("java.")) {
                    setForeground(Color.GREEN.darker().darker());
                } else if (keyValue[0].startsWith("user.")) {
                    setForeground(Color.BLUE.darker());
                } else if (
                        keyValue[0].startsWith("os.")
                                || keyValue[0].startsWith("awt.")
                                || keyValue[0].startsWith("java.awt.")
                                || keyValue[0].startsWith("line.")
                                || keyValue[0].startsWith("file.")
                                || keyValue[0].startsWith("path.")
                                || keyValue[0].startsWith("sun.")
                        ) {
                    setForeground(Color.RED.darker());
                } else {
                    setForeground(initialColor);
                }
                return this;
            }
        });
        JTabbedPane jtp = new JTabbedPane();
        jtp.addTab("System", createGeneralComponent());
        jtp.addTab("Advanced", jsp);
        jtp.addTab("ClassPath", new JScrollPane(getSystemPropertyTable("java.class.path").getComponent()));
        add(jtp, BorderLayout.CENTER);
        table.setRowSelectionInterval(0, 0);
        setPreferredSize(new Dimension(700, 400));
    }

    private JComponent createGeneralComponent() {
        String value = null;
        try {
            value = SwingPrivateIOUtils.loadStreamAsString(SystemPropertiesPanel.class.getResource("SystemPropertiesPanelSummary.html"));
            for (Map.Entry<Object, Object> objectObjectEntry : System.getProperties().entrySet()) {
                String k = (String) objectObjectEntry.getKey();
                String v = (String) objectObjectEntry.getValue();
                value = value.replace("${" + k + "}", v);
            }
        } catch (IOException e) {
            e.printStackTrace();
            value = "<html>"
                    + "<table>Operating System</b> : &nbsp; &nbsp;" + System.getProperty("os.name") + " " + System.getProperty("os.version") + " / " + System.getProperty("os.arch") + "<br>"
                    + "<b>Operating System</b> : &nbsp; &nbsp;" + System.getProperty("os.name") + " " + System.getProperty("os.version") + " / " + System.getProperty("os.arch") + "<br>"
                    + "<b>Java Virtual Machine</b> : &nbsp; &nbsp;" + System.getProperty("java.version") + " " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.vendor") + "<br>"
                    + "<b>User</b> : &nbsp; &nbsp;" + System.getProperty("user.name") + "<br>"
                    + "<b>Locale</b> : &nbsp; &nbsp;" + System.getProperty("user.country") + " " + System.getProperty("user.language") + " " + System.getProperty("user.timezone") + "<br>"
                    + "</html>";
        }
        JLabel b = new JLabel(value);
        JPanel pp = new JPanel(new BorderLayout());
        pp.add(b);
        return pp;
    }

    private IJTable getSystemPropertyTable(String var) {
        String jcp = System.getProperty(var);
        String[] jcps = (jcp == null ? "" : jcp).split(System.getProperty("path.separator"));
        IJTable table = createTable();
        Object[][] mod = new Object[jcps.length][1];
        for (int i = 0; i < mod.length; i++) {
            mod[i][0] = jcps[i];
        }
        table.setModel(new DefaultTableModel(mod, new Object[]{"paths"}));
        return table;
    }

    public class SystemPropertiesModel extends AbstractTableModel {
        String[][] keys;

        public SystemPropertiesModel() {
            int i = 0;
            Properties properties = System.getProperties();
            keys = new String[properties.size()][2];
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                keys[i][0] = (String) entry.getKey();
                keys[i][1] = (String) entry.getValue();
                keys[i][1] = keys[i][1].replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
                i++;
            }
            Arrays.sort(keys, new Comparator<String[]>() {
                public int compare(String[] o1, String[] o2) {
                    return o1[0].compareTo(o2[0]);
                }
            });
        }

        public int getRowCount() {
            return keys == null ? 0 : keys.length;
        }

        public int getColumnCount() {
            return 2;
        }

        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case 0: {
                    return "Name";
                }
                case 1: {
                    return "Value";
                }
            }
            return super.getColumnName(columnIndex);
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return keys[rowIndex][columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
    }

    protected IJTable createTable() {
        return new JTableImpl();
    }
}

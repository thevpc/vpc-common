/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.list;

import net.thevpc.common.swing.combo.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.function.Function;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.thevpc.common.swing.NamedValue;
//import net.thevpc.echo.Application;

/**
 *
 * @author vpc
 */
public class JListHelper {

    public static JList createList(Function<String, Icon> functionResolver, NamedValue... values) {
        JList c = new JList();
        prepareList(c, functionResolver, values);
        return c;
    }

    public static JList prepareList(JList c, Function<String, Icon> functionResolver, NamedValue... values) {
        c.setModel(new ExtendedComboBoxModel(values));
        c.getSelectionModel().addListSelectionListener(new ListSelectionListenerImpl(c)
        );
        c.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    setIcon(null);
                } else {
                    NamedValue nv = (NamedValue) value;
                    if (nv.isGroup()) {
                        JLabel label = new JLabel(nv.getName());
                        Font f = label.getFont();
                        Color fc = label.getForeground();
                        Color bc = label.getBackground();
                        label.setOpaque(true);
                        label.setBackground(fc);
                        label.setForeground(bc);
                        label.setFont(f.deriveFont(f.getStyle() | Font.BOLD | Font.ITALIC));
                        return label;
                    } else {
                        super.getListCellRendererComponent(list, nv.getName(), index, isSelected, cellHasFocus);
                        if (value instanceof NamedValue) {
                            String icon = ((NamedValue) value).getIcon();
                            Icon iconObj = null;
                            if (functionResolver != null) {
                                iconObj = functionResolver.apply(icon);
                            }
                            setIcon(iconObj);
                        } else {
                            setIcon(null);
                        }
                    }
                }
                return this;
            }
        });
        return c;
    }

    private static class ListSelectionListenerImpl implements ListSelectionListener {

        private final JList c;
        private int lastIndex = -1;

        public ListSelectionListenerImpl(JList c) {
            this.c = c;
        }

        private int next(int i) {
            int bestIndex = -1;
            for (int j = i + 1; j < c.getModel().getSize(); j++) {
                NamedValue o2 = (NamedValue) c.getModel().getElementAt(j);
                if (!o2.isGroup()) {
                    bestIndex = j;
                    break;
                }
            }
            return bestIndex;
        }

        private int previous(int i) {
            int bestIndex = -1;
            for (int j = i - 1; j >= 0; j--) {
                NamedValue o2 = (NamedValue) c.getModel().getElementAt(j);
                if (!o2.isGroup()) {
                    bestIndex = j;
                    break;
                }
            }
            return bestIndex;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int i = c.getSelectedIndex();
            if (i >= 0) {
                NamedValue o = (NamedValue) c.getSelectedValue();
                if (o.isGroup()) {
                    int bestIndex = -1;
                    if (lastIndex <= i) {
                        bestIndex = next(i);
                        if (bestIndex < 0) {
                            bestIndex = previous(i);
                        }
                    } else {
                        bestIndex = previous(i);
                        if (bestIndex < 0) {
                            bestIndex = next(i);
                        }
                    }
                    if (bestIndex < 0) {
                        c.setSelectedIndex(-1);
                    } else {
                        c.setSelectedIndex(bestIndex);
                    }
                }
            }
            lastIndex = i;
        }
    }

}

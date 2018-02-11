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
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 aout 2006 16:41:17
 */
public class TabbedPanel extends JPanel {
    JPanel sub;
    JToolBar bar;
    ButtonGroup buttons;
    private ItemListener listener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            JToggleButton b = ((JToggleButton) e.getSource());
            if (b.isSelected()) {
                String name = (String) b.getClientProperty("Page");
                showTab(name);
            }
        }
    };

    public TabbedPanel() {
        super(new BorderLayout());
        sub = new JPanel(new CardLayout());
        bar = new JToolBar();
        buttons=new ButtonGroup();
        bar.setFloatable(false);
        add(sub, BorderLayout.CENTER);
        add(bar, BorderLayout.PAGE_END);
    }

    public void showTab(String page) {
        JToggleButton b = getButton(page);
        if (b.isSelected()) {
            b.setSelected(true);
        }
        CardLayout manager = (CardLayout) sub.getLayout();
        manager.show(sub, page);
    }

    private JToggleButton getButton(String page) {
        return (JToggleButton) getClientProperty("Button." + page);
    }

    public void addTab(String name, Component comp) {
        JToggleButton b = PRSManager.createToggleButton(name);
        putClientProperty("Button." + name, b);
        b.putClientProperty("Page", name);
        b.addItemListener(listener);
        buttons.add(b);
        bar.add(b);
        sub.add(comp, name);
    }

}

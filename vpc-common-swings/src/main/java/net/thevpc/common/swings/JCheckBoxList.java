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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.swing.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 mai 2007 22:26:14
 */
public class JCheckBoxList extends JPanel {
    private Box verticalBox;
    private LinkedHashMap<String,JCheckBox> checkBoxes =new LinkedHashMap<String, JCheckBox>();
    private Vector<JCheckBox> list=new Vector<JCheckBox>();

    public JCheckBoxList() {
        verticalBox = Box.createVerticalBox();

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton all = new JButton("select All");
        all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : checkBoxes.values()) {
                    jCheckBox.setSelected(true);
                }
            }
        });
        JButton none = new JButton("select None");
        none.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : checkBoxes.values()) {
                    jCheckBox.setSelected(false);
                }
            }
        });
        toolBar.add(all);
        toolBar.add(none);
        JScrollPane jsp = new JScrollPane(verticalBox);
        jsp.setPreferredSize(new Dimension(400,400));
        this.setLayout(new BorderLayout());
        this.add(toolBar, BorderLayout.PAGE_START);
        this.add(jsp, BorderLayout.CENTER);

    }

    public JCheckBox addItem(String name){
        return addItem(name,name);
    }

    public JCheckBox addItem(String name,String title){
        JCheckBox c=new JCheckBox();
        c.setName(name);
        c.setText(title);
        addItem(c);
        return c;
    }

    public JCheckBox getItem(int index){
        return list.get(index);
    }

    public void addItem(JCheckBox box){
        checkBoxes.put(box.getName(),box);
        verticalBox.add(toRowComponent(box));
        list.add(box);
    }
    
    protected Component toRowComponent(JCheckBox box){
        return box;
    } 
}

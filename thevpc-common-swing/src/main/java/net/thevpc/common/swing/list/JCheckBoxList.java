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
package net.thevpc.common.swing.list;

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
 * %creationtime 10 mai 2007 22:26:14
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

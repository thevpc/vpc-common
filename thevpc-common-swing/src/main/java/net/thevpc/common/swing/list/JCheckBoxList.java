/**
 * ====================================================================
 * vpc-swingext library
 * <p>
 * Description: <start><end>
 *
 * <br>
 * <p>
 * Copyright [2020] [thevpc] Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br> ====================================================================
 */
package net.thevpc.common.swing.list;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com) %creationtime 10 mai 2007
 * 22:26:14
 */
public class JCheckBoxList extends JPanel {

    private Box verticalBox;
    private List<JCheckBox> list = new Vector<JCheckBox>();

    public JCheckBoxList() {
        verticalBox = Box.createVerticalBox();

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton all = new JButton("select All");
        all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : list) {
                    jCheckBox.setSelected(true);
                }
            }
        });
        JButton none = new JButton("select None");
        none.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : list) {
                    jCheckBox.setSelected(false);
                }
            }
        });
        toolBar.add(all);
        toolBar.add(none);
        JScrollPane jsp = new JScrollPane(verticalBox);
        jsp.setPreferredSize(new Dimension(400, 400));
        this.setLayout(new BorderLayout());
        this.add(toolBar, BorderLayout.PAGE_START);
        this.add(jsp, BorderLayout.CENTER);

    }

    //    public JCheckBox addItem(String name){
//        return addItem(name,name);
//    }
//
//    public JCheckBox addItem(String name,String title){
//        JCheckBox c=new JCheckBox();
//        c.setName(name);
//        c.setText(title);
//        addItem(c);
//        return c;
//    }
    public JCheckBox addItem(Object value, String name) {
        return addItem(value, name, name);
    }

    public JCheckBox addItem(Object value, String name, String title) {
        JCheckBox c = new JCheckBox();
        c.putClientProperty("value", value);
        c.setName(name);
        c.setText(name);
        c.setToolTipText(title);
        addItem(c);
        return c;
    }

    public boolean isItemSelected(int index) {
        return getItemCheckbox(index).isSelected();
    }

    public Object getSelectedElementAt(int index) {
        JCheckBox z = getItemCheckbox(index);
        if (z.isSelected()) {
            return z.getClientProperty("value");
        }
        return null;
    }

    public List<Object> getSelectedElements() {
        return list.stream().filter(x -> x.isSelected()).map(x->x.getClientProperty("value")).collect(Collectors.toList());
    }

    public Object getElementAt(int index) {
        return getItemCheckbox(index).getClientProperty("value");
    }

    public JCheckBox getItemCheckbox(int index) {
        return list.get(index);
    }

    public void addItem(JCheckBox box) {
        verticalBox.add(toRowComponent(box));
        list.add(box);
    }

    protected Component toRowComponent(JCheckBox box) {
        return box;
    }

    public void resetModel() {
        verticalBox.removeAll();
        list.clear();
    }

    public boolean isAllSelected() {
        return list.stream().allMatch(x -> x.isSelected());
    }

    public boolean isNoneSelected() {
        return list.stream().allMatch(x -> !x.isSelected());
    }

    public boolean isOneSelected() {
        return getSelectedCount() == 0;
    }

    public int getSelectedCount() {
        return (int) list.stream().filter(x -> x.isSelected()).count();
    }

    public int getElementCount() {
        return list.size();
    }
}

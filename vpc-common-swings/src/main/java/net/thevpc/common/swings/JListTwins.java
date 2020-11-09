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
package net.thevpc.common.swings;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 août 2007 20:39:47
 */
public class JListTwins extends JPanel {
//    public static void main(String[] args) {
//        JListTwins ll = new JListTwins();
//        ll.setModel(new Object[]{"a", "b", "c", "d"}, new int[]{2, 3});
//        JOptionPane.showMessageDialog(null, ll);
//    }

    private ListSelectionListener listSelectionListener;
    private ListDataListener listDataListener;
    private JListTwinsModel model;
    private JList leftList = new JList();
    private JList rightList = new JList();
    private JTextField leftFilter = new JTextField();
    private JTextField rightFilter = new JTextField();
    private JButton addButton = new JButton("->");
    private JButton removeButton = new JButton("<-");
    private JButton addAllButton = new JButton("*->");
    private JButton removeAllButton = new JButton("<-*");
    private boolean leftFilterEnabled;
    private boolean rightFilterEnabled;

    public JListTwins() {
        super(
                new DumbGridBagLayout()
                        .addLine("[^<-leftFilter][nothing][^<-rightFilter]")
                        .addLine("[^<$-=leftList  ][$toolbar][^<$-=rightList]")
        );
        JPanel toolbar = new JPanel(
                new DumbGridBagLayout()
                .addLine("[+a]")
                .addLine("[+r]")
                .addLine("[+A]")
                .addLine("[+R]")
                .setInsets(".*",new Insets(3,6,3,6))
        );
        toolbar.add(addButton,"a");
        toolbar.add(removeButton,"r");
        toolbar.add(addAllButton,"A");
        toolbar.add(removeAllButton,"R");
        this.add(leftFilter, "leftFilter");
        this.add(rightFilter, "rightFilter");
        leftFilter.setVisible(leftFilterEnabled);
        rightFilter.setVisible(rightFilterEnabled);
        this.add(new JScrollPane(leftList), "leftList");
        this.add(new JScrollPane(rightList), "rightList");
        this.add(toolbar, "toolbar");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] indices = leftList.getSelectedIndices();
                int[] rindices = new int[indices.length];
                for (int i = 0; i < indices.length; i++) {
                    int index = indices[i];
                    model.moveRight(index);
                    rindices[i] = rightList.getModel().getSize() - 1;
                    if (index < leftList.getModel().getSize()) {
                        leftList.setSelectedIndex(index);
                    } else if (index > 0) {
                        leftList.setSelectedIndex(index - 1);
                    }
                }
                rightList.setSelectedIndices(rindices);
            }
        });
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] indices = rightList.getSelectedIndices();
                int[] lindices = new int[indices.length];
                for (int i = 0; i < indices.length; i++) {
                    int index = indices[i];
                    model.moveLeft(index);
                    lindices[i] = leftList.getModel().getSize() - 1;
                    if (index < rightList.getModel().getSize()) {
                        rightList.setSelectedIndex(index);
                    } else if (index > 0) {
                        rightList.setSelectedIndex(index - 1);
                    }
                }
                leftList.setSelectedIndices(lindices);
            }
        });
        addAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int lcount=leftList.getModel().getSize();
                model.moveAllRight();
                int rcount=rightList.getModel().getSize();
                int[] rindices = new int[lcount];
                for (int i = 0; i < rindices.length; i++) {
                    rindices[i]=rcount-lcount+i;
                }
                rightList.setSelectedIndices(rindices);
            }
        });
        removeAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rcount=rightList.getModel().getSize();
                model.moveAllLeft();
                int lcount=leftList.getModel().getSize();
                int[] lindices = new int[rcount];
                for (int i = 0; i < lindices.length; i++) {
                    lindices[i]=lcount-rcount+i;
                }
                leftList.setSelectedIndices(lindices);
            }
        });
        listDataListener = new ListDataListener() {
            public void intervalAdded(ListDataEvent e) {
                revalideEnabledButtons();
            }

            public void intervalRemoved(ListDataEvent e) {
                revalideEnabledButtons();
            }

            public void contentsChanged(ListDataEvent e) {
                revalideEnabledButtons();
            }
        };
        listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                revalideEnabledButtons();
            }
        };
        leftList.getModel().addListDataListener(listDataListener);
        leftList.getSelectionModel().addListSelectionListener(listSelectionListener);
        rightList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                revalideEnabledButtons();
            }
        });
        revalideEnabledButtons();
    }

    private void revalideEnabledButtons() {
        addButton.setEnabled(leftList.getSelectedIndex() >= 0);
        removeButton.setEnabled(rightList.getSelectedIndex() >= 0);
        addAllButton.setEnabled(leftList.getModel().getSize() >= 0);
        removeAllButton.setEnabled(rightList.getModel().getSize() >= 0);
    }

    public void setModel(JListTwinsModel _model) {
        JListTwinsModel old = this.model;
        this.model = _model;
        leftList.getModel().removeListDataListener(listDataListener);
        rightList.getModel().removeListDataListener(listDataListener);
        leftList.setModel(new ListModel() {
            public int getSize() {
                return model.getLeftSize();
            }

            public Object getElementAt(int index) {
                return model.getLeftElementAt(index);
            }

            public void addListDataListener(ListDataListener l) {
                model.addLeftListDataListener(l);
            }

            public void removeListDataListener(ListDataListener l) {
                model.removeLeftListDataListener(l);
            }
        });
        rightList.setModel(new ListModel() {
            public int getSize() {
                return model.getRightSize();
            }

            public Object getElementAt(int index) {
                return model.getRightElementAt(index);
            }

            public void addListDataListener(ListDataListener l) {
                model.addRightListDataListener(l);
            }

            public void removeListDataListener(ListDataListener l) {
                model.removeRightListDataListener(l);
            }
        });
        firePropertyChange("model", old, _model);
    }

    public void setModel(Object[] values, int[] selected) {
        setModel(new DefaultListTwinsModel(values, selected));
    }

    public boolean isLeftFilterEnabled() {
        return leftFilterEnabled;
    }

    public void setLeftFilterEnabled(boolean leftFilterEnabled) {
        this.leftFilterEnabled = leftFilterEnabled;
        leftFilter.setVisible(leftFilterEnabled);
    }

    public boolean isRightFilterEnabled() {
        return rightFilterEnabled;
    }

    public void setRightFilterEnabled(boolean rightFilterEnabled) {
        this.rightFilterEnabled = rightFilterEnabled;
        rightFilter.setVisible(rightFilterEnabled);
    }

    public JListTwinsModel getModel() {
        return model;
    }
}

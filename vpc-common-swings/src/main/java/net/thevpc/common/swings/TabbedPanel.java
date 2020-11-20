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
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 3 aout 2006 16:41:17
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
        JToggleButton b = new JToggleButton(name);
        b.setName(name);
        final SwingComponentConfigurer c = SwingComponentConfigurerFactory.getInstance().get(JToggleButton.class);
        if(c!=null){
            c.onCreateComponent(b);
        }
        //PRSManager.createToggleButton(name);
        putClientProperty("Button." + name, b);
        b.putClientProperty("Page", name);
        b.addItemListener(listener);
        buttons.add(b);
        bar.add(b);
        sub.add(comp, name);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.prs;

import javax.swing.Action;
import javax.swing.JToggleButton;

import net.thevpc.common.swing.DefaultAction;
import net.thevpc.common.swing.SwingComponentConfigurer;
import net.thevpc.common.swing.SwingComponentConfigurerFactory;
import net.thevpc.common.swing.TextManipSupport;
import net.thevpc.common.swing.table.JQuickSearchTextFieldSwingComponentConfigurer;

/**
 *
 * @author thevpc
 */
public class PRSManagerInstaller {

    static {
        SwingComponentConfigurerFactory.getInstance().register(DefaultAction.class, new SwingComponentConfigurer() {
            @Override
            public void onCreateComponent(Object instance) {
                final DefaultAction i = (DefaultAction) instance;
                PRSManager.addSupport(i, (String) i.getValue(Action.NAME));
            }
        });

        SwingComponentConfigurerFactory.getInstance().register(DefaultAction.class, new JQuickSearchTextFieldSwingComponentConfigurer());

        SwingComponentConfigurerFactory.getInstance().register(JToggleButton.class, new SwingComponentConfigurer() {
            @Override
            public void onCreateComponent(Object instance) {
                final JToggleButton i = (JToggleButton) instance;
                PRSManager.addSupport(i, i.getName());
            }
        });
        SwingComponentConfigurerFactory.getInstance().register(TextManipSupport.TextAction.class, new SwingComponentConfigurer() {
            @Override
            public void onCreateComponent(Object instance) {
                final TextManipSupport.TextAction i = (TextManipSupport.TextAction) instance;
                PRSManager.addSupport(i, (String) i.getValue(Action.NAME));
            }
        });

    }
}

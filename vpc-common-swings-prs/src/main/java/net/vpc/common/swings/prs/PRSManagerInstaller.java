/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.swings.prs;

import javax.swing.Action;
import javax.swing.JToggleButton;
import net.vpc.common.swings.DefaultAction;
import net.vpc.common.swings.SwingComponentConfigurer;
import net.vpc.common.swings.SwingComponentConfigurerFactory;
import net.vpc.common.swings.TextManipSupport;
import net.vpc.common.swings.table.JQuickSearchTextFieldSwingComponentConfigurer;

/**
 *
 * @author vpc
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

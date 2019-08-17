/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.swings.table;

import javax.swing.JComponent;
import net.vpc.common.prs.messageset.MessageSet;
import net.vpc.common.swings.prs.PRSManager;
import net.vpc.common.swings.SwingComponentConfigurer;
import net.vpc.common.swings.messageset.ComponentMessageSetUpdater;

/**
 *
 * @author vpc
 */
public class JQuickSearchTextFieldSwingComponentConfigurer implements SwingComponentConfigurer {

    @Override
    public void onCreateComponent(Object instance) {
        final JQuickSearchTextField i = (JQuickSearchTextField) instance;
        PRSManager.addMessageSetSupport(i, "FilterTextField", new ComponentMessageSetUpdater() {
            public void updateMessageSet(JComponent comp, String id, MessageSet messageSet) {
                i.configButton.setToolTipText(messageSet.get("FilterTextField.ConfigButton.toolTipText"));
                i.config_caseSensitive.setText(messageSet.get("FilterTextField.ConfigButton.CaseSensitive"));
                i.config_startsWith.setText(messageSet.get("FilterTextField.ConfigButton.StartWith"));
                i.config_contains.setText(messageSet.get("FilterTextField.ConfigButton.Contains"));
                i.config_endsWith.setText(messageSet.get("FilterTextField.ConfigButton.EndWith"));
                i.config_regexp.setText(messageSet.get("FilterTextField.ConfigButton.RegExp"));
                i.config_shell.setText(messageSet.get("FilterTextField.ConfigButton.Shell"));
                i.filterTextField.setToolTipText(messageSet.get("FilterTextField.FilterText.toolTipText"));
                i.goFilter.setText(messageSet.get("FilterTextField.ConfigButton.ApplyFilter"));
            }

            public void install(JComponent comp, String id) {
            }
        });

        PRSManager.addSupport(i.goFilter, "ApplyFilter");
    }
}

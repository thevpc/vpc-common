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
package net.vpc.common.swings.table;

import net.vpc.common.swings.PRSManager;
import net.vpc.common.swings.messageset.ComponentMessageSetUpdater;
import net.vpc.common.prs.messageset.MessageSet;
import net.vpc.common.swings.JDropDownButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.vpc.common.swings.util.StringFilter;
import net.vpc.common.swings.util.StringPortionFilter;
import net.vpc.common.swings.util.StringRegexpFilter;
import net.vpc.common.swings.util.StringShellFilter;
import net.vpc.common.swings.util.StringStartsWithFilter;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 mai 2006 18:47:41
 */
public class JQuickSearchTextField extends JPanel {
    public static enum Strategy {
        STARTS_WITH,
        ENDS_WITH,
        CONTAINS,
        REGXP,
        SHELL,
    }

    private JCheckBoxMenuItem config_caseSensitive;
    private JRadioButtonMenuItem config_regexp;
    private JRadioButtonMenuItem config_shell;
    private JRadioButtonMenuItem config_startsWith;
    private JRadioButtonMenuItem config_contains;
    private JRadioButtonMenuItem config_endsWith;
    private JDropDownButton configButton;
    private JTextField filterTextField;
    private JMenuItem goFilter;

    public JQuickSearchTextField() {
        PRSManager.addMessageSetSupport(this, "FilterTextField", new ComponentMessageSetUpdater() {
            public void updateMessageSet(JComponent comp, String id, MessageSet messageSet) {
                configButton.setToolTipText(messageSet.get("FilterTextField.ConfigButton.toolTipText"));
                config_caseSensitive.setText(messageSet.get("FilterTextField.ConfigButton.CaseSensitive"));
                config_startsWith.setText(messageSet.get("FilterTextField.ConfigButton.StartWith"));
                config_contains.setText(messageSet.get("FilterTextField.ConfigButton.Contains"));
                config_endsWith.setText(messageSet.get("FilterTextField.ConfigButton.EndWith"));
                config_regexp.setText(messageSet.get("FilterTextField.ConfigButton.RegExp"));
                config_shell.setText(messageSet.get("FilterTextField.ConfigButton.Shell"));
                filterTextField.setToolTipText(messageSet.get("FilterTextField.FilterText.toolTipText"));
                goFilter.setText(messageSet.get("FilterTextField.ConfigButton.ApplyFilter"));
            }

            public void install(JComponent comp, String id) {
            }
        });
        goFilter = PRSManager.createMenuItem("ApplyFilter");
        configButton = new JDropDownButton(" ");
        configButton.setQuickActionDelay(0);
        configButton.setPopupOrientation(SwingConstants.RIGHT);
        configButton.setPopupOrientation(SwingConstants.RIGHT);
        configButton.setMargin(new Insets(1, 1, 1, 1));
        configButton.add(goFilter);
        configButton.addSeparator();
        configButton.add(config_caseSensitive = new JCheckBoxMenuItem());
        config_caseSensitive.setSelected(false);
        configButton.addSeparator();
        ButtonGroup bg = new ButtonGroup();
        configButton.add(config_startsWith = new JRadioButtonMenuItem());
        configButton.add(config_contains = new JRadioButtonMenuItem());
        configButton.add(config_endsWith = new JRadioButtonMenuItem());
        configButton.add(config_shell = new JRadioButtonMenuItem());
        configButton.add(config_regexp = new JRadioButtonMenuItem());
        bg.add(config_startsWith);
        bg.add(config_contains);
        bg.add(config_endsWith);
        bg.add(config_regexp);
        bg.add(config_shell);
        config_startsWith.setSelected(true);
        filterTextField = new JTextField("");
        filterTextField.setColumns(20);
        config_caseSensitive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firePropertyChange("CaseSensitive", null, isCaseSensitive());
                saveConfigCaseSensitive();
            }

        });
        ActionListener lookupStrategy = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                firePropertyChange("Strategy", null, getStrategy());
            }
        };
        config_startsWith.addActionListener(lookupStrategy);
        config_endsWith.addActionListener(lookupStrategy);
        config_contains.addActionListener(lookupStrategy);
        config_regexp.addActionListener(lookupStrategy);
        config_shell.addActionListener(lookupStrategy);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(filterTextField);
        add(configButton);
//        PRSManager.update(this,table.getSession().getViewManager());
        setBorder(BorderFactory.createEtchedBorder());
    }

    public StringFilter getStringFilter() {
        StringFilter sf = null;
        switch (this.getStrategy()) {
            case CONTAINS: {
                sf = new StringPortionFilter(this.getPattern(), this.isCaseSensitive());
                break;
            }
            case STARTS_WITH: {
                sf = new StringStartsWithFilter(this.getPattern(), this.isCaseSensitive());
                break;
            }
            case ENDS_WITH: {
                sf = new StringStartsWithFilter(this.getPattern(), this.isCaseSensitive());
                break;
            }
            case SHELL: {
                sf = new StringShellFilter(this.getPattern(), this.isCaseSensitive());
                break;
            }
            case REGXP: {
                sf = new StringRegexpFilter(this.getPattern(), this.isCaseSensitive());
                break;
            }
        }
        return sf;
    }

    public String getPattern() {
        return filterTextField.getText();
    }

    public JTextField getFilterTextField() {
        return filterTextField;
    }

    public void setStrategy(Strategy strategy) {
        switch (strategy) {
            case CONTAINS: {
                config_contains.setSelected(true);
                break;
            }
            case STARTS_WITH: {
                config_startsWith.setSelected(true);
                break;
            }
            case ENDS_WITH: {
                config_endsWith.setSelected(true);
                break;
            }
            case REGXP: {
                config_regexp.setSelected(true);
                break;
            }
            case SHELL: {
                config_regexp.setSelected(true);
                break;
            }
        }
    }

    protected void saveConfigCaseSensitive() {
        //table.getSession().getConfig().setBooleanProperty("filter.lookup_case_sensitive", config_caseSensitive.isSelected());
    }


    public boolean isCaseSensitive() {
        return config_caseSensitive.isSelected();
    }

    public void setCaseSensitive(boolean cs) {
        config_caseSensitive.setSelected(cs);
    }

    public Strategy getStrategy() {
        if (config_endsWith.isSelected()) {
            return Strategy.ENDS_WITH;
        } else if (config_contains.isSelected()) {
            return Strategy.CONTAINS;
        } else if (config_startsWith.isSelected()) {
            return Strategy.STARTS_WITH;
        } else if (config_regexp.isSelected()) {
            return Strategy.REGXP;
        } else if (config_shell.isSelected()) {
            return Strategy.SHELL;
        } else {
            return Strategy.CONTAINS;
        }
    }

    public void setGoFilterEnabled(boolean enable) {
        goFilter.setVisible(enable);
    }
}
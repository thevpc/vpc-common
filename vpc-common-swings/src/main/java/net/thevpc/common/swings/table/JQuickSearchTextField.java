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
package net.thevpc.common.swings.table;

import net.thevpc.common.swings.JDropDownButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import net.thevpc.common.swings.SwingComponentConfigurer;
import net.thevpc.common.swings.SwingComponentConfigurerFactory;
import net.thevpc.common.swings.util.StringFilter;
import net.thevpc.common.swings.util.StringPortionFilter;
import net.thevpc.common.swings.util.StringRegexpFilter;
import net.thevpc.common.swings.util.StringShellFilter;
import net.thevpc.common.swings.util.StringStartsWithFilter;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 6 mai 2006 18:47:41
 */
public class JQuickSearchTextField extends JPanel {
    public static enum Strategy {
        STARTS_WITH,
        ENDS_WITH,
        CONTAINS,
        REGXP,
        SHELL,
    }

    JCheckBoxMenuItem config_caseSensitive;
    JRadioButtonMenuItem config_regexp;
    JRadioButtonMenuItem config_shell;
    JRadioButtonMenuItem config_startsWith;
    JRadioButtonMenuItem config_contains;
    JRadioButtonMenuItem config_endsWith;
    JDropDownButton configButton;
    JTextField filterTextField;
    JMenuItem goFilter;

    public JQuickSearchTextField() {
        goFilter = new JMenuItem("ApplyFilter");
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
        final SwingComponentConfigurer r = SwingComponentConfigurerFactory.getInstance().get(JQuickSearchTextField.class);
        if(r!=null){
            r.onCreateComponent(this);
        }

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

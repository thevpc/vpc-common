package net.vpc.common.swings.app.impl.swing;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.common.prpbind.PValue;
import net.vpc.common.swings.app.*;
import net.vpc.common.swings.app.core.DefaultApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;

public class SwingHelper {

    public static void invokeLong(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            new Thread(r).run();
        }
        r.run();
    }

    public static void invokeLater(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        }
        SwingUtilities.invokeLater(r);
    }

    public static void invokeAndWait(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        }
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (InterruptedException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void prepareJCombobox(JComboBox button, AppToolComponent binding, Application application) {

    }
    public static void prepareAbstractButton(AbstractButton button, AppToolComponent binding, Application application) {
        AppTool tool = binding.tool();
        tool.title().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                button.setText((String) event.getNewValue());
            }
        });
        button.setText(tool.title().get());
        tool.enabled().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                button.setEnabled((Boolean) event.getNewValue());
            }
        });
        button.setEnabled(tool.enabled().get());
        tool.visible().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                button.setVisible((Boolean) event.getNewValue());
            }
        });
        button.setVisible(tool.visible().get());
        PValue<String> group = null;
        if (tool instanceof AppToolCheckBox) {
            AppToolCheckBox cc = (AppToolCheckBox) tool;
            group = cc.group();
            button.setSelected(cc.selected().get());
        }
        if (tool instanceof AppToolRadioBox) {
            AppToolRadioBox cc = (AppToolRadioBox) tool;
            group = cc.group();
            button.setSelected(cc.selected().get());
        }
        if (group != null) {
            String s = group.get();
            if (s != null) {
                ((DefaultApplication) application).getButtonGroup(s).add(button);
            }
            group.listeners().add(new PropertyListener() {
                @Override
                public void propertyUpdated(PropertyEvent event) {
                    if (event.getOldValue() != null) {
                        ((DefaultApplication) application).getButtonGroup((String) event.getOldValue()).remove(button);
                    }
                    if (event.getNewValue() != null) {
                        ((DefaultApplication) application).getButtonGroup((String) event.getNewValue()).add(button);
                    }
                }
            });
        }


        if (tool instanceof AppToolAction) {
            AppToolAction action = (AppToolAction) tool;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ActionListener a = action.action().get();
                    if (a != null) {
                        a.actionPerformed(e);
                    }
                }
            });
        }

        if (tool instanceof AppToolCheckBox) {
            button.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    ((AppToolCheckBox)tool).selected().set(e.getStateChange() == ItemEvent.SELECTED);
                }
            });
        }
        if (tool instanceof AppToolRadioBox) {
            button.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    ((AppToolRadioBox)tool).selected().set(e.getStateChange() == ItemEvent.SELECTED);
                }
            });
        }
    }

    public static JComponent createMenuItem(AppToolComponent b, Application application) {
        AppTool t = b.tool();
        if (t instanceof AppToolFolder) {
            AppToolFolder a = (AppToolFolder) t;
            JMenu m = new JMenu();
            prepareAbstractButton(m, b, application);
            return m;
        }
        if (t instanceof AppToolRadioBox) {
            AppToolRadioBox a = (AppToolRadioBox) t;
            JRadioButtonMenuItem m = new JRadioButtonMenuItem();
            m.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    a.selected().set(e.getStateChange() == ItemEvent.SELECTED);
                }
            });
            prepareAbstractButton(m, b, application);
            return m;
        }
        if (t instanceof AppToolCheckBox) {
            AppToolCheckBox a = (AppToolCheckBox) t;
            JCheckBoxMenuItem m = new JCheckBoxMenuItem();
            m.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    a.selected().set(e.getStateChange() == ItemEvent.SELECTED);
                }
            });
            prepareAbstractButton(m, b, application);
            return m;
        }
        if (t instanceof AppToolAction) {
            AppToolAction a = (AppToolAction) t;
            JMenuItem m = new JMenuItem();
            prepareAbstractButton(m, b, application);
            return m;
        }
        if (t instanceof AppToolSeparator) {
            return new JPopupMenu.Separator();
        }
        throw new IllegalArgumentException("Unsupported");
    }
}

/**
 * ==================================================================== vpc-prs
 * library
 *
 * Pluggable Resources Set is a small library for simplifying plugin based
 * applications
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
package net.thevpc.common.swing.messageset;

import net.thevpc.common.prs.ObjectHolder;
import net.thevpc.common.prs.messageset.MessageSet;
import net.thevpc.common.swing.ComponentTreeVisitor;
import net.thevpc.common.swing.util.ClassMap;
import net.thevpc.common.swing.border.PRSBorder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;

import net.thevpc.common.swing.prs.PRSManager;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 26 juin 2006
 * 23:57:46
 */
public class SwingMessageSetManager {

    private static final String PREFIX = "MessageSetManager.";
    public static final String PROP_SUPPORTED = PREFIX + "Supported";
    public static final String PROP_ID = PREFIX + "Id";
    public static final String PROP_IMPL = PREFIX + "Impl";
    public static final String PROP_MESSAGE_SET = PREFIX + "MessageSet";
    private static ActionMessageSetUpdater actionMessageSetUpdater = new DefaultActionMessageSetUpdater();
    private static ClassMap<ComponentMessageSetUpdater> defaultComponentResourcesUpdaters = new ClassMap<ComponentMessageSetUpdater>();

//    private static PropertyChangeSupport support = new PropertyChangeSupport(MessageSetManager.class);
    static {
        setDefaultComponentMessageSetUpdater(AbstractButton.class, new AbstractButtonMessageSetUpdater());
        setDefaultComponentMessageSetUpdater(JToggleButton.class, new JToggleButtonMessageSetUpdater());
        setDefaultComponentMessageSetUpdater(JLabel.class, new JLabelMessageSetUpdater());
        setDefaultComponentMessageSetUpdater(JTabbedPane.class, new JTabbedPaneMessageSetUpdater());
    }

    public static void updateActions(Collection<? extends Action> actions, MessageSet messageSet) {
        for (Action action : actions) {
            updateAction(action, messageSet);
        }
    }

    public static void updateComponentTree(Component c, MessageSet messageSet) {
        PRSManager.visit(new ComponentTreeVisitor<ObjectHolder<MessageSet>>() {

            public void visit(Component comp, ObjectHolder<MessageSet> userObject) {
                if (comp instanceof JComponent) {
                    MessageSet mset = userObject.get();
                    MessageSet messageSetReplace = (MessageSet) ((JComponent) comp).getClientProperty(PROP_MESSAGE_SET);
                    if (messageSetReplace != null) {
                        userObject.set(messageSetReplace);
                        mset = messageSetReplace;
                    }
                    updateComponent0((JComponent) comp, mset);
                }
            }
        }, c, new ObjectHolder<MessageSet>(messageSet));

        PRSManager.applyOrientation(c, messageSet);
        c.invalidate();
        c.validate();
        c.repaint();
    }

    public static ComponentMessageSetUpdater getDefaultComponentMessageSetUpdater(Class c) {
        return defaultComponentResourcesUpdaters.getBest(c);
    }

    public static void setComponentMessageSet(JComponent component, MessageSet messageSet) {
        component.putClientProperty(PROP_MESSAGE_SET, messageSet);
    }

    public static void setActionMessageSet(Action action, MessageSet messageSet) {
        action.putValue(PROP_MESSAGE_SET, messageSet);
    }

    public static void setDefaultComponentMessageSetUpdater(Class c, ComponentMessageSetUpdater u) {
        if (u == null) {
            defaultComponentResourcesUpdaters.remove(c);
        } else {
            defaultComponentResourcesUpdaters.put(c, u);
        }
    }

    private static void updateComponent0(JComponent c, MessageSet messageSet) {
        Boolean b = (Boolean) c.getClientProperty(PROP_SUPPORTED);
        String id = (String) c.getClientProperty(PROP_ID);
        Border border = c.getBorder();
        if (border instanceof PRSBorder) {
            ((PRSBorder) border).update(messageSet);
        }
        if (b != null && b.booleanValue()) {
            ComponentMessageSetUpdater u = (ComponentMessageSetUpdater) c.getClientProperty(PROP_IMPL);
            if (u == null) {
                u = getDefaultComponentMessageSetUpdater(c.getClass());
            }
            if (u != null) {
                u.updateMessageSet(c, id, messageSet);
            }
        } else if (c instanceof AbstractButton) {
            Action a = ((AbstractButton) c).getAction();
            b = a == null ? null : (Boolean) a.getValue(PROP_SUPPORTED);
            if (b != null && b.booleanValue()) {
                id = (String) a.getValue(PROP_ID);
                ComponentMessageSetUpdater u = (ComponentMessageSetUpdater) c.getClientProperty(PROP_IMPL);
                if (u == null) {
                    u = getDefaultComponentMessageSetUpdater(c.getClass());
                }
                if (u != null) {
                    u.updateMessageSet(c, id, messageSet);
                }
            }
        }
    }

    public static boolean isMessageSetSupported(Action a) {
        return Boolean.TRUE.equals(a.getValue(PROP_SUPPORTED));
    }

    public static boolean isMessageSetSupported(JComponent c) {
        return Boolean.TRUE.equals(c.getClientProperty(PROP_SUPPORTED));
    }

    public static void addMessageSetSupport(Action a, String id, ActionMessageSetUpdater updater) {
        if (isMessageSetSupported(a)) {
            System.err.printf("override addMessageSetSupport (Supported=%s, Impl=%s, id=%s) ==> (Supported=%s, Impl=%s, id=%s)\n", a.getValue(PROP_SUPPORTED), a.getValue(PROP_IMPL), a.getValue(PROP_ID), true, null, id);
        }
        a.putValue(PROP_SUPPORTED, Boolean.TRUE);
        a.putValue(PROP_IMPL, updater);
        a.putValue(PROP_ID, id);
    }

    public static void addMessageSetSupport(Action a, String id) {
        addMessageSetSupport(a, id, null);
    }

    public static void addMessageSetSupport(JComponent c, String id) {
        addMessageSetSupport(c, id, null);
    }

    public static void removeMessageSetSupport(JComponent c) {
        c.putClientProperty(PROP_SUPPORTED, null);
        c.putClientProperty(PROP_IMPL, null);
        c.putClientProperty(PROP_ID, null);
    }

    public static void removeMessageSetSupport(Action c) {
        c.putValue(PROP_SUPPORTED, null);
        c.putValue(PROP_IMPL, null);
        c.putValue(PROP_ID, null);
    }

    public static void addMessageSetSupport(JComponent c, String id, ComponentMessageSetUpdater u) {
        c.putClientProperty(PROP_SUPPORTED, Boolean.TRUE);
        c.putClientProperty(PROP_IMPL, u);
        c.putClientProperty(PROP_ID, id);
        if (u == null) {
            u = getDefaultComponentMessageSetUpdater(c.getClass());
        }
        if (u != null) {
            u.install(c, id);
        }
    }

    public static void updateAction(Action action, MessageSet messageSet) {
        Boolean b = (Boolean) action.getValue(PROP_SUPPORTED);
        if (b != null && b.booleanValue()) {
            String id = (String) action.getValue(PROP_ID);
            ActionMessageSetUpdater u = (ActionMessageSetUpdater) action.getValue(PROP_IMPL);
            if (u == null) {
                u = actionMessageSetUpdater;
            }
            if (u != null) {
                MessageSet messageSetReplace = (MessageSet) action.getValue(PROP_MESSAGE_SET);
                if (messageSetReplace != null) {
                    messageSet = messageSetReplace;
                }
                u.updateResources(action, id, messageSet);
            }
        }
    }
}

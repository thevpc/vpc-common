/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.vpc.common.swings.iconset;

import net.vpc.common.prs.ObjectHolder;
import net.vpc.common.prs.iconset.IconSetManager;
import net.vpc.common.swings.ComponentTreeVisitor;
import net.vpc.common.swings.border.PRSBorder;
import net.vpc.common.swings.SwingLocaleManager;
import net.vpc.common.swings.util.ClassMap;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;
import net.vpc.common.prs.iconset.IconNotFoundException;
import net.vpc.common.prs.iconset.IconSet;
import net.vpc.common.prs.iconset.IconSetNotFoundException;
import net.vpc.common.swings.prs.PRSManager;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 23 juin 2006 17:21:52
 */
public class SwingIconSetManager {
    private static final String PREFIX = "IconSetManager.";
    public static final String PROP_SUPPORTED = PREFIX + "Supported";
    public static final String PROP_ID = PREFIX + "Id";
    public static final String PROP_IMPL = PREFIX + "Impl";
    public static final String PROP_ICON_SET = PREFIX + "IconSet";
    private static ActionIconSetUpdater actionIconSetUpdater = new DefaultActionIconSetUpdater();
    private static ClassMap<ComponentIconSetUpdater> defaultComponentIconSetUpdaters = new ClassMap<ComponentIconSetUpdater>();

    static {
        setDefaultComponentIconSetUpdater(AbstractButton.class, new AbstractButtonIconSetUpdater());
        setDefaultComponentIconSetUpdater(JToggleButton.class, new JToggleButtonIconSetUpdater());
        setDefaultComponentIconSetUpdater(JLabel.class, new JLabelIconSetUpdater());
        setDefaultComponentIconSetUpdater(JTabbedPane.class, new JTabbedPaneIconSetUpdater());
    }

    public static IconSet getComponentIconSet(JComponent component, IconSet defaultIconSet) throws IconSetNotFoundException, IconNotFoundException {
        Object iconSet = component.getClientProperty(PROP_ICON_SET);
        if (iconSet == null) {
            return defaultIconSet;
        } else if (iconSet instanceof String) {
            Locale locale = SwingLocaleManager.getComponentLocale(component);
            return IconSetManager.getIconSetFactory().getIconSet((String) iconSet, locale);
        } else {
            return (IconSet) iconSet;
        }

    }

    public static IconSet getActionIconSet(Action action, IconSet defaultIconSet) throws IconSetNotFoundException, IconNotFoundException {
        Object value = action.getValue(PROP_ICON_SET);
        if (value == null) {
            return defaultIconSet;
        } else {
            if (value instanceof String) {
                String iconSetName = (String) value;
                Locale locale = SwingLocaleManager.getActionLocale(action);
                return IconSetManager.getIconSetFactory().getIconSet(iconSetName, locale);
            } else {
                return (IconSet) value;
            }
        }

    }

    public static void setActionIconSet(Action action, IconSet defaultIconSet) {
        action.putValue(PROP_ICON_SET, defaultIconSet);
    }

    public static void setActionIconSet(Action action, String defaultIconSet) {
        action.putValue(PROP_ICON_SET, defaultIconSet);
    }

    public static void setComponentIconSet(JComponent component, String iconSetName) {
        component.putClientProperty(PROP_ICON_SET, iconSetName);
    }

    public static void setComponentIconSet(JComponent component, IconSet iconSet) {
        component.putClientProperty(PROP_ICON_SET, iconSet);
    }

    public static Icon getComponentIcon(JComponent component, String icon) throws IconSetNotFoundException, IconNotFoundException {
        return getComponentIconSet(component, IconSetManager.getIconSet()).getIcon(icon);
    }

//    public static ImageIcon getIconR(IconSet iconSet, String icon) throws IconNotFoundException {
//        return getIcon(iconSet, icon, IconSet.ErrorType.REQUIRED);
//    }
//
//    public static ImageIcon getIconW(IconSet iconSet, String icon) throws IconNotFoundException {
//        return getIcon(iconSet, icon, IconSet.ErrorType.WARN);
//    }
//
//    public static ImageIcon getIconS(IconSet iconSet, String icon) throws IconNotFoundException {
//        return getIcon(iconSet, icon, IconSet.ErrorType.SILENT);
//    }
//
//    public static ImageIcon getIcon(IconSet iconSet, String icon, IconSet.ErrorType errorType) throws IconNotFoundException {
//        try {
//            return iconSet.getIcon(icon);
//        } catch (IconNotFoundException e) {
//            switch (errorType) {
//                case ERROR: {
//                    System.err.println("Icon \"" + icon + "\" not found in " + iconSet.getId());
//                    throw e;
//                }
//                case REQUIRED: {
//                    System.err.println("Icon \"" + icon + "\" not found in " + iconSet.getId());
//                    return iconSet.getUnknowIcon();
//                }
//                case WARN: {
//                    System.err.println("Icon \"" + icon + "\" not found in " + iconSet.getId());
//                    break;
//                }
//            }
//            return null;
//        }
//    }


    public static void updateActionsIconSet(Collection<? extends Action> actions, IconSet iconSet) {
        for (Action action : actions) {
            updateActionIconSet(action, iconSet);
        }
    }


    public static void updateComponentTreeIconSet(Component c) {
        updateComponentTree(c, IconSetManager.getIconSet());
    }

    public static void updateComponentTree(Component c, IconSet defaultIconSet) {
        PRSManager.visit(new ComponentTreeVisitor<ObjectHolder<IconSet>>() {
            public void visit(Component comp, ObjectHolder<IconSet> userObject) {
                if (comp instanceof JComponent) {
                    IconSet iconSet = userObject.get();
                    iconSet = getComponentIconSet((JComponent) comp, iconSet);
                    userObject.set(iconSet);
                    updateComponentIconSet0((JComponent) comp, iconSet);
                }
            }
        }, c, new ObjectHolder<IconSet>(defaultIconSet));
//        updateComponentTreeIconSet0(c, defaultIconSet);
        c.invalidate();
        c.validate();
        c.repaint();
    }

//    private static void updateComponentTreeIconSet0(Component c, IconSet set) {
//        if (c instanceof JComponent) {
//            set = getComponentIconSet((JComponent) c, set);
//            updateComponentIconSet0((JComponent) c, set);
//        }
//        Component[] children = null;
//        if (c instanceof JMenu) {
//            children = ((JMenu) c).getMenuComponents();
//        } else if (c instanceof Container) {
//            children = ((Container) c).getComponents();
//        }
//        if (children != null) {
//            for (int i = 0; i < children.length; i++) {
//                updateComponentTreeIconSet0(children[i], set);
//            }
//        }
//    }

    public static ComponentIconSetUpdater getDefaultComponentIconSetUpdater(Class c) {
        return defaultComponentIconSetUpdaters.getBest(c);
    }

    public static void setDefaultComponentIconSetUpdater(Class c, ComponentIconSetUpdater u) {
        if (u == null) {
            defaultComponentIconSetUpdaters.remove(c);
        } else {
            defaultComponentIconSetUpdaters.put(c, u);
        }
    }

    private static void updateComponentIconSet0(JComponent c, IconSet componentIconSet) {
        Boolean b = (Boolean) c.getClientProperty(PROP_SUPPORTED);
        String id = (String) c.getClientProperty(PROP_ID);
        Border border = c.getBorder();
        if (border instanceof PRSBorder) {
            ((PRSBorder) border).update(componentIconSet);
        }
        if (b != null && b.booleanValue()) {
            ComponentIconSetUpdater u = (ComponentIconSetUpdater) c.getClientProperty(PROP_IMPL);
            if (u == null) {
                u = getDefaultComponentIconSetUpdater(c.getClass());
            }
            if (u != null) {
                u.updateIconSet(c, id, componentIconSet);
            }
        } else if (c instanceof AbstractButton) {
            Action a = ((AbstractButton) c).getAction();
            b = a == null ? null : (Boolean) a.getValue(PROP_SUPPORTED);
            if (b != null && b.booleanValue()) {
                id = (String) a.getValue(PROP_ID);
                ComponentIconSetUpdater u = (ComponentIconSetUpdater) c.getClientProperty(PROP_IMPL);
                if (u == null) {
                    u = getDefaultComponentIconSetUpdater(c.getClass());
                }
                if (u != null) {
                    u.updateIconSet(c, id, componentIconSet);
                }
            }
        }

    }

    public static void addIconSetSupport(Action a, String id, ActionIconSetUpdater updater) {
        if (isIconSetSupported(a)) {
            System.err.printf("override addIconSetSupport (Supported=%s, Impl=%s, id=%s) ==> (Supported=%s, Impl=%s, id=%s)\n", a.getValue(PROP_SUPPORTED), a.getValue(PROP_IMPL), a.getValue(PROP_ID), true, null, id);
        }
        a.putValue(PROP_SUPPORTED, Boolean.TRUE);
        a.putValue(PROP_IMPL, updater);
        a.putValue(PROP_ID, id);
    }

    public static void addIconSetSupport(Action a, String id) {
        if (isIconSetSupported(a)) {
            System.err.printf("override addIconSetSupport (Supported=%s, Impl=%s, id=%s) ==> (Supported=%s, Impl=%s, id=%s)\n", a.getValue(PROP_SUPPORTED), a.getValue(PROP_IMPL), a.getValue(PROP_ID), true, null, id);
        }
        a.putValue(PROP_SUPPORTED, Boolean.TRUE);
        a.putValue(PROP_IMPL, null);
        a.putValue(PROP_ID, id);
    }

    public static void addIconSetSupport(JComponent c, String id) {
        addIconSetSupport(c, id, null);
    }

//    public static void addIconSetSupport(JComponent c, String[] keys, String[] values) {
//        addIconSetSupport(c, null, null,null);
//    }

    public static void removeIconSetSupport(JComponent c) {
        c.putClientProperty(PROP_SUPPORTED, null);
        c.putClientProperty(PROP_IMPL, null);
        c.putClientProperty(PROP_ID, null);
    }

    public static void removeIconSetSupport(Action c) {
        c.putValue(PROP_SUPPORTED, null);
        c.putValue(PROP_IMPL, null);
        c.putValue(PROP_ID, null);
    }

    public static void addIconSetSupport(JComponent c, String id, ComponentIconSetUpdater u) {
        c.putClientProperty(PROP_SUPPORTED, Boolean.TRUE);
        c.putClientProperty(PROP_IMPL, u);
        c.putClientProperty(PROP_ID, id);
        if (u == null) {
            u = getDefaultComponentIconSetUpdater(c.getClass());
        }
        if (u != null) {
            u.install(c, id);
        }
    }

    public static void updateActionIconSet(Action action, IconSet iconSet) {
        Boolean b = (Boolean) action.getValue(PROP_SUPPORTED);
        if (b != null && b.booleanValue()) {
            ActionIconSetUpdater u = (ActionIconSetUpdater) action.getValue(PROP_IMPL);
            if (u == null) {
                u = actionIconSetUpdater;
            }
            if (u != null) {
                u.updateIconSet(action, getActionIconSet(action, iconSet));
            }
        }
    }

    public static boolean isIconSetSupported(Action a) {
        return Boolean.TRUE.equals(a.getValue(PROP_SUPPORTED));
    }

    public static boolean isIconSetSupported(JComponent c) {
        return Boolean.TRUE.equals(c.getClientProperty(PROP_SUPPORTED));
    }



    private SwingIconSetManager() {
    }
    
   

}

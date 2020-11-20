/**
 * ==================================================================== vpc-prs
 * library
 *
 * Description: <start><end>
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
package net.thevpc.common.swings.prs;

import net.thevpc.common.prs.ResourceSetHolder;
import net.thevpc.common.prs.artset.ArtSet;
import net.thevpc.common.prs.artset.ArtSetManager;
import net.thevpc.common.prs.log.LoggerProvider;
import net.thevpc.common.prs.messageset.MessageSet;
import net.thevpc.common.prs.messageset.MessageSetManager;
import net.thevpc.common.prs.plugin.UrlCacheManager;
import net.thevpc.common.swings.ComponentTreeVisitor;
import net.thevpc.common.swings.border.PRSBorder;
import net.thevpc.common.swings.border.PRSBorderImpl;
import net.thevpc.common.swings.iconset.ActionIconSetUpdater;
import net.thevpc.common.swings.iconset.ComponentIconSetUpdater;
import net.thevpc.common.swings.iconset.SwingIconSetManager;
import net.thevpc.common.swings.messageset.ActionMessageSetUpdater;
import net.thevpc.common.swings.messageset.ComponentMessageSetUpdater;
import net.thevpc.common.swings.messageset.JComboBoxMessageSetUpdater;
import net.thevpc.common.swings.messageset.SwingMessageSetManager;
import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.prs.iconset.IconSetNotFoundException;
import net.thevpc.common.prs.iconset.IconSetManager;
import net.thevpc.common.prs.iconset.IconNotFoundException;
import net.thevpc.common.prs.iconset.IconSetDescriptor;
import net.thevpc.common.prs.iconset.IconSetFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 13 juil. 2006
 * 22:14:21
 */
public class PRSManager {

    private static final String SHORT_NAME_PREFERRED = "SHORT_NAME_PREFERRED";

    public static void update(
            Collection<? extends Action> actions,
            Component component,
            ResourceSetHolder holder) {
        update(actions, component, holder.getMessageSet(), holder.getIconSet());
    }

    public static void update(
            Action[] actions,
            Component component,
            ResourceSetHolder holder) {
        update(actions == null ? null : Arrays.asList(actions), component, holder.getMessageSet(), holder.getIconSet());
    }

    public static void updateOnFirstComponentShown(JComponent component, ResourceSetHolder holder) {
        component.putClientProperty("updateOnFirstComponentShown.ResourceSetHolder", holder);
        component.addComponentListener(updateOnFirstComponentShownComponentAdapter);
    }

    public static void update(
            Component component,
            ResourceSetHolder holder) {
        update(null, component, holder.getMessageSet(), holder.getIconSet());
    }

    public static void update(
            Action[] actions,
            ResourceSetHolder holder) {
        update(actions == null ? null : Arrays.asList(actions), null, holder.getMessageSet(), holder.getIconSet());
    }

    public static void update(
            Collection<Action> actions,
            ResourceSetHolder holder) {
        update(actions, null, holder.getMessageSet(), holder.getIconSet());
    }

    public static void update(
            Action action,
            ResourceSetHolder holder) {
        update(action == null ? new Action[0] : new Action[]{action}, holder);
    }

    public static void update(
            Action action,
            MessageSet messageSet,
            IconSet iconSet) {
        update(Arrays.asList(action == null ? new Action[0] : new Action[]{action}), messageSet, iconSet);
    }

    public static void update(
            Collection<Action> actions,
            MessageSet messageSet,
            IconSet iconSet) {
        update(actions, null, messageSet, iconSet);
    }

    public static void update(
            Component component,
            MessageSet messageSet,
            IconSet iconSet) {
        update(null, component, messageSet, iconSet);
    }

    public static void update(
            Collection<? extends Action> actions,
            Component component,
            MessageSet messageSet,
            IconSet iconSet) {
        if (actions != null) {
            if (messageSet != null) {
                SwingMessageSetManager.updateActions(actions, messageSet);
            }
            if (iconSet != null) {
                SwingIconSetManager.updateActionsIconSet(actions, iconSet);
            }
        }
        if (component != null) {
            if (messageSet != null) {
                SwingMessageSetManager.updateComponentTree(component, messageSet);
            }
            if (iconSet != null) {
                SwingIconSetManager.updateComponentTree(component, iconSet);
            }
        }
    }

    public static void update(
            Collection<Action> actions,
            Component component,
            MessageSet messageSet) {
        if (actions != null) {
            SwingMessageSetManager.updateActions(actions, messageSet);
        }
        if (component != null) {
            SwingMessageSetManager.updateComponentTree(component, messageSet);
        }
    }

    public static void update(
            Collection<Action> actions,
            Component component,
            IconSet iconSet) {
        if (actions != null) {
            SwingIconSetManager.updateActionsIconSet(actions, iconSet == null ? IconSetManager.getIconSet() : iconSet);
        }
        if (component != null) {
            SwingIconSetManager.updateComponentTree(component, iconSet == null ? IconSetManager.getIconSet() : iconSet);
        }
    }

    public static JLabel createLabel(String id) {
        JLabel item = new JLabel("");
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static PRSBorder createBorder(String id) {
        return createBorder(id, BorderFactory.createTitledBorder(id));
    }

    public static PRSBorder createBorder(String id, TitledBorder titledBorder) {
        return new PRSBorderImpl(id, titledBorder);
    }

    public static JButton createButton(String id) {
        JButton item = new JButton("");
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static JToggleButton createToggleButton(String id) {
        JToggleButton item = new JToggleButton("");
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static JCheckBox createCheck(String id, boolean selected) {
        JCheckBox item = new JCheckBox("", selected);
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static JMenu createMenu(String id) {
        JMenu item = new JMenu("");
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static JMenuItem createMenuItem(String id) {
        JMenuItem item = new JMenuItem("");
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static JCheckBoxMenuItem createCheckBoxMenuItem(String id) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem("");
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static JRadioButtonMenuItem createCheckButtonMenuItem(String id) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem("");
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static JRadioButton createRadio(String id, boolean selected) {
        JRadioButton item = new JRadioButton("", selected);
        SwingMessageSetManager.addMessageSetSupport(item, id);
        SwingIconSetManager.addIconSetSupport(item, id);
        return item;
    }

    public static void addSupport(Action a, String messageId, String iconId) {
        SwingMessageSetManager.addMessageSetSupport(a, messageId);
        SwingIconSetManager.addIconSetSupport(a, iconId);
    }

    public static void addSupport(Action a, String id) {
        addSupport(a, id, id);
    }

    public static void addSupport(JComponent a, String messageId, String iconId) {
        SwingMessageSetManager.addMessageSetSupport(a, messageId);
        SwingIconSetManager.addIconSetSupport(a, iconId);
    }

    public static void addSupport(JComponent a, String id) {
        addSupport(a, id, id);
    }

    public static void addSupport(JComponent a, String id, ComponentMessageSetUpdater mu, ComponentIconSetUpdater iu) {
        SwingMessageSetManager.addMessageSetSupport(a, id, mu);
        SwingIconSetManager.addIconSetSupport(a, id, iu);
    }

    public static void addSupport(JComponent a, String id, ComponentResourcesUpdater u) {
        ComponentResourcesUpdaterAdapter w = new ComponentResourcesUpdaterAdapter(u);
        SwingMessageSetManager.addMessageSetSupport(a, id, w);
        SwingIconSetManager.addIconSetSupport(a, id, w);
    }

    public static void applyOrientation(Component c, MessageSet messageSet) {
        applyOrientation(c, null, messageSet.getLocale());
    }

    public static void applyOrientation(Component c) {
        applyOrientation(c, null, Locale.getDefault());
    }

    private static void applyOrientation(Component c, ComponentOrientation o, Locale locale) {
        if (o == null) {
            o = ComponentOrientation.getOrientation(locale);
        }
        //workaround for JSPlitPane
        if (c instanceof JSplitPane) {
            JSplitPane s = (JSplitPane) c;
            ComponentOrientation oldComponentOrientation = c.getComponentOrientation();
            Component l = s.getLeftComponent();
            Component r = s.getRightComponent();
            if (s.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                if ((ComponentOrientation.LEFT_TO_RIGHT.equals(o) && ComponentOrientation.RIGHT_TO_LEFT.equals(oldComponentOrientation))
                        || (ComponentOrientation.RIGHT_TO_LEFT.equals(o) && ComponentOrientation.LEFT_TO_RIGHT.equals(oldComponentOrientation))) {
                    s.setLeftComponent(null);
                    s.setRightComponent(null);
                    s.setLeftComponent(r);
                    s.setRightComponent(l);
                    s.setDividerLocation(1 - s.getDividerLocation());
                }
            }
        }

        //workaround for JMenu
        if (c instanceof JMenu) {
            JMenu s = (JMenu) c;
            applyOrientation(s.getPopupMenu(), o, locale);
        }

        c.setComponentOrientation(o);
        if (c instanceof Container) {
            Component[] cc = ((Container) c).getComponents();
            for (Component child : cc) {
                applyOrientation(child, o, locale);
            }
        }
    }

    public static JComboBox createCombo(String name, String valuesKey, Object[] values, int defaultSelected) {
        JComboBox item = new JComboBox(values);
        addSupport(item, name, new JComboBoxMessageSetUpdater(), null);
        item.putClientProperty("ValuesKey", valuesKey);
        item.setRenderer(new LocalizedByValueComboboxRenderer(item, null));

        item.setSelectedIndex(defaultSelected);
        return item;
    }

    public static void configure(JComponent component, ResourceSetHolder resourceSetHolder) {
        SwingMessageSetManager.setComponentMessageSet(component, resourceSetHolder.getMessageSet());
        SwingIconSetManager.setComponentIconSet(component, resourceSetHolder.getIconSet().getId());
    }

    public static void addMessageSetSupport(JComponent c, String id) {
        SwingMessageSetManager.addMessageSetSupport(c, id);
    }

    public static void addMessageSetSupport(Action a, String id) {
        SwingMessageSetManager.addMessageSetSupport(a, id);
    }

    public static void removeMessageSetSupport(JComponent c) {
        SwingMessageSetManager.removeMessageSetSupport(c);
    }

    public static void removeSupport(Action c) {
        SwingMessageSetManager.removeMessageSetSupport(c);
        SwingIconSetManager.removeIconSetSupport(c);
    }

    public static void removeSupport(JComponent c) {
        SwingMessageSetManager.removeMessageSetSupport(c);
        SwingIconSetManager.removeIconSetSupport(c);
    }

    public static void removeMessageSetSupport(Action c) {
        SwingMessageSetManager.removeMessageSetSupport(c);
    }

    public static void addMessageSetSupport(JComponent c, String id, ComponentMessageSetUpdater u) {
        SwingMessageSetManager.addMessageSetSupport(c, id, u);
    }

    public static void loadAvailableMessageSets(URL repository, ClassLoader parent, Object owner, UrlCacheManager urlCacheManager) throws IOException {
        MessageSetManager.loadAvailableMessageSets(repository, parent, owner, urlCacheManager);
    }

    public static void setComponentMessageSet(JComponent component, MessageSet messageSet) {
        SwingMessageSetManager.setComponentMessageSet(component, messageSet);
    }

    public static void addIconSetSupport(Action a, String id, ActionIconSetUpdater updater) {
        SwingIconSetManager.addIconSetSupport(a, id, updater);
    }

    public static void addMessageSetSupport(Action a, String id, ActionMessageSetUpdater updater) {
        SwingMessageSetManager.addMessageSetSupport(a, id, updater);
    }

    public static void setActionMessageSet(Action a, MessageSet messageSet) {
        SwingMessageSetManager.setActionMessageSet(a, messageSet);
    }

    public static void setActionIconSet(Action a, IconSet messageSet) {
        SwingIconSetManager.setActionIconSet(a, messageSet);
    }

    public static Icon getIcon(String iconSetName, String icon) throws IconSetNotFoundException, IconNotFoundException {
        return IconSetManager.getIcon(iconSetName, icon);
    }

    public static Icon getIcon(String icon, Locale locale) throws IconSetNotFoundException, IconNotFoundException {
        return IconSetManager.getIcon(icon, locale);
    }

    public static Icon getIcon(String iconSetName, String icon, Locale locale) throws IconSetNotFoundException, IconNotFoundException {
        return IconSetManager.getIcon(iconSetName, icon, locale);
    }

    public static IconSet getComponentIconSet(JComponent component, IconSet defaultIconSet) throws IconSetNotFoundException, IconNotFoundException {
        return SwingIconSetManager.getComponentIconSet(component, defaultIconSet);
    }

    public static IconSet getActionIconSet(Action action, IconSet defaultIconSet) throws IconSetNotFoundException, IconNotFoundException {
        return SwingIconSetManager.getActionIconSet(action, defaultIconSet);
    }

    public static void setComponentResourceSetHolder(JComponent component, ResourceSetHolder h) {
//        IconSetManager.setComponentIconSet(component, h.getIconSet());
        SwingMessageSetManager.setComponentMessageSet(component, h.getMessageSet());
    }

    public static void setComponentIconSet(JComponent component, String iconSetName) {
        SwingIconSetManager.setComponentIconSet(component, iconSetName);
    }

    public static void setComponentIconSet(JComponent component, IconSet iconSet) {
        SwingIconSetManager.setComponentIconSet(component, iconSet);
    }

    public static Icon getComponentIcon(JComponent component, String icon) throws IconSetNotFoundException, IconNotFoundException {
        return SwingIconSetManager.getComponentIcon(component, icon);
    }

    public static IconSetFactory getIconSetFactory() {
        return IconSetManager.getIconSetFactory();
    }

    public static void setIconSetFactory(IconSetFactory fct) {
        IconSetManager.setIconSetFactory(fct);
    }

    public static IconSet getIconSet() {
        return IconSetManager.getIconSet();
    }

    public static IconSet getIconSet(String iconSetName) {
        return IconSetManager.getIconSet(iconSetName);
    }

    public static void setIconSet(IconSet iconSet) {
        IconSetManager.setIconSet(iconSet);
    }

    public static void setIconSet(String iconSetName) {
        IconSetManager.setIconSet(iconSetName);
    }

    public static void setIconSet(String iconSetName, Locale locale) {
        IconSetManager.setIconSet(iconSetName, locale);
    }

    public static void registerIconSet(IconSetDescriptor desc, LoggerProvider loggerProvider) {
        IconSetManager.registerIconSet(desc, loggerProvider);
    }

    public static IconSetDescriptor[] getIconSetDescriptors() {
        return IconSetManager.getIconSetDescriptors();
    }

    public static void updateActionsIconSet(Collection<? extends Action> actions, IconSet iconSet) {
        SwingIconSetManager.updateActionsIconSet(actions, iconSet);
    }

    public static void updateComponentTreeIconSet(Component c) {
        SwingIconSetManager.updateComponentTreeIconSet(c);
    }

    public static void updateComponentTreeIconSet(Component c, IconSet defaultIconSet) {
        SwingIconSetManager.updateComponentTree(c, defaultIconSet);
    }

    public static ComponentIconSetUpdater getDefaultComponentIconSetUpdater(Class c) {
        return SwingIconSetManager.getDefaultComponentIconSetUpdater(c);
    }

    public static void setDefaultComponentIconSetUpdater(Class c, ComponentIconSetUpdater u) {
        SwingIconSetManager.setDefaultComponentIconSetUpdater(c, u);
    }

    public static void setDefaultComponentMessageSetUpdater(Class c, ComponentMessageSetUpdater u) {
        SwingMessageSetManager.setDefaultComponentMessageSetUpdater(c, u);
    }

    public static ComponentMessageSetUpdater getDefaultComponentMessageSetUpdater(Class c) {
        return SwingMessageSetManager.getDefaultComponentMessageSetUpdater(c);
    }

    public static void addIconSetSupport(Action a, String id) {
        SwingIconSetManager.addIconSetSupport(a, id);
    }

    public static void addIconSetSupport(JComponent c, String id) {
        SwingIconSetManager.addIconSetSupport(c, id);
    }

    public static void removeIconSetSupport(JComponent c) {
        SwingIconSetManager.removeIconSetSupport(c);
    }

    public static void removeIconSetSupport(Action c) {
        SwingIconSetManager.removeIconSetSupport(c);
    }

    public static void addIconSetSupport(JComponent c, String id, ComponentIconSetUpdater u) {
        SwingIconSetManager.addIconSetSupport(c, id, u);
    }

    public static void updateActionIconSet(Action action, IconSet iconSet) {
        SwingIconSetManager.updateActionIconSet(action, iconSet);
    }

    public static boolean isIconSetSupported(Action a) {
        return SwingIconSetManager.isIconSetSupported(a);
    }

    public static boolean isIconSetSupported(JComponent c) {
        return SwingIconSetManager.isIconSetSupported(c);
    }

    public static IconSetDescriptor[] lookupIconsetDescriptors(URL url, ClassLoader parent, Object owner, UrlCacheManager urlCacheManager, LoggerProvider loggerProvider) throws IOException {
        return IconSetManager.lookupIconsetDescriptors(url, parent, owner, urlCacheManager, loggerProvider);
    }

    public static void registerMessageSet(URL messageSetURL, ClassLoader parent, Object owner, UrlCacheManager urlCacheManager, LoggerProvider loggerProvider) throws IOException {
        MessageSetManager.registerMessageSet(messageSetURL, parent, owner, urlCacheManager);
    }

    public static void registerIconSet(URL url, ClassLoader parent, Object owner, UrlCacheManager urlCacheManager, LoggerProvider loggerProvider) throws IOException {
        IconSetManager.registerIconSet(url, parent, owner, urlCacheManager, loggerProvider);
    }

    public static void loadAvailableIconSets(URL repository, ClassLoader parent, Object owner, UrlCacheManager urlCacheManager, LoggerProvider loggerProvider) throws IOException {
        IconSetManager.loadAvailableIconSets(repository, parent, owner, urlCacheManager, loggerProvider);
    }

    public static void visit(ComponentTreeVisitor visitor, Component c, Object userObject) {
        visitor.visit(c, userObject);
        Component[] children = null;
        MenuElement[] menuElements = null;
        if (c instanceof MenuElement) {
            menuElements = ((MenuElement) c).getSubElements();
//        }else if (c instanceof JMenu) {
//            children = ((JMenu) c).getMenuComponents();
//        } else if (c instanceof JMenuBar) {
//            menuElements = ((JMenuBar) c).getSubElements();
        } else if (c instanceof Container) {
            children = ((Container) c).getComponents();
        }
        if (children != null) {
            for (Component aChildren : children) {
                visit(visitor, aChildren, userObject);
            }
        }
        if (menuElements != null) {
            for (MenuElement aChildren : menuElements) {
                visit(visitor, aChildren.getComponent(), userObject);
            }
        }
    }
    private static ComponentAdapter updateOnFirstComponentShownComponentAdapter = new ComponentAdapter() {

        @Override
        public void componentShown(ComponentEvent e) {
            JComponent component = (JComponent) e.getSource();
            if (!Boolean.TRUE.equals(component.getClientProperty("updateAtFirstShowing.Done"))) {
                component.putClientProperty("updateOnFirstComponentShown.Done", Boolean.TRUE);
                update(component, (ResourceSetHolder) component.getClientProperty("updateOnFirstComponentShown.ResourceSetHolder"));
            }
        }
    };

    public static boolean isShortNamePreferred(AbstractButton b) {
        return Boolean.TRUE.equals(b.getClientProperty(SHORT_NAME_PREFERRED));
    }

    public static void setShortNamePreferred(AbstractButton b, boolean enable) {
        b.putClientProperty(SHORT_NAME_PREFERRED, enable);
    }

    public static void registerArtSets(URL url, ClassLoader parent, Object owner) throws IOException {
        ArtSetManager.registerArtSets(url, parent, owner);
    }

    public static ArtSet getArtSet(String id) {
        return ArtSetManager.getArtSet(id);
    }

    public static ArtSet[] getArtSets() {
        return ArtSetManager.getArtSets();
    }

    public static void setCurrentArtSet(String id) {
        ArtSetManager.setCurrent(id);
    }

    public static ArtSet getCurrentArtSet() {
        return ArtSetManager.getCurrent();
    }
}

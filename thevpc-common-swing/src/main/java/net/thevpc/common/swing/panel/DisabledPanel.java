/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.panel;

import net.thevpc.common.swing.layout.OverlapLayout;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import javax.swing.*;

/*
 *  The DisablePanel will simulate the usage of a "Glass Pane" except that the
 *  glass pane effect is only for the container and not the entire frame.
 *  By default the glass pane is invisible. When the DisablePanel is disabled
 *  then the glass pane is made visible. In addition:
 *
 *  a) All MouseEvents will now be intercepted by the glass pane.
 *  b) The panel is removed from the normal focus traveral policy to prevent
 *     any component on the panel from receiving focus.
 *  c) Key Bindings for any component on the panel will be intercepted.
 *
 *  Therefore, the panel and components will behave as though they are disabled
 *  even though the state of each component has not been changed.
 *
 *  The background color of the glass pane should use a color with an
 *  alpha value to create the disabled look.
 *
 *  The other approach to disabling components on a Container is to recurse
 *  through all the components and set each individual component disabled.
 *  This class supports two static methods to support this functionality:
 *
 *  a) disable( Container ) - to disable all Components on the Container
 *  b) enable ( Container ) - to enable all Components disabled by the
 *     disable() method. That is any component that was disabled prior to using
 *     the disable() method method will remain disabled.
 * sources from:  https://tips4java.wordpress.com/2009/08/02/disabled-panel/
 */
public class DisabledPanel extends JPanel {

    private static DisabledEventQueue queue = new DisabledEventQueue();

    private static Map<Container, List<JComponent>> containers = new HashMap<Container, List<JComponent>>();

    private JComponent glassPane;

    /**
     * Create a DisablePanel for the specified Container. The disabled color
     * will be the following color from the UIManager with an alpha value:
     * UIManager.getColor("inactiveCaptionBorder");
     *
     * @param container a Container to be added to this DisabledPanel
     */
    public DisabledPanel(Container container) {
        this(container, null);
    }

    /**
     * Create a DisablePanel for the specified Container using the specified
     * disabled color.
     *
     * @param disabledColor the background color of the GlassPane
     * @param container a Container to be added to this DisabledPanel
     */
    public DisabledPanel(Container container, Color disabledColor) {
        setLayout(new OverlapLayout());
        add(container);

        glassPane = new GlassPane();
        add(glassPane);

        if (disabledColor != null) {
            glassPane.setBackground(disabledColor);
        }

        setFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
    }

    /**
     * The background color of the glass pane.
     *
     * @return the background color of the glass pane
     */
    public Color getDisabledColor() {
        return glassPane.getBackground();
    }

    /**
     * Set the background color of the glass pane. This color should contain an
     * alpha value to give the glass pane a transparent effect.
     *
     * @param disabledColor the background color of the glass pane
     */
    public void setDisabledColor(Color disabledColor) {
        glassPane.setBackground(disabledColor);
    }

    /**
     * The glass pane of this DisablePanel. It can be customized by adding
     * components to it.
     *
     * @return the glass pane
     */
    public JComponent getGlassPane() {
        return glassPane;
    }

    /**
     * Use a custom glass pane. You are responsible for adding the appropriate
     * mouse listeners to intercept mouse events.
     *
     * @param glassPane a JComponent to be used as a glass pane
     */
    public void setGlassPane(JComponent glassPane) {
        this.glassPane = glassPane;
    }

    /**
     * Change the enabled state of the panel.
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {
            glassPane.setVisible(false);
            setFocusCycleRoot(false);
            queue.removePanel(this);
        } else {
            glassPane.setVisible(true);
            setFocusCycleRoot(true);  // remove from focus cycle
            queue.addPanel(this);
        }
    }

    /**
     * Because we use layered panels this should be disabled.
     */
    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    /**
     * Convenience static method to disable all components of a given Container,
     * including nested Containers.
     *
     * @param container the Container containing Components to be disabled
     */
    public static void disable(Container container) {
        List<JComponent> components
                = DisabledPanel.getDescendantsOfType(JComponent.class, container, true);
        List<JComponent> enabledComponents = new ArrayList<JComponent>();
        containers.put(container, enabledComponents);

        for (JComponent component : components) {
            if (component.isEnabled()) {
                enabledComponents.add(component);
                component.setEnabled(false);
            }
        }
    }

    /**
     * Convenience static method to enable Components disabled by using the
     * disable() method. Only Components disable by the disable() method will be
     * enabled.
     *
     * @param container a Container that has been previously disabled.
     */
    public static void enable(Container container) {
        List<JComponent> enabledComponents = containers.get(container);

        if (enabledComponents != null) {
            for (JComponent component : enabledComponents) {
                component.setEnabled(true);
            }

            containers.remove(container);
        }
    }

    /**
     * A simple "glass pane" that has two functions:
     *
     * a) to paint over top of the Container added to the DisablePanel b) to
     * intercept mouse events when visible
     */
    class GlassPane extends JComponent {

        public GlassPane() {
            setOpaque(false);
            setVisible(false);
            Color base = UIManager.getColor("inactiveCaptionBorder");
            base = (base == null) ? Color.LIGHT_GRAY : base;
            Color background = new Color(base.getRed(), base.getGreen(), base.getBlue(), 128);
            setBackground(background);

            //  Disable Mouse events for the panel
            addMouseListener(new MouseAdapter() {
            });
            addMouseMotionListener(new MouseMotionAdapter() {
            });
        }

        /*
		 *  The component is transparent but we want to paint the background
		 *  to give it the disabled look.
         */
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getSize().width, getSize().height);
        }
    }

    /**
     * A custom EventQueue to intercept Key Bindings that are used by any
     * component on a DisabledPanel. When a DisabledPanel is disabled it is
     * registered with the DisabledEventQueue. This class will check if any
     * components on the DisablePanel use KeyBindings. If not then nothing
     * changes. However, if some do, then the DisableEventQueue installs itself
     * as the current EquentQueue. The dispatchEvent() method is overriden to
     * check each KeyEvent. If the KeyEvent is for a Component on a DisablePanel
     * then the event is ignored, otherwise it is dispatched for normal
     * processing.
     */
    static class DisabledEventQueue extends EventQueue
            implements WindowListener {

        private Map<DisabledPanel, Set<KeyStroke>> panels
                = new HashMap<DisabledPanel, Set<KeyStroke>>();

        /**
         * Check if any component on the DisabledPanel is using Key Bindings. If
         * so, then track the bindings and use a custom EventQueue to intercept
         * the KeyStroke before it is passed to the component.
         */
        public void addPanel(DisabledPanel panel) {
            //  Get all the KeyStrokes used by all the components on the panel

            Set<KeyStroke> keyStrokes = getKeyStrokes(panel);

            if (keyStrokes.size() == 0) {
                return;
            }

            panels.put(panel, keyStrokes);

            //  More than one panel can be disabled but we only need to install
            //  the custom EventQueue when the first panel is disabled.
            EventQueue current = Toolkit.getDefaultToolkit().getSystemEventQueue();

            if (current != this) {
                current.push(queue);
            }

            //  We need to track when a Window is closed so we can remove
            //  the references for all the DisabledPanels on that window.
            Window window = SwingUtilities.windowForComponent(panel);
            if (window != null) {
                window.removeWindowListener(this);
                window.addWindowListener(this);
            }
        }

        /**
         * Check each component to see if its using Key Bindings
         */
        private Set<KeyStroke> getKeyStrokes(DisabledPanel panel) {
            Set<KeyStroke> keyStrokes = new HashSet<KeyStroke>();

            //  Only JComponents use Key Bindings
            Container container = ((Container) panel.getComponent(1));
            List<JComponent> components
                    = DisabledPanel.getDescendantsOfType(JComponent.class, container);

            //  We only care about the WHEN_IN_FOCUSED_WINDOW bindings
            for (JComponent component : components) {
                InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

                if (im != null && im.allKeys() != null) {
                    for (KeyStroke keyStroke : im.allKeys()) {
                        keyStrokes.add(keyStroke);
                    }
                }
            }

            return keyStrokes;
        }

        /**
         * The panel is no longer disabled so we no longer need to intercept its
         * KeyStrokes. Restore the default EventQueue when all panels using Key
         * Bindings have been enabled.
         */
        public void removePanel(DisabledPanel panel) {
            if (panels.remove(panel) != null
                    && panels.size() == 0) {
                pop();
            }
        }

        /**
         * Intercept KeyEvents bound to a component on a DisabledPanel.
         */
        @Override
        protected void dispatchEvent(AWTEvent event) {
            //  Potentially intercept KeyEvents

            if (event.getID() == KeyEvent.KEY_TYPED
                    || event.getID() == KeyEvent.KEY_PRESSED
                    || event.getID() == KeyEvent.KEY_RELEASED) {
                KeyEvent keyEvent = (KeyEvent) event;
                KeyStroke keyStroke = KeyStroke.getKeyStrokeForEvent(keyEvent);

                //  When using Key Bindings, the source of the KeyEvent will be
                //  the Window. Check each panel belonging to the source Window.
                for (DisabledPanel panel : panels.keySet()) {
                    Window panelWindow = SwingUtilities.windowForComponent(panel);

                    //  A binding was found so just return without dispatching it.
                    if (panelWindow == keyEvent.getComponent()
                            && searchForKeyBinding(panel, keyStroke)) {
                        return;
                    }
                }
            }

            //  Dispatch normally
            super.dispatchEvent(event);
        }

        /**
         * Check if the KeyStroke is for a Component on the DisablePanel
         */
        private boolean searchForKeyBinding(DisabledPanel panel, KeyStroke keyStroke) {
            Set<KeyStroke> keyStrokes = panels.get(panel);

            return keyStrokes.contains(keyStroke);
        }

        //  Implement WindowListener interface
        /**
         * When a Window containing a DisablePanel that has been disabled is
         * closed, remove the DisablePanel from the DisabledEventQueue. This may
         * result in the DisabledEventQueue deregistering itself as the current
         * EventQueue.
         */
        public void windowClosed(WindowEvent e) {
            List<DisabledPanel> panelsToRemove = new ArrayList<DisabledPanel>();
            Window window = e.getWindow();

            //  Create a List of DisabledPanels to remove
            for (DisabledPanel panel : panels.keySet()) {
                Window panelWindow = SwingUtilities.windowForComponent(panel);

                if (panelWindow == window) {
                    panelsToRemove.add(panel);
                }
            }

            //  Remove panels here to prevent ConcurrentModificationException
            for (DisabledPanel panel : panelsToRemove) {
                removePanel(panel);
            }

            window.removeWindowListener(this);
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowClosing(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowOpened(WindowEvent e) {
        }
    }

    //////////////////////////////////
    /**
     * Convenience method for searching below <code>container</code> in the
     * component hierarchy and return nested components that are instances of
     * class <code>clazz</code> it finds. Returns an empty list if no such
     * components exist in the container.
     * <P>
     * Invoking this method with a class parameter of JComponent.class will
     * return all nested components.
     * <P>
     * This method invokes getDescendantsOfType(clazz, container, true)
     *
     * @param clazz the class of components whose instances are to be found.
     * @param container the container at which to begin the search
     * @return the List of components
     * @param <T> type
     */
    public static <T extends JComponent> List<T> getDescendantsOfType(
            Class<T> clazz, Container container) {
        return getDescendantsOfType(clazz, container, true);
    }

    /**
     * Convenience method for searching below <code>container</code> in the
     * component hierarchy and return nested components that are instances of
     * class <code>clazz</code> it finds. Returns an empty list if no such
     * components exist in the container.
     * <P>
     * Invoking this method with a class parameter of JComponent.class will
     * return all nested components.
     *
     * @param clazz the class of components whose instances are to be found.
     * @param container the container at which to begin the search
     * @param nested true to list components nested within another listed
     * component, false otherwise
     * @return the List of components
     * @param <T> type
     */
    public static <T extends JComponent> List<T> getDescendantsOfType(
            Class<T> clazz, Container container, boolean nested) {
        List<T> tList = new ArrayList<T>();
        for (Component component : container.getComponents()) {
            if (clazz.isAssignableFrom(component.getClass())) {
                tList.add(clazz.cast(component));
            }
            if (nested || !clazz.isAssignableFrom(component.getClass())) {
                tList.addAll(DisabledPanel.<T>getDescendantsOfType(clazz,
                        (Container) component, nested));
            }
        }
        return tList;
    }

    /**
     * Convenience method that searches below <code>container</code> in the
     * component hierarchy and returns the first found component that is an
     * instance of class <code>clazz</code> having the bound property value.
     * Returns {@code null} if such component cannot be found.
     * <P>
     * This method invokes getDescendantOfType(clazz, container, property,
     * value, true)
     *
     * @param clazz the class of component whose instance is to be found.
     * @param container the container at which to begin the search
     * @param property the className of the bound property, exactly as expressed
     * in the accessor e.g. "Text" for getText(), "Value" for getValue().
     * @param value the value of the bound property
     * @return the component, or null if no such component exists in the
     * container
     * @throws java.lang.IllegalArgumentException if the bound property does not
     * exist for the class or cannot be accessed
     * @param <T> type
     */
    public static <T extends JComponent> T getDescendantOfType(
            Class<T> clazz, Container container, String property, Object value)
            throws IllegalArgumentException {
        return getDescendantOfType(clazz, container, property, value, true);
    }

    /**
     * Convenience method that searches below <code>container</code> in the
     * component hierarchy and returns the first found component that is an
     * instance of class <code>clazz</code> and has the bound property value.
     * Returns {@code null} if such component cannot be found.
     *
     * @param clazz the class of component whose instance to be found.
     * @param container the container at which to begin the search
     * @param property the className of the bound property, exactly as expressed
     * in the accessor e.g. "Text" for getText(), "Value" for getValue().
     * @param value the value of the bound property
     * @param nested true to list components nested within another component
     * which is also an instance of <code>clazz</code>, false otherwise
     * @return the component, or null if no such component exists in the
     * container
     * @throws java.lang.IllegalArgumentException if the bound property does not
     * exist for the class or cannot be accessed
     * @param <T> type
     */
    public static <T extends JComponent> T getDescendantOfType(Class<T> clazz,
            Container container, String property, Object value, boolean nested)
            throws IllegalArgumentException {
        List<T> list = getDescendantsOfType(clazz, container, nested);
        return getComponentFromList(clazz, list, property, value);
    }

    /**
     * Convenience method for searching below <code>container</code> in the
     * component hierarchy and return nested components of class
     * <code>clazz</code> it finds. Returns an empty list if no such components
     * exist in the container.
     * <P>
     * This method invokes getDescendantsOfClass(clazz, container, true)
     *
     * @param clazz the class of components to be found.
     * @param container the container at which to begin the search
     * @return the List of components
     * @param <T> type
     */
    public static <T extends JComponent> List<T> getDescendantsOfClass(
            Class<T> clazz, Container container) {
        return getDescendantsOfClass(clazz, container, true);
    }

    /**
     * Convenience method for searching below <code>container</code> in the
     * component hierarchy and return nested components of class
     * <code>clazz</code> it finds. Returns an empty list if no such components
     * exist in the container.
     *
     * @param clazz the class of components to be found.
     * @param container the container at which to begin the search
     * @param nested true to list components nested within another listed
     * component, false otherwise
     * @return the List of components
     * @param <T> type
     */
    public static <T extends JComponent> List<T> getDescendantsOfClass(
            Class<T> clazz, Container container, boolean nested) {
        List<T> tList = new ArrayList<T>();
        for (Component component : container.getComponents()) {
            if (clazz.equals(component.getClass())) {
                tList.add(clazz.cast(component));
            }
            if (nested || !clazz.equals(component.getClass())) {
                tList.addAll(DisabledPanel.<T>getDescendantsOfClass(clazz,
                        (Container) component, nested));
            }
        }
        return tList;
    }

    /**
     * Convenience method that searches below <code>container</code> in the
     * component hierarchy in a depth first manner and returns the first found
     * component of class <code>clazz</code> having the bound property value.
     * <P>
     * Returns {@code null} if such component cannot be found.
     * <P>
     * This method invokes getDescendantOfClass(clazz, container, property,
     * value, true)
     *
     * @param clazz the class of component to be found.
     * @param container the container at which to begin the search
     * @param property the className of the bound property, exactly as expressed
     * in the accessor e.g. "Text" for getText(), "Value" for getValue(). This
     * parameter is case sensitive.
     * @param value the value of the bound property
     * @return the component, or null if no such component exists in the
     * container's hierarchy.
     * @throws java.lang.IllegalArgumentException if the bound property does not
     * exist for the class or cannot be accessed
     * @param <T> type
     */
    public static <T extends JComponent> T getDescendantOfClass(Class<T> clazz,
            Container container, String property, Object value)
            throws IllegalArgumentException {
        return getDescendantOfClass(clazz, container, property, value, true);
    }

    /**
     * Convenience method that searches below <code>container</code> in the
     * component hierarchy in a depth first manner and returns the first found
     * component of class <code>clazz</code> having the bound property value.
     * <P>
     * Returns {@code null} if such component cannot be found.
     *
     * @param clazz the class of component to be found.
     * @param container the container at which to begin the search
     * @param property the className of the bound property, exactly as expressed
     * in the accessor e.g. "Text" for getText(), "Value" for getValue(). This
     * parameter is case sensitive.
     * @param value the value of the bound property
     * @param nested true to include components nested within another listed
     * component, false otherwise
     * @return the component, or null if no such component exists in the
     * container's hierarchy
     * @throws java.lang.IllegalArgumentException if the bound property does not
     * exist for the class or cannot be accessed
     * @param <T> type
     */
    public static <T extends JComponent> T getDescendantOfClass(Class<T> clazz,
            Container container, String property, Object value, boolean nested)
            throws IllegalArgumentException {
        List<T> list = getDescendantsOfClass(clazz, container, nested);
        return getComponentFromList(clazz, list, property, value);
    }

    private static <T extends JComponent> T getComponentFromList(Class<T> clazz,
            List<T> list, String property, Object value)
            throws IllegalArgumentException {
        T retVal = null;
        Method method = null;
        try {
            method = clazz.getMethod("get" + property);
        } catch (NoSuchMethodException ex) {
            try {
                method = clazz.getMethod("is" + property);
            } catch (NoSuchMethodException ex1) {
                throw new IllegalArgumentException("Property " + property
                        + " not found in class " + clazz.getName());
            }
        }
        try {
            for (T t : list) {
                Object testVal = method.invoke(t);
                if (equals(value, testVal)) {
                    return t;
                }
            }
        } catch (InvocationTargetException ex) {
            throw new IllegalArgumentException(
                    "Error accessing property " + property
                    + " in class " + clazz.getName());
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException(
                    "Property " + property
                    + " cannot be accessed in class " + clazz.getName());
        } catch (SecurityException ex) {
            throw new IllegalArgumentException(
                    "Property " + property
                    + " cannot be accessed in class " + clazz.getName());
        }
        return retVal;
    }

    /**
     * Convenience method for determining whether two objects are either equal
     * or both null.
     *
     * @param obj1 the first reference object to compare.
     * @param obj2 the second reference object to compare.
     * @return true if obj1 and obj2 are equal or if both are null, false
     * otherwise
     */
    public static boolean equals(Object obj1, Object obj2) {
        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }

    /**
     * Convenience method for mapping a container in the hierarchy to its
     * contained components. The keys are the containers, and the values are
     * lists of contained components.
     * <P>
     * Implementation note: The returned value is a HashMap and the values are
     * of type ArrayList. This is subject to change, so callers should code
     * against the interfaces Map and List.
     *
     * @param container The JComponent to be mapped
     * @param nested true to drill down to nested containers, false otherwise
     * @return the Map of the UI
     */
    public static Map<JComponent, List<JComponent>> getComponentMap(
            JComponent container, boolean nested) {
        HashMap<JComponent, List<JComponent>> retVal
                = new HashMap<JComponent, List<JComponent>>();
        for (JComponent component : getDescendantsOfType(JComponent.class,
                container, false)) {
            if (!retVal.containsKey(container)) {
                retVal.put(container,
                        new ArrayList<JComponent>());
            }
            retVal.get(container).add(component);
            if (nested) {
                retVal.putAll(getComponentMap(component, nested));
            }
        }
        return retVal;
    }

    /**
     * Convenience method for retrieving a subset of the UIDefaults pertaining
     * to a particular class.
     *
     * @param clazz the class of interest
     * @return the UIDefaults of the class
     */
    public static UIDefaults getUIDefaultsOfClass(Class clazz) {
        String name = clazz.getName();
        name = name.substring(name.lastIndexOf(".") + 2);
        return getUIDefaultsOfClass(name);
    }

    /**
     * Convenience method for retrieving a subset of the UIDefaults pertaining
     * to a particular class.
     *
     * @param className fully qualified name of the class of interest
     * @return the UIDefaults of the class named
     */
    public static UIDefaults getUIDefaultsOfClass(String className) {
        UIDefaults retVal = new UIDefaults();
        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        List<?> listKeys = Collections.list(defaults.keys());
        for (Object key : listKeys) {
            if (key instanceof String && ((String) key).startsWith(className)) {
                String stringKey = (String) key;
                String property = stringKey;
                if (stringKey.contains(".")) {
                    property = stringKey.substring(stringKey.indexOf(".") + 1);
                }
                retVal.put(property, defaults.get(key));
            }
        }
        return retVal;
    }

    /**
     * Convenience method for retrieving the UIDefault for a single property of
     * a particular class.
     *
     * @param clazz the class of interest
     * @param property the property to query
     * @return the UIDefault property, or null if not found
     */
    public static Object getUIDefaultOfClass(Class clazz, String property) {
        Object retVal = null;
        UIDefaults defaults = getUIDefaultsOfClass(clazz);
        List<Object> listKeys = Collections.list(defaults.keys());
        for (Object key : listKeys) {
            if (key.equals(property)) {
                return defaults.get(key);
            }
            if (key.toString().equalsIgnoreCase(property)) {
                retVal = defaults.get(key);
            }
        }
        return retVal;
    }

    /**
     * Exclude methods that return values that are meaningless to the user
     */
    static Set<String> setExclude = new HashSet<String>();

    static {
        setExclude.add("getFocusCycleRootAncestor");
        setExclude.add("getAccessibleContext");
        setExclude.add("getColorModel");
        setExclude.add("getGraphics");
        setExclude.add("getGraphicsConfiguration");
    }

    /**
     * Convenience method for obtaining most non-null human readable properties
     * of a JComponent. Array properties are not included.
     * <P>
     * Implementation note: The returned value is a HashMap. This is subject to
     * change, so callers should code against the interface Map.
     *
     * @param component the component whose properties are to be determined
     * @return the class and value of the properties
     */
    public static Map<Object, Object> getProperties(JComponent component) {
        Map<Object, Object> retVal = new HashMap<Object, Object>();
        Class<?> clazz = component.getClass();
        Method[] methods = clazz.getMethods();
        Object value = null;
        for (Method method : methods) {
            if (method.getName().matches("^(is|get).*")
                    && method.getParameterTypes().length == 0) {
                try {
                    Class returnType = method.getReturnType();
                    if (returnType != void.class
                            && !returnType.getName().startsWith("[")
                            && !setExclude.contains(method.getName())) {
                        String key = method.getName();
                        value = method.invoke(component);
                        if (value != null && !(value instanceof Component)) {
                            retVal.put(key, value);
                        }
                    }
                    // ignore exceptions that arise if the property could not be accessed
                } catch (IllegalAccessException ex) {
                } catch (IllegalArgumentException ex) {
                } catch (InvocationTargetException ex) {
                }
            }
        }
        return retVal;
    }

    /**
     * Convenience method to obtain the Swing class from which this component
     * was directly or indirectly derived.
     *
     * @param component The component whose Swing superclass is to be determined
     * @return The nearest Swing class in the inheritance tree
     * @param <T> type
     */
    public static <T extends JComponent> Class getJClass(T component) {
        Class<?> clazz = component.getClass();
        while (!clazz.getName().matches("javax.swing.J[^.]*$")) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

}

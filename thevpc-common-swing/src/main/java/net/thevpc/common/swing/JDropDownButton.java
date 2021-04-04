/**
 * ====================================================================
 * vpc-swingext library
 * <p>
 * Description: <start><end>
 * <br>
 *
 * Copyright [2020] [thevpc] Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br> ====================================================================
 */
package net.thevpc.common.swing;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 13 juil. 2006
 * 22:14:21
 */
public class JDropDownButton extends JButton {

    /*
     * Diagnostic aids -- should be false for production builds.
     */
    private static final boolean TRACE = false; // trace creates and disposes
    private static final boolean VERBOSE = false; // show reuse hits/misses
    private static final boolean DEBUG = false;  // show bad params, misc.
    /*
     * Registry of listeners created for <code>Action-JMenuItem</code> linkage.
     * This is needed so that references can be cleaned up at remove time to
     * allow garbage collection Default is <code>null</code>.
     */
    private static Map listenerRegistry = null;
    protected List<ActionListener> quickListeners = new ArrayList();
    /**
     * The window-closing listener for the popup.
     *
     * @see WinListener
     */
    protected WinListener popupListener;
    //    public static void main(String[] args) {
//        boolean nativeLAF = true;
//        try {
//            String plaf;
//            if (nativeLAF) {
//                plaf = UIManager.getSystemLookAndFeelClassName();
//            } else {
//                plaf = UIManager.getCrossPlatformLookAndFeelClassName();
//            }
//            UIManager.setLookAndFeel(plaf);
//        } catch (Exception e) {
//            System.out.println("Error loading Look and Feel");
//        }
//        JFrame frame = new JFrame();
//        JDropDownButton button = new JDropDownButton();
//        button.add("test1");
//        button.add("test2");
//        button.add("test3");
//        //JButton button=new JButton();
//        //button.setText("Toto");
//        //button.setPreferredSize(new Dimension(20,20));
//        //button.setMinimumSize(new Dimension(20,20));
//        button.setIcon(new ImageIcon("D:/Personal/work/app/lib/resources/images/vpc/application/CalculatorAction.gif"));
//        frame.getContentPane().setLayout(new BorderLayout());
//        frame.getContentPane().add(new JLabel("..."), BorderLayout.PAGE_START);
//        Box box = Box.createHorizontalBox();
//        box.add(Box.createHorizontalGlue());
//        box.add(button);
//        box.add(Box.createHorizontalGlue());
//        frame.getContentPane().add(box);
//        JToolBar toolbar = new JToolBar();
//
//        JDropDownButton b1 = new JDropDownButton();
////		b1.setIcon(new ImageIcon("D:/Personal/work/app/lib/resources/images/vpc/application/CalculatorAction.gif"));
//        b1.add("test1");
//        b1.add("test2");
//        b1.add("test3");
//        JDropDownButton b2 = new JDropDownButton();
////		b2.setIcon(new ImageIcon("D:/Personal/work/app/lib/resources/images/vpc/application/CalculatorAction.gif"));
//        b2.add("test1");
//        b2.add("test2");
//        b2.add("test3");
//        toolbar.add(b1);
//        toolbar.add(b2);
//        toolbar.add(new JButton("titi"));
//        frame.getContentPane().add(toolbar, BorderLayout.PAGE_END);
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.show();
//    }
    /*
     * The popup menu portion of the menu.
     */
    private JPopupMenu popupMenu;
    private boolean paintHandle = true;
    /*
     * The button's model listeners. Default is <code>null</code>.
     */
    private ChangeListener menuChangeListener = null;
    /*
     * Only one <code>MenuEvent</code> is needed for each menu since the event's
     * only state is the source property. The source of events generated is
     * always "this". Default is <code>null</code>.
     */
    private MenuEvent menuEvent = null;
    /*
     * Used by the look and feel (L&F) code to handle implementation specific
     * menu behaviors.
     */
    private int delay;
    private int quickActionDelay = 200;
    /*
     * Location of the popup component. Location is <code>null</code> if it was
     * not customized by <code>setMenuLocation</code>
     */
    private Point customMenuLocation = null;
    private int popupOrientation;

    public JDropDownButton() {
        super();
        prepareButton();
    }

    public JDropDownButton(Action a) {
        super(a);
        prepareButton();
    }

    public JDropDownButton(Icon icon) {
        super(icon);
        prepareButton();
    }

    public JDropDownButton(String text) {
        super(text);
        prepareButton();
    }

    public JDropDownButton(String text, Icon icon) {
        super(text, icon);
        prepareButton();
    }

    public int getQuickActionDelay() {
        return quickActionDelay;
    }

    public void setQuickActionDelay(int quickActionDelay) {
        this.quickActionDelay = quickActionDelay;
    }

    public boolean isPaintHandle() {
        return paintHandle;
    }

    public JDropDownButton setPaintHandle(boolean paintHandle) {
        this.paintHandle = paintHandle;
        return this;
    }

    protected void installAncestorListener() {
        addAncestorListener(new AncestorListener() {

            public void ancestorAdded(AncestorEvent event) {
                hidePopup();
            }

            public void ancestorRemoved(AncestorEvent event) {
                hidePopup();
            }

            public void ancestorMoved(AncestorEvent event) {
                if (event.getSource() != JDropDownButton.this) {
                    hidePopup();
                }
            }
        });
    }

    /**
     * Causes the combo box to close its popup window.
     */
    public void hidePopup() {
        setPopupMenuVisible(false);
    }

    public JMenuItem getTreeMenuItemByActionCommand(String type) {
        for (int i = getItemCount() - 1; i >= 0; i--) {
            JMenuItem m = getItem(i);
            if (m != null) {
                if (type.equals(m.getActionCommand())) {
                    return (JMenuItem) m;
                } else if (m instanceof JMenu) {
                    JMenuItem x = getLeafMenuItem(type, (JMenu) m);
                    if (x != null) {
                        return x;
                    }
                }
            }
        }
        return null;
    }

    private JMenuItem getLeafMenuItem(String type, JMenu parent) {
        for (int i = parent.getItemCount() - 1; i >= 0; i--) {
            Component c = parent.getItem(i);
            if (c instanceof JMenu) {
                return getLeafMenuItem(type, (JMenu) c);
            } else if (c instanceof JMenuItem) {
                if (type.equals(((JMenuItem) c).getActionCommand())) {
                    return (JMenuItem) c;
                }
            } else {
                //
            }
        }
        return null;
    }

    private void prepareButton() {
        popupOrientation = SwingConstants.LEFT;
        installAncestorListener();
        addMouseListener(
                new MouseAdapter() {

            private long time;
//                    public void mouseClicked(MouseEvent e) {
//                        long d=System.currentTimeMillis()-e.getWhen();
//                        if((d) >= getDelay()){
//                            setPopupMenuVisible(true);
//                            MenuElement me[] = buildMenuElementArray(JDropDownButton.this);
//                            MenuSelectionManager.defaultManager().setSelectedPath(me);
//                        }else{
//                            System.out.println("NOT_SHOWN : d="+d+"<"+getDelay());
//                        }
//                    }

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    time = e.getWhen();
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    long d = e.getWhen() - time;
                    time = 0;
//                            System.out.println("d /delay= " + d+" / "+getDelay());
                    if ((d) > getQuickActionDelay()) {
//                                System.out.println("popup ok");
                        setPopupMenuVisible(true);
                        MenuElement[] me = buildMenuElementArray(JDropDownButton.this);
                        MenuSelectionManager.defaultManager().setSelectedPath(me);
                    } else {
//                                System.out.println("quick action");
                        ActionEvent ae = new ActionEvent(JDropDownButton.this,
                                ActionEvent.ACTION_PERFORMED,
                                getActionCommand(),
                                e.getWhen(),
                                e.getModifiers());
                        fireQuickActionPerformed(ae);
                    }
                }
            }
        });
    }

    public void addQuickActionListener(ActionListener listener) {
        if (!quickListeners.contains(listener)) {
            quickListeners.add(listener);
        }
    }

    public void removeQuickActionListener(ActionListener listener) {
        quickListeners.remove(listener);
    }

    protected void fireQuickActionPerformed(ActionEvent event) {
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = quickListeners.size() - 1; i >= 0; i--) {
            ((ActionListener) quickListeners.get(i)).actionPerformed(event);
        }
    }

    public int getPopupOrientation() {
        return popupOrientation;
    }

    /**
     * @param orientation : SwingConstants.LEFT or SwingConstants.RIGHT
     */
    public void setPopupOrientation(int orientation) {
        if (orientation != SwingConstants.RIGHT && orientation != SwingConstants.LEFT) {
            throw new RuntimeException("expected SwingConstants.LEFT or SwingConstants.RIGHT for popup popupOrientation");
        }
        popupOrientation = orientation;
    }

    public void paintHandle(Graphics g) {
        Color shadow = UIManager.getColor("controlShadow");
        Color darkShadow = UIManager.getColor("controlDkShadow");
        Color highlight = UIManager.getColor("controlLtHighlight");
        int direction = SOUTH;

        Color origColor;
        boolean isPressed, isEnabled;
        int w, h, size;

        w = getSize().width;
        h = getSize().height;
        origColor = g.getColor();
        isPressed = getModel().isPressed();
        isEnabled = isEnabled();

        //g.setColor(getBackground());
        //g.fillRect(1, 1, w-2, h-2);
        /// Draw the proper Border
/*
         * if (isPressed) { g.setColor(shadow); g.drawRect(0, 0, w-1, h-1); }
         * else { // Using the background color set above g.drawLine(0, 0, 0,
         * h-1); g.drawLine(1, 0, w-2, 0);
         *
         * g.setColor(highlight); // inner 3D border g.drawLine(1, 1, 1, h-3);
         * g.drawLine(2, 1, w-3, 1);
         *
         * g.setColor(shadow); // inner 3D border g.drawLine(1, h-2, w-2, h-2);
         * g.drawLine(w-2, 1, w-2, h-3);
         *
         * g.setColor(darkShadow); // black drop shadow __| g.drawLine(0, h-1,
         * w-1, h-1); g.drawLine(w-1, h-1, w-1, 0);
            }
         */
        // If there's no room to draw arrow, bail
        if (h < 5 || w < 5) {
            g.setColor(origColor);
            return;
        }

        if (isPressed) {
            g.translate(1, 1);
        }

        // Draw the arrow
        size = Math.min((h - 4) / 4, (w - 4) / 4);
//            size = Math.max(size, 2);

        //paintTriangle(g, (w - size) / 2, (h - size) / 2,size, direction, isEnabled);
        String txt = getText();
        Icon icon = getIcon();

        if (txt != null && (txt.length() == 0 || txt.equals(" "))) {
            txt = null;
        }
        if (txt == null && icon == null) {
//            System.out.println("a");
            int rest = w;
            size = Math.min(size, Math.min(rest, 5));
            paintTriangle(g, ((w - size) / 2), ((h - size) / 2), size, direction, isEnabled);
        } else if (txt != null && icon == null) {
            int rest = (w - (int) getFont().getStringBounds(txt, ((Graphics2D) g).getFontRenderContext()).getWidth()) / 2;
//            System.out.println("b rest="+rest+" ; size="+size+" ==> "+Math.min(size, Math.min(rest,4)));
            size = Math.min(size, Math.min(rest, 5));
            if (rest < 7) {
                paintTriangle(g, (w - 5 - size), (h - 2 - size), size, direction, isEnabled);
            } else {
                paintTriangle(g, (w - 5 - size), ((h - size) / 2), size, direction, isEnabled);
            }
        } else if (txt == null && icon != null) {
//            System.out.println("c");
            int rest = (w - icon.getIconWidth()) / 2;
            size = Math.min(size, Math.min(rest, 5));
            if (rest < 7) {
                paintTriangle(g, (w - 5 - size), (h - 2 - size), size, direction, isEnabled);
            } else {
                paintTriangle(g, (w - 5 - size), ((h - size) / 2), size, direction, isEnabled);
            }

        } else /*
             * (txt!=null && icon!=null)
         */ {
//            System.out.println("d");
            int rest = (w - (int) getFont().getStringBounds(txt, ((Graphics2D) g).getFontRenderContext()).getWidth() - icon.getIconWidth()) / 3;
            size = Math.min(size, Math.min(rest, 5));
            if (rest < 7) {
                paintTriangle(g, (w - 5 - size), (h - 2 - size), size, direction, isEnabled);
            } else {
                paintTriangle(g, (w - 5 - size), ((h - size) / 2), size, direction, isEnabled);
            }
        }

        // Reset the Graphics back to it's original settings
        if (isPressed) {
            g.translate(-1, -1);
        }
        g.setColor(origColor);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isPaintHandle()) {
            paintHandle(g);
        }
    }

    public void paintTriangle(Graphics g, int x, int y, int size, int direction, boolean isEnabled) {
        Color shadow = UIManager.getColor("controlShadow");
        Color darkShadow = UIManager.getColor("controlDkShadow");
        Color highlight = UIManager.getColor("controlLtHighlight");
        Color oldColor = g.getColor();
        int mid, i, j;

        j = 0;
        size = Math.max(size, 2);
        mid = (size / 2) - 1;

        g.translate(x, y);
        if (isEnabled) {
            g.setColor(darkShadow);
        } else {
            g.setColor(shadow);
        }

        switch (direction) {
            case NORTH:
                for (i = 0; i < size; i++) {
                    g.drawLine(mid - i, i, mid + i, i);
                }
                if (!isEnabled) {
                    g.setColor(highlight);
                    g.drawLine(mid - i + 2, i, mid + i, i);
                }
                break;
            case SOUTH:
                if (!isEnabled) {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(mid - i, j, mid + i, j);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(shadow);
                }

                j = 0;
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(mid - i, j, mid + i, j);
                    j++;
                }
                break;
            case WEST:
                for (i = 0; i < size; i++) {
                    g.drawLine(i, mid - i, i, mid + i);
                }
                if (!isEnabled) {
                    g.setColor(highlight);
                    g.drawLine(i, mid - i + 2, i, mid + i);
                }
                break;
            case EAST:
                if (!isEnabled) {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for (i = size - 1; i >= 0; i--) {
                        g.drawLine(j, mid - i, j, mid + i);
                        j++;
                    }
                    g.translate(-1, -1);
                    g.setColor(shadow);
                }

                j = 0;
                for (i = size - 1; i >= 0; i--) {
                    g.drawLine(j, mid - i, j, mid + i);
                    j++;
                }
                break;
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (popupMenu != null) {
            SwingUtilities.updateComponentTreeUI(popupMenu);
            //popupMenu.updateUI();
        }
    }

    /**
     * Returns true if the menu's popup window is visible.
     *
     * @return true if the menu is visible, else false
     */
    public boolean isPopupMenuVisible() {
        ensurePopupMenuCreated();
        return popupMenu.isVisible();
    }

    /**
     * Sets the visibility of the menu's popup. If the menu is not enabled, this
     * method will have no effect.
     *
     * @param b a boolean value -- true to make the menu visible, false to hide
     * it @@beaninfo description: The popup menu's visibility expert: true
     * hidden: true
     */
    public void setPopupMenuVisible(boolean b) {
        if (DEBUG) {
            System.out.println("in JMenu.setPopupMenuVisible " + b);
            // Thread.dumpStack();
        }

        boolean isVisible = isPopupMenuVisible();
        if (b != isVisible && (isEnabled() || !b)) {

            ensurePopupMenuCreated();
            if ((b == true) && isShowing()) {
                // Set location of popupMenu (pulldown or pullright)
                Point p = getCustomMenuLocation();
                if (p == null) {
                    p = getPopupMenuOrigin();
                }

                getPopupMenu().show(this, p.x, p.y);
            } else {
                getPopupMenu().setVisible(false);
            }
        }

    }

    /**
     * Computes the origin for the <code>JMenu</code>'s popup menu. This method
     * uses Look and Feel properties named <code>Menu.menuPopupOffsetX</code>,
     * <code>Menu.menuPopupOffsetY</code>,
     * <code>Menu.submenuPopupOffsetX</code>, and
     * <code>Menu.submenuPopupOffsetY</code> to adjust the exact location of
     * popup.
     *
     * @return a <code>Point</code> in the coordinate space of the menu which
     * should be used as the origin of the <code>JMenu</code>'s popup menu
     * @since 1.3
     */
    protected Point getPopupMenuOrigin() {
        int x = 0;
        int y = 0;
        JPopupMenu pm = getPopupMenu();
        // Figure out the sizes needed to caclulate the menu position
        Dimension screenSize;
        Dimension s = getSize();
        Dimension pmSize = pm.getSize();
        // For the first time the menu is popped up,
        // the size has not yet been initiated
        if (pmSize.width == 0) {
            pmSize = pm.getPreferredSize();
        }
        Point position = getLocationOnScreen();
        GraphicsConfiguration gc = getGraphicsConfiguration();
        if (gc == null) {
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        } else {
            Rectangle sBounds = gc.getBounds();

            screenSize = sBounds.getSize();
            position.x -= sBounds.x;
            position.y -= sBounds.y;
        }

        Container parent = getParent();
        if (parent instanceof JPopupMenu) {
            // We are a submenu (pull-right)
            int xOffset = UIManager.getInt("Menu.submenuPopupOffsetX");
            int yOffset = UIManager.getInt("Menu.submenuPopupOffsetY");

            if ((this.getComponentOrientation().isLeftToRight() && getPopupOrientation() == SwingConstants.LEFT)
                    || (!this.getComponentOrientation().isLeftToRight() && getPopupOrientation() != SwingConstants.LEFT)) {
                // First determine x:
                x = s.width + xOffset;   // Prefer placement to the right
                if (position.x + x + pmSize.width >= screenSize.width
                        && // popup doesn't fit - place it wherever there's more room
                        screenSize.width - s.width < 2 * position.x) {

                    x = 0 - xOffset - pmSize.width;
                }
            } else {
                // First determine x:
                x = 0 - xOffset - pmSize.width; // Prefer placement to the left
                if (position.x + x < 0
                        && // popup doesn't fit - place it wherever there's more room
                        screenSize.width - s.width > 2 * position.x) {

                    x = s.width + xOffset;
                }
            }
            // Then the y:
            y = yOffset;                     // Prefer dropping down
            if (position.y + y + pmSize.height >= screenSize.height
                    && // popup doesn't fit - place it wherever there's more room
                    screenSize.height - s.height < 2 * position.y) {

                y = s.height - yOffset - pmSize.height;
            }
        } else {
            // We are a toplevel menu (pull-down)
            int xOffset = UIManager.getInt("Menu.menuPopupOffsetX");
            int yOffset = UIManager.getInt("Menu.menuPopupOffsetY");

            if ((this.getComponentOrientation().isLeftToRight() && getPopupOrientation() == SwingConstants.LEFT)
                    || (!this.getComponentOrientation().isLeftToRight() && getPopupOrientation() != SwingConstants.LEFT)) {
                // First determine the x:
                x = xOffset;                   // Extend to the right
                if (position.x + x + pmSize.width >= screenSize.width
                        && // popup doesn't fit - place it wherever there's more room
                        screenSize.width - s.width < 2 * position.x) {

                    x = s.width - xOffset - pmSize.width;
                }
            } else {
                // First determine the x:
                x = s.width - xOffset - pmSize.width; // Extend to the left
                if (position.x + x < 0
                        && // popup doesn't fit - place it wherever there's more room
                        screenSize.width - s.width > 2 * position.x) {

                    x = xOffset;
                }
            }
            // Then the y:
            y = s.height + yOffset;    // Prefer dropping down
            if (position.y + y + pmSize.height >= screenSize.height
                    && // popup doesn't fit - place it wherever there's more room
                    screenSize.height - s.height < 2 * position.y) {

                y = 0 - yOffset - pmSize.height;   // Otherwise drop 'up'
            }
        }
        return new Point(x, y);
    }

    /**
     * Returns the suggested delay, in milliseconds, before submenus are popped
     * up or down. Each look and feel (L&#38;F) may determine its own policy for
     * observing the <code>delay</code> property. In most cases, the delay is
     * not observed for top level menus or while dragging. The default for
     * <code>delay</code> is 0. This method is a property of the look and feel
     * code and is used to manage the idiosyncracies of the various UI
     * implementations.
     *
     * @return the <code>delay</code> property
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Sets the suggested delay before the menu's <code>PopupMenu</code> is
     * popped up or down. Each look and feel (L&#35;F) may determine it's own
     * policy for observing the delay property. In most cases, the delay is not
     * observed for top level menus or while dragging. This method is a property
     * of the look and feel code and is used to manage the idiosyncracies of the
     * various UI implementations.
     *
     * @param d the number of milliseconds to delay
     * @throws IllegalArgumentException if <code>d</code> is less than 0 popup
     * menu visible expert: true
     */
    public void setDelay(int d) {
        if (d < 0) {
            throw new IllegalArgumentException("Delay must be a positive integer");
        }

        delay = d;
    }

    private void ensurePopupMenuCreated() {
        Point pt = this.getLocation();
        if (popupMenu == null) {
            /*
             * -- TAHA -- final JMenu thisMenu = this;
             */
            final JDropDownButton thisMenu = this;

            this.popupMenu = new JPopupMenu();
            SwingUtilities3.applyOrientation(popupMenu);
            popupMenu.setInvoker(this);
            popupListener = createWinListener(popupMenu);
            popupMenu.addPopupMenuListener(new PopupMenuListener() {

                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                }

                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                public void popupMenuCanceled(PopupMenuEvent e) {
                    fireMenuCanceled();
                }
            });
            popupMenu.getAccessibleContext().setAccessibleParent(this);
        }
    }

    /*
     * Return the customized location of the popup component.
     */
    private Point getCustomMenuLocation() {
        return customMenuLocation;
    }

    /**
     * Sets the location of the popup component.
     *
     * @param x the x coordinate of the popup's new position
     * @param y the y coordinate of the popup's new position
     */
    public void setMenuLocation(int x, int y) {
        customMenuLocation = new Point(x, y);
        if (popupMenu != null) {
            popupMenu.setLocation(x, y);
        }
    }

    /**
     * Appends a menu item to the end of this menu. Returns the menu item added.
     *
     * @param menuItem the <code>JMenuitem</code> to be added
     * @return the <code>JMenuItem</code> added
     */
    public JMenuItem add(JMenuItem menuItem) {
        AccessibleContext ac = menuItem.getAccessibleContext();
        ac.setAccessibleParent(this);
        ensurePopupMenuCreated();

        popupMenu.add(menuItem);
        SwingUtilities3.applyOrientation(popupMenu);
        return menuItem;
    }

    /**
     * Appends a component to the end of this menu. Returns the component added.
     *
     * @param c the <code>Component</code> to add
     * @return the <code>Component</code> added
     */
    public Component add(Component c) {
        if (c instanceof JComponent) {
            AccessibleContext ac = ((JComponent) c).getAccessibleContext();
            if (ac != null) {
                ac.setAccessibleParent(this);
            }
        }
        ensurePopupMenuCreated();
        popupMenu.add(c);
        SwingUtilities3.applyOrientation(popupMenu);
        return c;
    }

    /**
     * Adds the specified component to this container at the given position. If
     * <code>index</code> equals -1, the component will be appended to the end.
     *
     * @param c the <code>Component</code> to add
     * @param index the position at which to insert the component
     * @return the <code>Component</code> added
     * @see #remove
     * @see java.awt.Container#add(Component, int)
     */
    public Component add(Component c, int index) {
        if (c instanceof JComponent) {
            AccessibleContext ac = ((JComponent) c).getAccessibleContext();
            if (ac != null) {
                ac.setAccessibleParent(this);
            }
        }
        ensurePopupMenuCreated();
        popupMenu.add(c, index);
        SwingUtilities3.applyOrientation(popupMenu);
        return c;
    }

    /**
     * Removes the menu item at the specified index from this menu.
     *
     * @param pos the position of the item to be removed
     * @throws IllegalArgumentException if the value of <code>pos</code> &lt; 0,
     * or if <code>pos</code> is greater than the number of menu items
     */
    public void remove(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        if (pos > getItemCount()) {
            throw new IllegalArgumentException("index greater than the number of items.");
        }
        if (popupMenu != null) {
            popupMenu.remove(pos);
        }
    }

    /**
     * Removes the component <code>c</code> from this menu.
     *
     * @param c the component to be removed
     */
    public void remove(Component c) {
        if (popupMenu != null) {
            popupMenu.remove(c);
        }
    }

    /**
     * Removes all menu items from this menu.
     */
    public void removeAll() {
        if (popupMenu != null) {
            popupMenu.removeAll();
        }
    }

    /**
     * Creates a new menu item with the specified text and appends it to the end
     * of this menu.
     *
     * @param s the string for the menu item to be added
     */
    public JMenuItem add(String s) {
        return add(new JMenuItem(s));
    }

    /**
     * Creates a new menu item attached to the specified <code>Action</code>
     * object and appends it to the end of this menu. As of 1.3, this is no
     * longer the preferred method for adding <code>Actions</code> to a
     * container. Instead it is recommended to configure a control with an
     * action using <code>setAction</code>, and then add that control directly
     * to the <code>Container</code>.
     *
     * @param a the <code>Action</code> for the menu item to be added
     * @see Action
     */
    public JMenuItem add(Action a) {
        JMenuItem mi = createActionComponent(a);
        mi.setAction(a);
        add(mi);
        SwingUtilities3.applyOrientation(popupMenu);
        return mi;
    }

    /**
     * Appends a new separator to the end of the menu.
     */
    public void addSeparator() {
        ensurePopupMenuCreated();
        popupMenu.addSeparator();
        SwingUtilities3.applyOrientation(popupMenu);
    }

    /**
     * Inserts a new menu item with the specified text at a given position.
     *
     * @param s the text for the menu item to add
     * @param pos an integer specifying the position at which to add the new
     * menu item
     * @throws IllegalArgumentException when the value of <code>pos</code> &lt;
     * 0
     */
    public void insert(String s, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JMenuItem(s), pos);
        SwingUtilities3.applyOrientation(popupMenu);
    }

    /**
     * Inserts the specified <code>JMenuitem</code> at a given position.
     *
     * @param mi the <code>JMenuitem</code> to add
     * @param pos an integer specifying the position at which to add the new
     * <code>JMenuitem</code>
     * @return the new menu item
     * @throws IllegalArgumentException if the value of <code>pos</code> &lt; 0
     */
    public JMenuItem insert(JMenuItem mi, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        AccessibleContext ac = mi.getAccessibleContext();
        ac.setAccessibleParent(this);
        ensurePopupMenuCreated();
        popupMenu.insert(mi, pos);
        SwingUtilities3.applyOrientation(popupMenu);
        return mi;
    }

    /**
     * Inserts a new menu item attached to the specified <code>Action</code>
     * object at a given position.
     *
     * @param a the <code>Action</code> object for the menu item to add
     * @param pos an integer specifying the position at which to add the new
     * menu item
     * @throws IllegalArgumentException if the value of <code>pos</code> &lt; 0
     */
    public JMenuItem insert(Action a, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        JMenuItem mi = new JMenuItem((String) a.getValue(Action.NAME),
                (Icon) a.getValue(Action.SMALL_ICON));
        mi.setHorizontalTextPosition(JButton.TRAILING);
        mi.setVerticalTextPosition(JButton.CENTER);
        mi.setEnabled(a.isEnabled());
        mi.setAction(a);
        popupMenu.insert(mi, pos);
        SwingUtilities3.applyOrientation(popupMenu);
        return mi;
    }

    /**
     * Inserts a separator at the specified position.
     *
     * @param index an integer specifying the position at which to insert the
     * menu separator
     * @throws IllegalArgumentException if the value of <code>index</code> &lt;
     * 0
     */
    public void insertSeparator(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JPopupMenu.Separator(), index);
        SwingUtilities3.applyOrientation(popupMenu);
    }

    /**
     * Returns the <code>JMenuItem</code> at the specified position. If the
     * component at <code>pos</code> is not a menu item, <code>null</code> is
     * returned. This method is included for AWT compatibility.
     *
     * @param pos an integer specifying the position
     * @return the menu item at the specified position; or <code>null</code> if
     * the item as the specified position is not a menu item
     * @throws IllegalArgumentException if the value of <code>pos</code> &lt; 0
     */
    public JMenuItem getItem(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        Component c = getMenuComponent(pos);
        if (c instanceof JMenuItem) {
            JMenuItem mi = (JMenuItem) c;
            return mi;
        }

        // 4173633
        return null;
    }

    /**
     * Returns the number of items on the menu, including separators. This
     * method is included for AWT compatibility.
     *
     * @return an integer equal to the number of items on the menu
     * @see #getMenuComponentCount
     */
    public int getItemCount() {
        return getMenuComponentCount();
    }

    /**
     * Removes the specified menu item from this menu. If there is no popup
     * menu, this method will have no effect.
     *
     * @param item the <code>JMenuItem</code> to be removed from the menu
     */
    public void remove(JMenuItem item) {
        if (popupMenu != null) {
            popupMenu.remove(item);
        }
    }

    /**
     * Returns the number of components on the menu.
     *
     * @return an integer containing the number of components on the menu
     */
    public int getMenuComponentCount() {
        int componentCount = 0;
        if (popupMenu != null) {
            componentCount = popupMenu.getComponentCount();
        }
        return componentCount;
    }

    /**
     * Returns the component at position <code>n</code>.
     *
     * @param n the position of the component to be returned
     * @return the component requested, or <code>null</code> if there is no
     * popup menu
     */
    public Component getMenuComponent(int n) {
        if (popupMenu != null) {
            return popupMenu.getComponent(n);
        }

        return null;
    }

    /**
     * Returns an array of <code>Component</code>s of the menu's subcomponents.
     * Note that this returns all <code>Component</code>s in the popup menu,
     * including separators.
     *
     * @return an array of <code>Component</code>s or an empty array if there is
     * no popup menu
     */
    public Component[] getMenuComponents() {
        if (popupMenu != null) {
            return popupMenu.getComponents();
        }

        return new Component[0];
    }

    /**
     * Returns true if the specified component exists in the submenu hierarchy.
     *
     * @param c the <code>Component</code> to be tested
     * @return true if the <code>Component</code> exists, false otherwise
     */
    public boolean isMenuComponent(Component c) {
        // Are we in the MenuItem part of the menu
        if (c == this) {
            return true;
        }
        // Are we in the PopupMenu?
        if (c instanceof JPopupMenu) {
            JPopupMenu comp = (JPopupMenu) c;
            if (comp == this.getPopupMenu()) {
                return true;
            }
        }
        // Are we in a Component on the PopupMenu
        int ncomponents = this.getMenuComponentCount();
        Component[] component = this.getMenuComponents();
        for (int i = 0; i < ncomponents; i++) {
            Component comp = component[i];
            // Are we in the current component?
            if (comp == c) {
                return true;
            }
            // Hmmm, what about Non-menu containers?

            // Recursive call for the Menu case
            if (comp instanceof JMenu) {
                JMenu subMenu = (JMenu) comp;
                if (subMenu.isMenuComponent(c)) {
                    return true;
                }
            }
        }
        return false;
    }


    /*
     * Returns a point in the coordinate space of this menu's popupmenu which
     * corresponds to the point <code>p</code> in the menu's coordinate space.
     *
     * @param p the point to be translated @return the point in the coordinate
     * space of this menu's popupmenu
     */
    private Point translateToPopupMenu(Point p) {
        return translateToPopupMenu(p.x, p.y);
    }

    /*
     * Returns a point in the coordinate space of this menu's popupmenu which
     * corresponds to the point (x,y) in the menu's coordinate space.
     *
     * @param x the x coordinate of the point to be translated @param y the y
     * coordinate of the point to be translated @return the point in the
     * coordinate space of this menu's popupmenu
     */
    private Point translateToPopupMenu(int x, int y) {
        int newX;
        int newY;

        if (getParent() instanceof JPopupMenu) {
            newX = x - getSize().width;
            newY = y;
        } else {
            newX = x;
            newY = y - getSize().height;
        }

        return new Point(newX, newY);
    }

    /**
     * Returns the popupmenu associated with this menu. If there is no
     * popupmenu, it will create one.
     */
    public JPopupMenu getPopupMenu() {
        ensurePopupMenuCreated();
        return popupMenu;
    }

    public JPopupMenu getPopupMenuOrNull() {
        return popupMenu;
    }

    /**
     * Adds a listener for menu events.
     *
     * @param l the listener to be added
     */
    public void addMenuListener(MenuListener l) {
        listenerList.add(MenuListener.class, l);
    }

    /**
     * Removes a listener for menu events.
     *
     * @param l the listener to be removed
     */
    public void removeMenuListener(MenuListener l) {
        listenerList.remove(MenuListener.class, l);
    }

    /**
     * Returns an array of all the <code>MenuListener</code>s added to this
     * JMenu with addMenuListener().
     *
     * @return all of the <code>MenuListener</code>s added or an empty array if
     * no listeners have been added
     * @since 1.4
     */
    public MenuListener[] getMenuListeners() {
        return (MenuListener[]) listenerList.getListeners(MenuListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is created lazily.
     *
     * @throws Error if there is a <code>null</code> listener
     * @see EventListenerList
     */
    protected void fireMenuSelected() {
        if (DEBUG) {
            System.out.println("In JMenu.fireMenuSelected");
        }
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MenuListener.class) {
                if (listeners[i + 1] == null) {
                    throw new Error(getText() + " has a NULL Listener!! " + i);
                } else {
                    // Lazily create the event:
                    if (menuEvent == null) {
                        menuEvent = new MenuEvent(this);
                    }
                    ((MenuListener) listeners[i + 1]).menuSelected(menuEvent);
                }
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is created lazily.
     *
     * @throws Error if there is a <code>null</code> listener
     * @see EventListenerList
     */
    protected void fireMenuDeselected() {
        if (DEBUG) {
            System.out.println("In JMenu.fireMenuDeselected");
        }
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MenuListener.class) {
                if (listeners[i + 1] == null) {
                    throw new Error(getText() + " has a NULL Listener!! " + i);
                } else {
                    // Lazily create the event:
                    if (menuEvent == null) {
                        menuEvent = new MenuEvent(this);
                    }
                    ((MenuListener) listeners[i + 1]).menuDeselected(menuEvent);
                }
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is created lazily.
     *
     * @throws Error if there is a <code>null</code> listener
     * @see EventListenerList
     */
    protected void fireMenuCanceled() {
        if (DEBUG) {
            System.out.println("In JMenu.fireMenuCanceled");
        }
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == MenuListener.class) {
                if (listeners[i + 1] == null) {
                    throw new Error(getText() + " has a NULL Listener!! "
                            + i);
                } else {
                    // Lazily create the event:
                    if (menuEvent == null) {
                        menuEvent = new MenuEvent(this);
                    }
                    ((MenuListener) listeners[i + 1]).menuCanceled(menuEvent);
                }
            }
        }
    }

    private ChangeListener createMenuChangeListener() {
        return new MenuChangeListener();
    }

    /**
     * Creates a window-closing listener for the popup.
     *
     * @param p the <code>JPopupMenu</code>
     * @return the new window-closing listener
     * @see WinListener
     */
    protected WinListener createWinListener(JPopupMenu p) {
        return new WinListener(p);
    }

    /**
     * Messaged when the menubar selection changes to activate or deactivate
     * this menu. Overrides <code>JMenuItem.menuSelectionChanged</code>.
     *
     * @param isIncluded true if this menu is active, false if it is not
     */
    public void menuSelectionChanged(boolean isIncluded) {
        if (DEBUG) {
            System.out.println("In JMenu.menuSelectionChanged to " + isIncluded);
        }
        setSelected(isIncluded);
    }

    /**
     * Returns an array of <code>MenuElement</code>s containing the submenu for
     * this menu component. If popup menu is <code>null</code> returns an empty
     * array. This method is required to conform to the <code>MenuElement</code>
     * interface. Note that since <code>JSeparator</code>s do not conform to the
     * <code>MenuElement</code> interface, this array will only contain
     * <code>JMenuItem</code>s.
     *
     * @return an array of <code>MenuElement</code> objects
     */
    public MenuElement[] getSubElements() {
        if (popupMenu == null) {
            return new MenuElement[0];
        } else {
            MenuElement[] result = new MenuElement[1];
            result[0] = popupMenu;
            return result;
        }
    }

    /**
     * Returns the <code>java.awt.Component</code> used to paint this
     * <code>MenuElement</code>. The returned component is used to convert
     * events and detect if an event is inside a menu component.
     */
    public Component getComponent() {
        return this;
    }

    /**
     * Programmatically performs a "click". This overrides the method
     * <code>AbstractButton.doClick</code> in order to make the menu pop up.
     *
     * @param pressTime indicates the number of milliseconds the button was
     * pressed for
     */
    public void doClick(int pressTime) {
        super.doClick(pressTime);
        System.out.println("???");
        if ((pressTime) >= getQuickActionDelay()) {
            setPopupMenuVisible(true);
            MenuElement[] me = buildMenuElementArray(JDropDownButton.this);
            MenuSelectionManager.defaultManager().setSelectedPath(me);
        } else {
            ActionEvent ae = new ActionEvent(JDropDownButton.this,
                    ActionEvent.ACTION_PERFORMED,
                    getActionCommand(),
                    0,
                    0);
            fireQuickActionPerformed(ae);
        }
    }

    // implements javax.swing.MenuElement

    /*
     * Build an array of menu elements - from <code>PopupMenu</code> to the root
     * <code>JMenuBar</code>. @param leaf the leaf node from which to start
     * building up the array @return the array of menu items
     */
    private MenuElement[] buildMenuElementArray(/*
     * -- TAHA --
             */JDropDownButton leaf) {
        List elements = new ArrayList();
        Component current = leaf.getPopupMenu();
        JPopupMenu pop;
        /*
         * taha JMenu
         */
        Object menu;
        JMenuBar bar;

        while (true) {
            if (current instanceof JPopupMenu) {
                pop = (JPopupMenu) current;
                elements.add(0, pop);
                current = pop.getInvoker();
            } else if (current instanceof JMenu) {
                menu = (JMenu) current;
                elements.add(0, menu);
                current = ((JMenu) menu).getParent();
            } else if (current instanceof JDropDownButton) {
                menu = (JDropDownButton) current;
                //elements.insertElementAt(menu, 0);
                current = ((JDropDownButton) menu).getParent();
            } else if (current instanceof JMenuBar) {
                bar = (JMenuBar) current;
                elements.add(0, bar);
                MenuElement[] me = new MenuElement[elements.size()];
                elements.toArray(me);
                return me;
            } else {
                MenuElement[] me = new MenuElement[elements.size()];
                //System.out.println ("elements.size()"+elements.size());
                elements.toArray(me);
                return me;
                //break;
            }
        }
    }

    /**
     * Sets the <code>ComponentOrientation</code> property of this menu and all
     * components contained within it. This includes all components returned by
     * {@link #getMenuComponents getMenuComponents}.
     *
     * @param orientation the new component orientation of this menu and the
     * components contained within it.
     * @exception NullPointerException if <code>orientation</code> is null.
     * @see java.awt.Component#setComponentOrientation
     * @see java.awt.Component#getComponentOrientation
     * @since 1.4
     */
    /**
     * Factory method which creates the <code>JMenuItem</code> for
     * <code>Action</code>s added to the <code>JMenu</code>. As of 1.3, this is
     * no longer the preferred method. Instead it is recommended to configure a
     * control with an action using <code>setAction</code>, and then adding that
     * control directly to the <code>Container</code>.
     *
     * @param a the <code>Action</code> for the menu item to be added
     * @return the new menu item
     * @see Action
     * @since 1.3
     */
    protected JMenuItem createActionComponent(Action a) {
        JMenuItem mi = new JMenuItem((String) a.getValue(Action.NAME),
                (Icon) a.getValue(Action.SMALL_ICON)) {

            protected PropertyChangeListener createActionPropertyChangeListener(Action a) {
                PropertyChangeListener pcl = createActionChangeListener(this);
                if (pcl == null) {
                    pcl = super.createActionPropertyChangeListener(a);
                }
                return pcl;
            }
        };
        mi.setHorizontalTextPosition(JButton.TRAILING);
        mi.setVerticalTextPosition(JButton.CENTER);
        mi.setEnabled(a.isEnabled());
        return mi;
    }

    /**
     * Returns a properly configured <code>PropertyChangeListener</code> which
     * updates the control as changes to the <code>Action</code> occur. As of
     * 1.3, this is no longer the preferred method for adding
     * <code>Action</code>s to a <code>Container</code>. Instead it is
     * recommended to configure a control with an action using
     * <code>setAction</code>, and then add that control directly to the
     * <code>Container</code>.
     */
    protected PropertyChangeListener createActionChangeListener(JMenuItem b) {
        return new ActionChangedListener(b);
    }

    class MenuChangeListener implements ChangeListener, Serializable {

        boolean isSelected = false;

        public void stateChanged(ChangeEvent e) {
            ButtonModel model = (ButtonModel) e.getSource();
            boolean modelSelected = model.isSelected();

            if (modelSelected != isSelected) {
                if (modelSelected == true) {
                    fireMenuSelected();
                } else {
                    fireMenuDeselected();
                }
                isSelected = modelSelected;
            }
        }
    }

    /**
     * A listener class that watches for a popup window closing. When the popup
     * is closing, the listener deselects the menu.
     * <p>
     * <strong>Warning:</strong> Serialized objects of this class will not be
     * compatible with future Swing releases. The current serialization support
     * is appropriate for short term storage or RMI between applications running
     * the same version of Swing. As of 1.4, support for long term storage of
     * all JavaBeans<sup><font size="-2">TM</font></sup> has been added to the
     * <code>java.beans</code> package. Please see
     * {@link java.beans.XMLEncoder}.
     */
    protected class WinListener extends WindowAdapter implements Serializable {

        JPopupMenu popupMenu;

        /**
         * Create the window listener for the specified popup.
         */
        public WinListener(JPopupMenu p) {
            this.popupMenu = p;
        }

        /**
         * Deselect the menu when the popup is closed from outside.
         */
        public void windowClosing(WindowEvent e) {
            setSelected(false);
        }
    }

    private class ActionChangedListener implements PropertyChangeListener {

        WeakReference menuItem;

        ActionChangedListener(JMenuItem mi) {
            super();
            setTarget(mi);
        }

        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            JMenuItem mi = (JMenuItem) getTarget();
            if (mi == null) {
                Action action = (Action) e.getSource();
                action.removePropertyChangeListener(this);
            } else {
                if (propertyName.equals(Action.NAME)) {
                    String text = (String) e.getNewValue();
                    mi.setText(text);
                } else if (propertyName.equals("enabled")) {
                    Boolean enabledState = (Boolean) e.getNewValue();
                    mi.setEnabled(enabledState.booleanValue());
                } else if (propertyName.equals(Action.SMALL_ICON)) {
                    Icon icon = (Icon) e.getNewValue();
                    mi.setIcon(icon);
                    mi.invalidate();
                    mi.repaint();
                } else if (propertyName.equals(Action.ACTION_COMMAND_KEY)) {
                    mi.setActionCommand((String) e.getNewValue());
                }
            }
        }

        public JMenuItem getTarget() {
            return (JMenuItem) menuItem.get();
        }

        public void setTarget(JMenuItem b) {
            menuItem = new WeakReference(b);
        }
    }
//    		protected void fireActionPerformed(ActionEvent event) {
//		    	setPopupMenuVisible(true);
//				MenuElement me[] = buildMenuElementArray(this);
//				MenuSelectionManager.defaultManager().setSelectedPath(me);
//    			super.fireActionPerformed(event);
//    		}
}

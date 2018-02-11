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
package net.vpc.common.swings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public class SwingUtilities3 {

    public static JWindow createSplashScreen(Component splash, boolean closeOnClick, long timeOut) {
        JWindow window = new JWindow();
        javax.swing.border.Border border = BorderFactory.createEtchedBorder();
        JPanel panel = new JPanel();
        panel.setBorder(border);
        panel.add(splash);
        if (closeOnClick) {
            panel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    Window w = (Window) SwingUtilities.getAncestorOfClass(Window.class, e.getComponent());
                    w.setVisible(false);
                }
            });
        }

        if (timeOut > 0) {
            final JWindow finalWindow = window;
            java.util.Timer timer = new java.util.Timer(true);
            timer.schedule(new TimerTask() {

                public void run() {
                    finalWindow.setVisible(false);
                }
            }, new Date(new Date().getTime() + timeOut));
        }
        window.getContentPane().add(panel);
        window.pack();
        Dimension paneSize = window.getSize();
        Dimension screenSize = window.getToolkit().getScreenSize();
        window.setLocation((screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2);
        return window;
    }

    public static Container[] getAncestors(Component comp) {
        ArrayList<Container> all = new ArrayList<Container>();
        if (comp != null) {
            Container p = comp.getParent();
            while (p != null) {
                all.add(0,p);
                p = p.getParent();
            }
        }
        return (Container[]) all.toArray(new Container[all.size()]);
    }

    public static Point getScreenCentredPosition(Component comp) {
        Dimension paneSize = comp.getSize();
        Dimension screenSize = comp.getToolkit().getScreenSize();
        return new Point((screenSize.width - paneSize.width) / 2, (screenSize.height - paneSize.height) / 2);
    }

    public static JWindow createSplashScreen(String imageResource, boolean closeOnClick, long timeout) {
        URL resource = SwingUtilities3.class.getResource(imageResource);
        ImageIcon icon = resource == null ? null : new ImageIcon(resource);
        if (icon == null) {
            return createSplashScreen(new JLabel("Launching application..."), closeOnClick, timeout);
        } else {
            return createSplashScreen(new JLabel(icon), closeOnClick, timeout);
        }

    }

    public static void expandAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    public static void collapseAll(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.collapseRow(row);
            row++;
        }
    }

    public static String getDefaultNativeOpenFileCommand() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            if (os.contains("95")) {
                return "command.com /C %f";
            } else {
                return "cmd.exe /C %f";
            }
        } else if (System.getenv("DESKTOP_LAUNCH") != null) {
            return System.getenv("DESKTOP_LAUNCH") + " %f";
        } else {
            return "kfmclient exec %f";
        }
    }

    public static java.lang.Process openFile(File file) throws IOException {
        return openFile(file, null);
    }

    public static java.lang.Process openFile(File file, String cmd) throws IOException {
        return openURL(file.toURL(), cmd);
    }

    public static java.lang.Process openURL(URL url) throws IOException {
        return openURL(url, null);
    }

    public static java.lang.Process openURL(URL url, String cmd) throws IOException {
        if (cmd == null || cmd.equalsIgnoreCase("system")) {
            cmd = getDefaultNativeOpenFileCommand();
        }
        String __f = url.getFile();
        String __u = url.toString();
        if (cmd.indexOf("%f") < 0) {
            cmd = cmd + " %f";
        }
        StringTokenizer st = new StringTokenizer(cmd);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
            if (cmdarray[i].equals("%f")) {
                cmdarray[i] = __f;
            } else if (cmdarray[i].equals("%u")) {
                cmdarray[i] = __u;
            }
        }
        return Runtime.getRuntime().exec(cmdarray);
    }

    public static void applyOrientation(Component c) {
        applyOrientation(c, null);
    }

    private static void applyOrientation(Component c, ComponentOrientation o) {
        if (o == null) {
            o = ComponentOrientation.getOrientation(Locale.getDefault());
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
            applyOrientation(s.getPopupMenu(), o);
        }

        c.setComponentOrientation(o);
        if (c instanceof Container) {
            Component[] cc = ((Container) c).getComponents();
            for (Component child : cc) {
                applyOrientation(child, o);
            }
        }
    }

    public static Container getAncestorOfClass(Class[] classes, Component comp) {
        if (comp == null || classes == null) {
            return null;
        }
        Container parent = comp.getParent();
        for (boolean found = false; parent != null && !found;) {
            for (Class aClass : classes) {
                if (!aClass.isInstance(parent)) {
                    continue;
                }
                found = true;
                break;
            }

            if (!found) {
                parent = parent.getParent();
            }
        }

        return parent;
    }

    public static boolean isAncestorDialog(Component component) {
        return getDialogAncestor(component) != null;
    }

    public static Dialog getDialogAncestor(Component component) {
        return (Dialog) SwingUtilities.getAncestorOfClass(Dialog.class, component);
    }

    public static boolean isAncestorFrame(Component component) {
        return getDialogAncestor(component) != null;
    }

    public static Frame getFrameAncestor(Component component) {
        return (Frame) SwingUtilities.getAncestorOfClass(Frame.class, component);
    }

    public static JLabel createTitleLabel(String text) {
        return createTitleLabel(text, Font.BOLD | Font.ITALIC, 1.4F);
    }

    public static JLabel createTitleLabel(String text, int fontStyle, float sizeCoeff) {
        JLabel titleLabel = new JLabel(text != null ? text : "");
        titleLabel.setBackground((Color) UIManager.getDefaults().get("TextArea.inactiveForeground"));
        titleLabel.setForeground((Color) UIManager.getDefaults().get("TextArea.background"));
        Font font = UIManager.getFont("TextField.font");
        font = font.deriveFont(fontStyle < 0 ? font.getStyle() : fontStyle, (sizeCoeff <= 0 || sizeCoeff == 1.0f) ? (float) font.getSize() : (float) font.getSize() * sizeCoeff);
        titleLabel.setFont(font);
        titleLabel.setOpaque(true);
        return titleLabel;
    }

    public static double getSplitPaneDividerLocationQuotient(JSplitPane p) {
        if (p.getOrientation() == JSplitPane.VERTICAL_SPLIT) {
            double dividerLocation = p.getDividerLocation();
            double height = p.getHeight();
            double dividerSize = p.getDividerSize();
            return dividerLocation / (height - dividerSize);
        } else {
            double dividerLocation = p.getDividerLocation();
            double width = p.getWidth();
            double dividerSize = p.getDividerSize();
            return dividerLocation / (width - dividerSize);
        }
    }

    public static Object getAncestorByProperty(String prop, Class type, Component comp) {
        if (comp == null) {
            return null;
        }
        Object w;
        while (comp != null) {
            if (comp instanceof JComponent) {
                w = ((JComponent) comp).getClientProperty(prop);
                if (type.isInstance(w)) {
                    return w;
                }
            }
            comp = comp.getParent();
        }
        return null;
    }

    private SwingUtilities3() {
    }

    public static String getModifiersText(int modifiers) {
        StringBuffer buf = new StringBuffer();

        if ((modifiers & InputEvent.SHIFT_DOWN_MASK) != 0) {
            buf.append("shift ");
        }
        if ((modifiers & InputEvent.CTRL_DOWN_MASK) != 0) {
            buf.append("ctrl ");
        }
        if ((modifiers & InputEvent.META_DOWN_MASK) != 0) {
            buf.append("meta ");
        }
        if ((modifiers & InputEvent.ALT_DOWN_MASK) != 0) {
            buf.append("alt ");
        }
        if ((modifiers & InputEvent.ALT_GRAPH_DOWN_MASK) != 0) {
            buf.append("altGraph ");
        }
        if ((modifiers & InputEvent.BUTTON1_DOWN_MASK) != 0) {
            buf.append("button1 ");
        }
        if ((modifiers & InputEvent.BUTTON2_DOWN_MASK) != 0) {
            buf.append("button2 ");
        }
        if ((modifiers & InputEvent.BUTTON3_DOWN_MASK) != 0) {
            buf.append("button3 ");
        }

        return buf.toString();
    }

    public static String keyStroketoString(KeyStroke keyStroke) {
        if (keyStroke.getKeyCode() == KeyEvent.VK_UNDEFINED) {
            String s = getModifiersText(keyStroke.getModifiers()).toUpperCase();
            if (s.length() > 0) {
                return s + " " + keyStroke.getKeyChar();
            } else {
                return String.valueOf(keyStroke.getKeyChar());
            }
        } else {
            String s = getModifiersText(keyStroke.getModifiers()).toUpperCase();
            if (s.length() > 0) {
                return s + " " + getVKText(keyStroke.getKeyCode());
            } else {
                return getVKText(keyStroke.getKeyCode());
            }
//            return getModifiersText(modifiers) +
//                (onKeyRelease ? "released" : "pressed") + " " +
//                getVKText(keyCode);
        }
    }
    private static Hashtable<Integer, String> vKToText;

    static String getVKText(int keyCode) {
        Integer key = Integer.valueOf(keyCode);
        String name;
        if (vKToText == null) {
            vKToText = new Hashtable<Integer, String>();
        } else {
            name = vKToText.get(key);
            if (name != null) {
                return name.substring(3);
            }
        }
        int expected_modifiers =
                (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);

        Field[] fields = KeyEvent.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getModifiers() == expected_modifiers
                        && fields[i].getType() == Integer.TYPE
                        && fields[i].getName().startsWith("VK_")
                        && fields[i].getInt(KeyEvent.class) == keyCode) {
                    name = fields[i].getName();
                    vKToText.put(key, name);
                    return name.substring(3);
                }
            } catch (IllegalAccessException e) {
                assert (false);
            }
        }
        return "UNKNOWN";
    }

    public static void invokeAndWait(final Runnable doRun)
            throws InterruptedException, InvocationTargetException {
        if (SwingUtilities.isEventDispatchThread()) {
            doRun.run();
        } else {
            EventQueue.invokeAndWait(doRun);
        }
    }

    public static void invokeLater(Runnable doRun) {
        if (SwingUtilities.isEventDispatchThread()) {
            doRun.run();
        } else {
            EventQueue.invokeLater(doRun);
        }
    }
}

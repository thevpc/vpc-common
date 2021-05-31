package net.thevpc.common.swing.win;
import net.thevpc.common.swing.frame.JInternalFrameHelper;
import net.thevpc.common.swing.SwingUtilities3;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InternalWindowsHelper {
    Map<String, JInternalFrame> userWindows = new HashMap<String, JInternalFrame>();
    private List<WindowInfoListener> listeners = new ArrayList<>();
    JDesktopPane desktop;

    public InternalWindowsHelper() {
        this(null);
    }

    public InternalWindowsHelper(JDesktopPane desktop) {
        this.desktop = desktop==null?new JDesktopPane():new JDesktopPane();
        this.desktop.putClientProperty(InternalWindowsHelper.class.getName(),this);
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public JInternalFrame findFrame(String id) {
        return userWindows.get(id);
    }
    
    public JInternalFrame addFrame(WindowInfo fino) {
            JInternalFrame jInternalFrame = new JInternalFrame(fino.getTitle(), fino.isResizable(), fino.isClosable(), fino.isMaximizable(), fino.isIconifiable());
        if(fino.getFrameIcon()!=null) {
            jInternalFrame.setFrameIcon(fino.getFrameIcon());
        }
        JComponent c = (JComponent) fino.getComponent();
        if (c == null) {
            c = new JLabel("...");
        }
        jInternalFrame.add(c);
        Dimension ps = fino.getPreferredSize();
        if (ps != null) {
            jInternalFrame.setPreferredSize(ps);
        }
        c.putClientProperty(JInternalFrame.class,jInternalFrame);
        jInternalFrame.putClientProperty(WindowInfo.class.getName(), fino);
        final String title = jInternalFrame.getTitle();
        jInternalFrame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                userWindows.remove(title);
                JInternalFrame ifr = e.getInternalFrame();
                WindowInfo fino = (WindowInfo) ifr.getClientProperty(WindowInfo.class.getName());
                if (fino != null) {
                    for (WindowInfoListener listener2 : listeners) {
                        listener2.onCloseFrame(fino);
                    }
                }
            }
        });
        userWindows.put(title, jInternalFrame);
        desktop.add(jInternalFrame);
        onPostAddJInternalFrame(new JInternalFrameHelper(jInternalFrame),fino);
        for (WindowInfoListener listener2 : listeners) {
            listener2.onAddFrame(fino);
        }
        return jInternalFrame;
    }

    public void addWindowInfoListener(WindowInfoListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeWindowInfoListener(WindowInfoListener listener) {
        listeners.remove(listener);
    }


    public synchronized void tileFrames() {
        SwingUtilities3.tileFrames(desktop);
    }
    public synchronized void iconifyFrames() {
        SwingUtilities3.iconifyFrames(desktop);
    }
    public synchronized void deiconifyFrames() {
        SwingUtilities3.deiconifyFrames(desktop);
    }
    public synchronized void closeFrames() {
        SwingUtilities3.closeFrames(desktop);
    }

    public synchronized JInternalFrame getWindow(WindowPath path) {
        if (path == null) {
            path = new WindowPath("");
        }
        String[] pp = path.toArray();
        String windowTitle = "";
//        String windowSupTitle = "";
        java.util.List<String> subTitles = new ArrayList<>();
        switch (pp.length) {
            case 0: {
                windowTitle = "DefaultProject";
//                windowSupTitle = "Plot";
                break;
            }
            case 1: {
                windowTitle = pp[0];
//                windowSupTitle = "Plot";
                break;
            }
            default: {
                windowTitle = pp[0];
                for (int i = 1; i < pp.length; i++) {
                    subTitles.add(pp[i]);
                }
                break;
            }
        }
        JInternalFrame internalFrame = userWindows.get(windowTitle);
        if (internalFrame == null) {
            internalFrame = addFrame(new WindowInfo()
                    .setClosable(true)
                    .setIcon(false)
                    .setIconifiable(true)
                    .setResizable(true)
                    .setMaximizable(true)
                    .setTitle(windowTitle)
                    .setComponent(new DummyLabel())
            );
        }
        return internalFrame;
    }

    public synchronized void removeWindow(WindowPath path) {
        String[] strings = path.toArray();
        if (strings.length != 2) {
            throw new IllegalArgumentException("bad");
        }
        JInternalFrame internalFrame = userWindows.get(strings[0]);
        if (internalFrame != null) {
            Component[] components = internalFrame.getContentPane().getComponents();
            JTabbedPane pane;
            if (components.length == 0) {
                //
            } else if (components[0] instanceof DummyLabel) {
                //
            } else {
                pane = (JTabbedPane) components[0];
                for (int i = 0; i < pane.getTabCount(); i++) {
                    String titleAt = pane.getTitleAt(i);
                    if (titleAt.equals(strings[1])) {
                        pane.removeTabAt(i);
                        return;
                    }
                }
            }
        }
    }
    public synchronized void removeWindow(Component comp) {
        for (JInternalFrame internalFrame : desktop.getAllFrames()) {
            Component[] components = internalFrame.getContentPane().getComponents();
            JTabbedPane pane;
            if (components.length == 0) {
                //
            } else if (components[0] instanceof DummyLabel) {
                //
            } else {
                pane = (JTabbedPane) components[0];
                for (int i = 0; i < pane.getTabCount(); i++) {
                    Component c = pane.getComponentAt(i);
                    if (c == comp) {
                        pane.removeTabAt(i);
                        return;
                    }
                }
            }
        }
    }

    private void onPostAddJInternalFrame(JInternalFrameHelper h, WindowInfo fino) {
        if(fino!=null) {
            h.setVisible(true);
            h.setIcon(fino.isClosable() && fino.isIcon());
            if(!fino.isIcon()) {
                h.moveToFront();
                h.setSelected(true);
            }
            h.getFrame().pack();
            h.getFrame().requestFocus(true);
        }else{
            h.setVisible(true);
            if(!h.setIcon(false)){
                System.out.println("Cant deiconify!");
            }
            h.moveToFront();
            h.setSelected(true);
            h.getFrame().pack();
            h.getFrame().requestFocus(true);
        }

//        System.out.println(h.getFrame().getTitle()+" :: "
//                + (h.getFrame().isIcon() ? "icon;" : "")
//                + (h.getFrame().isClosed() ? "closed;" : "")
//                + (h.getFrame().isSelected() ? "selected;" : "")
//                + (h.getFrame().isMaximum() ? "maximum;" : "")
//                + (h.getFrame().isVisible() ? "visible;" : "")
//        );
    }

    public void ensureVisible(JInternalFrame frame) {
        if (frame.getParent() == null) {
            getDesktop().add(frame);
        }
        JInternalFrameHelper inf=new JInternalFrameHelper(frame);
        onPostAddJInternalFrame(inf,null);

    }

    private static class DummyLabel extends JLabel {

        public DummyLabel() {
            super("loading...");
        }
    }

}

package net.thevpc.common.swing.dock;

import net.thevpc.common.swing.util.BorderLayoutHelper;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class JDockTools extends JPanel implements IDockTools {

    List<JDockToolAndButton> windows = new ArrayList<>();
    JDockToolAndButton selected;
    JDockToolAndButton lastSelected;
    JToolBar bar;
    JPanel pane;
    JDockAnchor anchor;
    ButtonGroup bg = new ButtonGroup() {
        @Override
        public void setSelected(ButtonModel model, boolean selected) {
            if (selected) {
                super.setSelected(model, selected);
            } else {
                //super.setSelected(model, selected);
                clearSelection();
            }
        }
    };
    private List<SelectionListener> listeners = new ArrayList<>();

    public JDockTools(JDockAnchor anchor) {
        super(new BorderLayout());
        pane = new JPanel(new BorderLayout());
        this.add(pane, BorderLayout.CENTER);
        bar = new JToolBar();
        bar.setFloatable(false);
        this.anchor = anchor;
        switch (this.anchor) {
            case TOP: {
                bar.setOrientation(JToolBar.HORIZONTAL);
                pane.add(bar, BorderLayout.NORTH);
                break;
            }
            case BOTTOM: {
                bar = new JToolBar(JToolBar.HORIZONTAL);
                pane.add(bar, BorderLayout.SOUTH);
                break;
            }
            case LEFT: {
                bar.setOrientation(JToolBar.VERTICAL);
                pane.add(bar, BorderLayout.WEST);
                break;
            }
            case RIGHT: {
                bar = new JToolBar(JToolBar.VERTICAL);
                pane.add(bar, BorderLayout.EAST);
                break;
            }
            case CENTER: {

            }
        }
    }

    public JDockToolAndButton get(String id) {
        int i = indexOfId(id);
        if (i >= 0) {
            return windows.get(i);
        }
        return null;
    }

    public int indexOfId(String id) {
        for (int i = 0; i < windows.size(); i++) {
            JDockToolAndButton w = windows.get(i);
            if (w.win.id.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public JDockAnchor getDockAnchor() {
        return anchor;
    }

    public void add(String id, JComponent component, String title, Icon icon, boolean closable) {
        JToggleButton t = new JToggleButton(title) {
            @Override
            public void setUI(ButtonUI ui) {
                super.setUI(ui);
                if (anchor == JDockAnchor.LEFT) {
                    super.setUI(new VerticalToggleButtonUI(VerticalToggleButtonUI.Direction.BOTTOM_UP));
                } else if (anchor == JDockAnchor.RIGHT) {
                    super.setUI(new VerticalToggleButtonUI(VerticalToggleButtonUI.Direction.UP_BOTTOM));
                }
            }
        };
        t.setMargin(new Insets(2, 2, 2, 2));
        t.setFont(t.getFont().deriveFont(t.getFont().getSize() * 0.8f));
        JDockToolAndButton tb = new JDockToolAndButton(
                new JDockWindow(id, title, icon, component, closable),
                t
        );
        t.putClientProperty("JDockToolAndButton", tb);
        bg.add(t);
        t.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
                updateContent();
                for (SelectionListener listener : listeners) {
                    listener.onSelectionChanged(id, component, isSelected, anchor);
                }
            }
        });
        bar.add(t);
        windows.add(tb);
        if (selected == null) {
            selected = tb;
            activateSelected();
        }
        bar.setFloatable(false);
    }

    @Override
    public boolean containsId(String id) {
        return windows.stream().anyMatch(x -> x.win.id.equals(id));
    }

    @Override
    public void remove(String id) {
        JDockToolAndButton a = windows.stream().filter(x -> x.win.id.equals(id)).findFirst().orElse(null);
        if (a != null) {
            if (selected == a) {
                add(null, BorderLayout.CENTER);
            }
            windows.remove(a);
            bar.remove(a.button);
            bg.remove(a.button);
        }
    }

    @Override
    public boolean isEmpty() {
        return windows.isEmpty();
    }

    @Override
    public void setWindowTitle(String id, String title) {
        JDockToolAndButton w = get(id);
        if (w != null) {
            w.win.setTitle(title);
            w.button.setText(title);
        }
    }

    @Override
    public void setWindowIcon(String id, Icon icon) {
        JDockToolAndButton w = get(id);
        if (w != null) {
            w.win.setIcon(icon);
        }
    }

    @Override
    public void setWindowClosable(String id, boolean closable) {
        JDockToolAndButton w = get(id);
        if (w != null) {
            w.win.setClosable(closable);
        }
    }

    @Override
    public void setWindowActive(String id, boolean active) {
        for (JDockToolAndButton window : windows) {
            if (window.win.id.equals(id) && window.button.isSelected() != active) {
                window.button.setSelected(active);
                return;
            }
        }
    }

    public void activateLast() {
        if (selected == null && lastSelected != null) {
            selected = lastSelected;
            lastSelected = null;
            activateSelected();
        }
    }

    @Override
    public String[] getActive() {
        return windows.stream().filter(w -> w.button.isSelected())
                .map(w -> w.win.id)
                .toArray(String[]::new)
                ;
    }

    protected void updateContent() {
        JDockToolAndButton selectedWindow = findSelectedWinAndButton();
        if (selectedWindow != null) {
            boolean oldNoSelection = selected == null;
            if (selected != selectedWindow
                    || BorderLayoutHelper.getCenter(pane) != selected.win) {
                selected = selectedWindow;
                System.out.println(anchor + ":selected " + selected.win.id);
                pane.add(selected.win, BorderLayout.CENTER);
            }
            if (oldNoSelection) {
                updateSplitPane();
            }
        } else {
            if (selected != null) {
//            selected.button.setSelected(false);
                lastSelected = selected;
                System.out.println(anchor + ":lastSelected " + lastSelected.win.id);
                pane.remove(selected.win);
                selected = null;
                if (getParent() instanceof JSplitPane) {
                    JSplitPane jsp = (JSplitPane) getParent();
                    if (anchor == JDockAnchor.LEFT || anchor == JDockAnchor.TOP) {
                        jsp.setDividerLocation(jsp.getMinimumDividerLocation());
                    } else {
                        jsp.setDividerLocation(jsp.getMaximumDividerLocation());
                    }
                }
                repaint();
            }
        }
    }

    private JDockToolAndButton findSelectedWinAndButton() {
        JDockToolAndButton selectedWindow = null;
        for (JDockToolAndButton window : windows) {
            if (window.button.isSelected()) {
                selectedWindow = window;
            }
        }
        return selectedWindow;
    }

    public void scrollChanged() {
        if (getParent() instanceof JSplitPane) {
            JSplitPane jsp = (JSplitPane) getParent();
            switch (anchor) {
                case LEFT:
                case TOP: {
                    if (jsp.getDividerLocation() <= jsp.getMinimumDividerLocation()) {
                        JDockToolAndButton w = findSelectedWinAndButton();
                        if (w != null) {
                            w.button.setSelected(false);
                        }
                    } else {
                        JDockToolAndButton w = findSelectedWinAndButton();
                        if (w == null && lastSelected != null) {
                            activateLast();
                        }
                    }
                    break;
                }
                case BOTTOM:
                case RIGHT: {
                    if (jsp.getDividerLocation() >= jsp.getMaximumDividerLocation()) {
                        JDockToolAndButton w = findSelectedWinAndButton();
                        if (w != null) {
                            w.button.setSelected(false);
                        }
                    } else {
                        JDockToolAndButton w = findSelectedWinAndButton();
                        if (w == null && lastSelected != null) {
                            activateLast();
                        }
                    }
                    break;
                }
            }
        }
    }

    private JSplitPane resolveSplitPane() {
        Component c=getParent();
        while(c instanceof JSplitPane){
            JSplitPane s = (JSplitPane) c;
            if(s.getTopComponent()==this || s.getBottomComponent()==this){
                return s;
            }
            c=c.getParent();
        }
        return null;
    }

    private void updateSplitPane() {
        if (findSelectedWinAndButton() != null) {
            JSplitPane jsp = resolveSplitPane();
            if (jsp != null) {
                switch (anchor) {
                    case LEFT:
                    case TOP: {
                        if (jsp.getDividerLocation() < jsp.getMinimumDividerLocation()) {
                            int newVal = (int) ((jsp.getMaximumDividerLocation()) * 0.3);
                            if (newVal > jsp.getMaximumDividerLocation()) {
                                newVal = jsp.getMaximumDividerLocation();
                            }
                            if (newVal <= jsp.getMinimumDividerLocation()) {
                                newVal = jsp.getMinimumDividerLocation() + 1;
                            }
                            jsp.setDividerLocation(newVal);
                        } else {
//                            jsp.setDividerLocation(jsp.getLastDividerLocation());
                        }
                        break;
                    }
                    case BOTTOM:
                    case RIGHT: {
                        if (jsp.getDividerLocation() >= jsp.getMaximumDividerLocation()) {
                            int newVal = (int) ((jsp.getMaximumDividerLocation()) * (1 - 0.3));
                            if (newVal < jsp.getMinimumDividerLocation()) {
                                newVal = jsp.getMinimumDividerLocation();
                            }
                            if (newVal >= jsp.getMaximumDividerLocation()) {
                                newVal = jsp.getMaximumDividerLocation() - 1;
                            }
                            jsp.setDividerLocation(newVal);
                        } else /*if (jsp.getLastDividerLocation() < jsp.getMinimumDividerLocation()) */ {
//                            jsp.setDividerLocation(jsp.getLastDividerLocation());
                        }
                        break;
                    }
                }
            }
        }
    }

    public void activateSelected() {
        selected.button.setSelected(true);
    }

    public void setSelectedToolAt(int index) {
        JDockToolAndButton a = windows.get(index);
        a.button.setSelected(true);
    }

    public void setToolBarVisible(boolean b) {
        bar.setVisible(b);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (bar != null) {
            bar.updateUI();
        }
        if (windows != null) {
            for (JDockToolAndButton window : windows) {
                window.win.updateUI();
            }
        }
    }

    public void addListener(SelectionListener s) {
        if (s != null) {
            listeners.add(s);
        }
    }

    public interface SelectionListener {
        void onSelectionChanged(String id, JComponent component, boolean selected, JDockAnchor anchor);
    }
}

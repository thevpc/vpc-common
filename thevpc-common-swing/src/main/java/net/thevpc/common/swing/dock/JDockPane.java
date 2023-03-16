package net.thevpc.common.swing.dock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class JDockPane extends JPanel {

    private IDockTools[] all = new IDockTools[JDockAnchor.values().length];

    private JSplitPane[] splitPanes = new JSplitPane[4];
    private boolean[] splitPaneUsed = new boolean[4];
    private double[] splitPaneSizes = new double[4];
    private List<JDockTools.SelectionListener> listeners=new ArrayList<>();
    private JDockTools.SelectionListener dispatcher=new JDockTools.SelectionListener() {
        @Override
        public void onSelectionChanged(String id, JComponent component, boolean selected, JDockAnchor anchor) {
            for (JDockTools.SelectionListener listener : listeners) {
                if(listener!=null) {
                    listener.onSelectionChanged(id, component, selected, anchor);
                }
            }
        }
    };

    public void addListener(JDockTools.SelectionListener s){
        if(s!=null){
            listeners.add(s);
        }
    }

    public JDockPane() {
        super(new BorderLayout());
        addComponentListener(new ComponentListener() {
            Dimension oldDim;

            {
                oldDim = getSize();
            }

            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = getSize();
                //if (oldDim != null) {
                    for (JDockAnchor value : JDockAnchor.values()) {
                        IDockTools t = all[value.ordinal()];
                        if(t!=null){
                            if(t.getActive().length==0){
                                //readjust because all is closed!!
                                switch (value){
                                    case LEFT:{
                                        setHorizontalDividerLocation(0,0);
                                        break;
                                    }
                                    case RIGHT:{
                                        setHorizontalDividerLocation(1,Integer.MAX_VALUE);
                                        break;
                                    }
                                    case TOP:{
                                        setVerticalDividerLocation(0,0);
                                        break;
                                    }
                                    case BOTTOM:{
                                        setVerticalDividerLocation(1,Integer.MAX_VALUE);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    //                    double[] splitPaneSizes0 = Arrays.copyOf(splitPaneSizes, splitPaneSizes.length);
////                    System.out.println(Arrays.toString(splitPaneSizes0));
//                    if (splitPaneUsed[0]) {
//                        double v = splitPaneSizes0[0] * newSize.width / oldDim.width;
//                        splitPanes[0].setDividerLocation((int) v);
//                    }
//                    if (splitPaneUsed[1]) {
//                        double v = splitPaneSizes0[1] * newSize.width / oldDim.width;
//                        splitPanes[1].setDividerLocation((int) v);
//                    }
//                    if (splitPaneUsed[2]) {
//                        double v = splitPaneSizes0[2] * newSize.height / oldDim.height;
////                        System.out.println("ASK-2: " + (int) v + " : " + (splitPaneSizes0[2] / oldDim.height) + " :: " + oldDim + " -> " + newSize);
//                        splitPanes[2].setDividerLocation((int) v);
//                    }
//                    if (splitPaneUsed[3]) {
//                        double v = splitPaneSizes0[3] * newSize.height / oldDim.height;
//                        splitPanes[3].setDividerLocation((int) v);
//                    }
                //}
                //oldDim = newSize;
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    private int getHorizontalSplitIndex(int index) {
        if (index == 0) {
            if (splitPaneUsed[0]) {
                return 0;
            }
        } else {
            if (splitPaneUsed[1]) {
                return 1;
            }
            if (splitPaneUsed[0]) {
                return 1;
            }
        }
        return -1;
    }

    private int getVerticalSplitIndex(int index) {
        if (index == 0) {
            if (splitPaneUsed[2]) {
                return 2;
            }
        } else {
            if (splitPaneUsed[3]) {
                return 3;
            }
            if (splitPaneUsed[2]) {
                return 2;
            }
        }
        return -1;
    }

    public void setHorizontalDividerLocation(int index, int location) {
        int i = getHorizontalSplitIndex(index);
        if(splitPanes[0]==null){
            return;
        }
        switch (i) {
            case 0: {
                if(location==Integer.MAX_VALUE){
                    location=splitPanes[0].getMaximumDividerLocation();
                }else if(location==0){
                    location=splitPanes[0].getMinimumDividerLocation();
                }
                splitPanes[0].setDividerLocation(location);
                break;
            }
            case 1: {
                if(splitPanes[1]==null){
                    setHorizontalDividerLocation(0,location);
                    return;
                }
                if(location==Integer.MAX_VALUE){
                    location=splitPanes[1].getMaximumDividerLocation();
                }else if(location==0){
                    location=splitPanes[1].getMinimumDividerLocation();
                }
                splitPanes[1].setDividerLocation(
                        location
                                - (splitPanes[0].getDividerLocation()
                                + splitPanes[0].getDividerSize())
                );
                break;
            }
        }
    }

    public int getHorizontalDividerLocation(int index) {
        int i = getHorizontalSplitIndex(index);
        switch (i) {
            case 0: {
                return splitPanes[0].getDividerLocation();
            }
            case 1: {
                return splitPanes[0].getDividerLocation()
                        + splitPanes[0].getDividerSize()
                        + splitPanes[1].getDividerLocation();
            }
        }
        return -1;
    }

    public void setVerticalDividerLocation(int index, int location) {
        int i = getVerticalSplitIndex(index);
        if(splitPanes[2]==null){
            return;
        }
        switch (i) {
            case 2: {
                if(location==Integer.MAX_VALUE){
                    location=splitPanes[2].getMaximumDividerLocation();
                }else if(location==0){
                    location=splitPanes[2].getMinimumDividerLocation();
                }
                splitPanes[2].setDividerLocation(location);
                break;
            }
            case 3: {
                if(splitPanes[3]==null){
                    setVerticalDividerLocation(0,location);
                    return;
                }
                if(location==Integer.MAX_VALUE){
                    location=splitPanes[3].getMaximumDividerLocation();
                }else if(location==0){
                    location=splitPanes[3].getMinimumDividerLocation();
                }
                splitPanes[3].setDividerLocation(
                        location
                                - (splitPanes[2].getDividerLocation()
                                + splitPanes[2].getDividerSize())
                );
                break;
            }
        }
    }

    public int getVerticalDividerLocation(int index) {
        int i = getVerticalSplitIndex(index);
        switch (i) {
            case 2: {
                return splitPanes[2].getDividerLocation();
            }
            case 3: {
                return splitPanes[2].getDividerLocation()
                        + splitPanes[2].getDividerSize()
                        + splitPanes[3].getDividerLocation();
            }
        }
        return -1;
    }

    public void add(String id, JComponent n, String title, Icon icon, boolean closable, JDockAnchor anchor) {
        JDockAnchor old = lookupAnchor(id);
        if (old != null) {
            throw new IllegalArgumentException("already found " + id + " in " + old);
        }
        if (all[anchor.ordinal()] == null) {
            if (anchor == JDockAnchor.CENTER) {
                all[anchor.ordinal()] = new TabsTools();
//                ((TabsTools)(all[anchor.ordinal()])).
//                addListener(dispatcher);
            } else {
                all[anchor.ordinal()] = new JDockTools(anchor);
                ((JDockTools)(all[anchor.ordinal()])).
                        addListener(dispatcher);
            }
        }
        all[anchor.ordinal()].add(id, n, title, icon, closable);
        rebuild();
    }

    public void setWindowTitle(String id, String title) {
        IDockTools a = lookupIDockTools(id);
        if (a != null) {
            a.setWindowTitle(id, title);
        }
    }

    public void setWindowIcon(String id, Icon icon) {
        IDockTools a = lookupIDockTools(id);
        if (a != null) {
            a.setWindowIcon(id, icon);
        }
    }

    public void setWindowActive(String id, boolean active) {
        IDockTools a = lookupIDockTools(id);
        if (a != null) {
            a.setWindowActive(id, active);
        }
    }

    public void setWindowClosable(String id, boolean closable) {
        IDockTools a = lookupIDockTools(id);
        if (a != null) {
            a.setWindowClosable(id, closable);
        }
    }

    public void setWindowAnchor(String id, JDockAnchor anchor) {
        if (anchor == null) {
            return;
        }
        IDockTools a = lookupIDockTools(id);
        if (a != null) {
            if (a.getDockAnchor() != anchor) {
                //???
            }
        }
    }

    public IDockTools lookupIDockTools(String id) {
        for (int i = 0; i < all.length; i++) {
            IDockTools a = all[i];
            if (a != null) {
                if (a.containsId(id)) {
                    return a;
                }
            }
        }
        return null;
    }

    public JDockAnchor lookupAnchor(String id) {
        for (int i = 0; i < all.length; i++) {
            IDockTools a = all[i];
            if (a != null) {
                if (a.containsId(id)) {
                    return JDockAnchor.values()[i];
                }
            }
        }
        return null;
    }

    public void remove(String id) {
        JDockAnchor anchor = lookupAnchor(id);
        if (anchor == null) {
            return;
        }
        if (all[anchor.ordinal()] != null) {
            all[anchor.ordinal()].remove(id);
            if (all[anchor.ordinal()].isEmpty()) {
                all[anchor.ordinal()] = null;
            }
        }
        rebuild();
    }

    private void rebuild() {
        for (int i = 0; i < 4; i++) {
            splitPaneUsed[i] = false;
            splitPaneSizes[i] = 0;
        }
        JComponent n = rebuildV();
        add(n, BorderLayout.CENTER);
    }

    private JComponent rebuildV() {
        List<JComponent> all2 = new ArrayList<>();
        if (all[JDockAnchor.TOP.ordinal()] != null) {
            all2.add((JComponent) all[JDockAnchor.TOP.ordinal()]);
        }
        all2.add(rebuildH());
        if (all[JDockAnchor.BOTTOM.ordinal()] != null) {
            all2.add((JComponent) all[JDockAnchor.BOTTOM.ordinal()]);
        }
        if (all2.size() == 0) {
            return new JLabel();
        }
        if (all2.size() == 1) {
            return all2.get(0);
        }
        JSplitPane s = createSplitPane(2);
        if (all[JDockAnchor.TOP.ordinal()] != null) {
            s.setResizeWeight(0);
        }else{
            s.setResizeWeight(1);
        }
        s.setOrientation(JSplitPane.VERTICAL_SPLIT);
        s.add(all2.get(0), JSplitPane.LEFT);
        s.add(all2.get(1), JSplitPane.RIGHT);
        if (all2.size() == 3) {
            s.setDividerLocation(0.2);
            JSplitPane s2 = createSplitPane(3);
            s2.setResizeWeight(1);
            s2.setOrientation(JSplitPane.VERTICAL_SPLIT);
            s2.add(s, JSplitPane.LEFT);
            s2.add(all2.get(2), JSplitPane.RIGHT);
            s2.setDividerLocation(0.8);
            return s2;
        } else {
            s.setDividerLocation(0.2);
        }
        return s;
    }

    private JComponent rebuildH() {
        List<JComponent> all2 = new ArrayList<>();
        if (all[JDockAnchor.LEFT.ordinal()] != null) {
            all2.add((JComponent) all[JDockAnchor.LEFT.ordinal()]);
        }
        if (all[JDockAnchor.CENTER.ordinal()] != null) {
            all2.add((JComponent) all[JDockAnchor.CENTER.ordinal()]);
        }
        if (all[JDockAnchor.RIGHT.ordinal()] != null) {
            all2.add((JComponent) all[JDockAnchor.RIGHT.ordinal()]);
        }
        if (all2.size() == 0) {
            return new JLabel();
        }
        if (all2.size() == 1) {
            return all2.get(0);
        }
        JSplitPane s = createSplitPane(0);
        if (all[JDockAnchor.LEFT.ordinal()] != null) {
            s.setResizeWeight(0);
        }else{
            s.setResizeWeight(1);
        }
        s.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        s.add(all2.get(0), JSplitPane.LEFT);
        s.add(all2.get(1), JSplitPane.RIGHT);
        if (all2.size() == 3) {
            s.setDividerLocation(0.2);
            JSplitPane s2 = createSplitPane(1);
            s2.setResizeWeight(1);
            s2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            s2.add(s, JSplitPane.LEFT);
            s2.add(all2.get(2), JSplitPane.RIGHT);
            s2.setDividerLocation(0.7);
            return s2;
        } else {
            s.setDividerLocation(0.3);
        }
        return s;
    }

    private JSplitPane createSplitPane(int index) {
        if (splitPanes[index] == null) {
            splitPanes[index] = new JSplitPane();
            splitPanes[index].addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            for (IDockTools t : all) {
                                if( t instanceof JDockTools) {
                                    ((JDockTools)t).scrollChanged();
                                }
                            }
//
//                            splitPaneSizes[index] = ((Number) evt.getNewValue()).doubleValue();
////                    System.out.println("SIZE " + index + " :: " + splitPaneSizes[index] + " : "
////                            + ((index >= 1)
////                                    ? (splitPaneSizes[index] / getHeight())
////                                    : (splitPaneSizes[index] / getHeight()))
////                            + " :: " + getSize()
////                    );
//                            switch (index) {
//                                case 0: {
//                                    if (splitPaneSizes[index] > splitPanes[index].getMinimumDividerLocation()) {
//                                        if (all[JDockAnchor.LEFT.ordinal()] != null) {
//                                            if(false) {
//                                                all[JDockAnchor.LEFT.ordinal()].activateLast();
//                                            }
//                                        }
//                                    }
//                                    break;
//                                }
//                                case 1: {
//                                    if (all[JDockAnchor.RIGHT.ordinal()] != null) {
//                                        if(false) {
//                                            all[JDockAnchor.RIGHT.ordinal()].activateLast();
//                                        }
//                                    }
//                                    break;
//                                }
//                                case 2: {
//                                    if (splitPaneSizes[index] < splitPanes[index].getMaximumDividerLocation()) {
//                                        if (all[JDockAnchor.BOTTOM.ordinal()] != null) {
//                                            if(false) {
//                                                all[JDockAnchor.BOTTOM.ordinal()].activateLast();
//                                            }
//                                        }
//                                    }
//                                    break;
//                                }
//                                case 3: {
//                                    if (all[JDockAnchor.TOP.ordinal()] != null) {
//                                        if(false) {
//                                            all[JDockAnchor.TOP.ordinal()].activateLast();
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
                        }
                    }
            );
        }
        splitPaneUsed[index] = true;
        return splitPanes[index];
    }

}

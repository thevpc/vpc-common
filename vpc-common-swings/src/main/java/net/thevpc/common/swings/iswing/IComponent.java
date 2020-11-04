package net.thevpc.common.swings.iswing;

import java.awt.event.MouseListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.*;
import java.beans.PropertyChangeListener;

/**
 * Component Interface Wrapper
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)  alias vpc
 * @creationtime 2009/08/15 21:24:16
 */
public interface IComponent {


    public void repaint();

    public void addMouseListener(MouseListener l);

    public void removeMouseListener(MouseListener l);

    public void addPropertyChangeListener(PropertyChangeListener listener); 

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);


    public void addFocusListener(FocusListener l);

    public void removeFocusListener(FocusListener l);

    public void addKeyListener(KeyListener l);

    public void removeKeyListener(KeyListener l);

    public Point getLocationOnScreen();

    public boolean isEnabled();

    public void setEnabled(boolean enable);
}

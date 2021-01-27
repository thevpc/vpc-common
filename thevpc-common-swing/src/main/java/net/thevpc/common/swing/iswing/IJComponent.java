package net.thevpc.common.swing.iswing;

import javax.swing.*;
import javax.swing.border.Border;
import java.beans.PropertyChangeListener;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)  alias vpc
 * %creationtime 2009/08/15 21:25:07
 */
public interface IJComponent extends IContainer {

    public void updateUI();

    public void setBorder(Border border);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void addPropertyChangeListener(String property, PropertyChangeListener listener);

    public void removePropertyChangeListener(String property, PropertyChangeListener listener);

    public JPopupMenu getComponentPopupMenu();

    /**
     * Returns the value of the property with the specified key.  Only
     * properties added with <code>putClientProperty</code> will return
     * a non-<code>null</code> value.
     *
     * @param key the being queried
     * @return the value of this property or <code>null</code>
     * @see #putClientProperty
     */
    public Object getClientProperty(Object key);

    /**
     * Adds an arbitrary key/value "client property" to this component.
     * <p>
     * The <code>get/putClientProperty</code> methods provide access to
     * a small per-instance hashtable. Callers can use get/putClientProperty
     * to annotate components that were created by another module.
     * For example, a
     * layout manager might store per child constraints this way. For example:
     * <pre>
     * componentA.putClientProperty("to the left of", componentB);
     * </pre>
     * If value is <code>null</code> this method will remove the property.
     * Changes to client properties are reported with
     * <code>PropertyChange</code> events.
     * The name of the property (for the sake of PropertyChange
     * events) is <code>key.toString()</code>.
     * <p>
     * The <code>clientProperty</code> dictionary is not intended to
     * support large
     * scale extensions to JComponent nor should be it considered an
     * alternative to subclassing when designing a new component.
     *
     * @param key   the new client property key
     * @param value the new client property value; if <code>null</code>
     *              this method will remove the property
     * @see #getClientProperty
     * @see #addPropertyChangeListener
     */
    public void putClientProperty(Object key, Object value);

    public void setTransferHandler(TransferHandler newHandler);
}

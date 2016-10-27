package net.vpc.common.log;

import java.util.Hashtable;
import java.util.Set;
import java.util.Collections;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 15:53:02
 * To change this template use File | Settings | File Templates.
 */
public class LogPropertyChangeSupportObject {
    private Hashtable<String,Object> properties;
    private PropertyChangeSupport propertyChangeSupport;

    public LogPropertyChangeSupportObject() {
    }

    public Object setProperty(String name, Object value) {
        if (value == null && properties == null) {
            return null;
        } else if (value == null) {
            Object old = properties.remove(name);
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange(name, old, value);
            }
            return old;
        } else {
            if (properties == null) {
                properties = new Hashtable<String,Object>();
            }
            Object old = properties.put(name, value);
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange(name, old, value);
            }
            return old;
        }
    }

    public Object getProperty(String name) {
        if (properties != null) {
            return properties.get(name);
        }
        return null;
    }

    public Object removeProperty(String name) {
        if (properties != null) {
            Object old = properties.remove(name);
            if (propertyChangeSupport != null) {
                propertyChangeSupport.firePropertyChange(name, old, null);
            }
            return old;
        }
        return null;
    }

    protected void firePropertyChange(String propertyName,
                                      Object oldValue, Object newValue) {
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public Set getPropertyNames() {
        return properties == null ? Collections.EMPTY_SET : properties.keySet();
    }

    public Set getPropertiesEntrySet() {
        return properties == null ? Collections.EMPTY_SET : properties.entrySet();
    }

    public PropertyChangeListener[] getPropertyChangeListener() {
        return propertyChangeSupport == null ? new PropertyChangeListener[0] : propertyChangeSupport.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListener(String propertyName) {
        return propertyChangeSupport == null ? new PropertyChangeListener[0] : propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(String[] propertyNames, PropertyChangeListener listener) {
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
        }
        for (int i = 0; i < propertyNames.length; i++) {
            propertyChangeSupport.addPropertyChangeListener(propertyNames[i], listener);
        }
    }
}

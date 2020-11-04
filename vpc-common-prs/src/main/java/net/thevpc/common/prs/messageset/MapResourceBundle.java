package net.thevpc.common.prs.messageset;

import java.util.*;

public class MapResourceBundle extends ResourceBundle {
    private java.util.Map lookup;
    public MapResourceBundle(Map map){
        lookup = new java.util.HashMap(map);
    }

    // Implements java.util.ResourceBundle.handleGetObject; inherits javadoc specification.
    public Object handleGetObject(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return lookup.get(key);
    }

    /**
     * Implementation of ResourceBundle.getKeys.
     */
    public java.util.Enumeration<String> getKeys() {
        ResourceBundle parent = this.parent;
        return new SimpleResourceBundleEnumeration(lookup.keySet(),
                (parent != null) ? parent.getKeys() : null);
    }

    static class SimpleResourceBundleEnumeration implements Enumeration<String> {

    private Set<String> set;
    private Iterator<String> iterator;
    private Enumeration<String> enumeration; // may remain null
    private String next = null;

    /**
     * Constructs a resource bundle enumeration.
     * @param set an set providing some elements of the enumeration
     * @param enumeration an enumeration providing more elements of the enumeration.
     *        enumeration may be null.
     */
    SimpleResourceBundleEnumeration(Set<String> set, Enumeration<String> enumeration) {
        this.set = set;
        this.iterator = set.iterator();
        this.enumeration = enumeration;
    }


    public boolean hasMoreElements() {
        if (next == null) {
            if (iterator.hasNext()) {
                next = iterator.next();
            } else if (enumeration != null) {
                while (next == null && enumeration.hasMoreElements()) {
                    next = enumeration.nextElement();
                    if (set.contains(next)) {
                        next = null;
                    }
                }
            }
        }
        return next != null;
    }

    public String nextElement() {
        if (hasMoreElements()) {
            String result = next;
            next = null;
            return result;
        } else {
            throw new NoSuchElementException();
        }
    }
}
}
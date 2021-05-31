/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import java.util.*;

/**
 *
 * @author thevpc
 */
public class DefaultUserObjects implements UserObjects{

    private Map<Object, Object> userObjects;

    @Override
    public Set<Object> keySet() {
        if (userObjects == null) {
            return Collections.emptySet();
        }
        return new LinkedHashSet<>(userObjects.keySet());
    }

    @Override
    public Object get(Object n) {
        if (n != null && userObjects != null) {
            return userObjects.get(n);
        }
        return null;
    }

    @Override
    public void put(Object n, Object value) {
        if (n != null) {
            if (value != null) {
                if (userObjects == null) {
                    userObjects = new LinkedHashMap<>();
                }
                userObjects.put(n, value);
            } else {
                if (userObjects != null) {
                    userObjects.remove(n);
                    if (userObjects.isEmpty()) {
                        userObjects = null;
                    }
                }
            }
        }
    }
}

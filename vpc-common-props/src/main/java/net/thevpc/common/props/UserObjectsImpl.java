/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author vpc
 */
public class UserObjectsImpl implements UserObjects{

    private Map<String, Object> userObjects;

    @Override
    public Set<String> names() {
        if (userObjects == null) {
            return Collections.emptySet();
        }
        return new LinkedHashSet<>(userObjects.keySet());
    }

    @Override
    public Object getUserObject(String n) {
        if (n != null && userObjects != null) {
            return userObjects.get(n);
        }
        return null;
    }

    @Override
    public void putUserObject(String n, Object value) {
        if (n != null) {
            if (value != null) {
                if (userObjects == null) {
                    userObjects = new HashMap<>();
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import java.util.*;
import java.util.function.Function;

/**
 * @author thevpc
 */
public class DefaultUserObjects implements UserObjects {

    private Map<Object, Object> userObjects;

    @Override
    public Set<Object> keySet() {
        if (userObjects == null) {
            return Collections.emptySet();
        }
        return new LinkedHashSet<>(userObjects.keySet());
    }

    @Override
    public <V> V get(Object n) {
        if (n != null && userObjects != null) {
            return (V) userObjects.get(n);
        }
        return null;
    }

    @Override
    public <K, V> V computeIfAbsent(K k, Function<? super K, ? extends V> value) {
        if (userObjects == null) {
            userObjects = new LinkedHashMap<>();
        }
        return (V) userObjects.computeIfAbsent(k, (Function) value);
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

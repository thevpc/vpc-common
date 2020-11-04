/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.prs.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public class ClassMap<V> extends HashMap<Class, V> {
    private Map<Class, V> cache = new HashMap<Class, V>();

    public ClassMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ClassMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ClassMap() {
    }

    public ClassMap(Map<? extends Class, ? extends V> t) {
        super(t);
    }

    public V put(Class key, V value) {
        cache.clear();
        return super.put(key, value);
    }

    public V remove(Object key) {
        cache.clear();
        return super.remove(key);
    }

    public V getBest(Class clazz) {
        V value;
        HashSet<Class> seen = new HashSet<Class>();
        Stack<Class> queue = new Stack<Class>();
        queue.add(0, clazz);
        seen.add(clazz);
        while (queue.size() > 0) {
            Class k = queue.pop();
            value = get(k);
            if (value == null) {
                value = cache.get(clazz);
            }
            if (value != null) {
                cache.put(clazz, value);
                return value;
            } else {
                Class superclass = k.getSuperclass();
                if (superclass != null && !seen.contains(superclass)) {
                    seen.add(superclass);
                    queue.add(0, k.getSuperclass());
                }
                Class[] interfaces = k.getInterfaces();
                for (Class aClass : interfaces) {
                    if (!seen.contains(aClass)) {
                        seen.add(aClass);
                        queue.add(0, aClass);
                    }
                }
            }
        }
        return null;
    }

    public Class getKey(Class clazz) {
        HashSet<Class> seen = new HashSet<Class>();
        Stack<Class> queue = new Stack<Class>();
        queue.add(0, clazz);
        seen.add(clazz);
        while (queue.size() > 0) {
            Class k = queue.pop();
            if (containsKey(k)) {
                return k;
            }
            Class superclass = k.getSuperclass();
            if (superclass != null && !seen.contains(superclass)) {
                seen.add(superclass);
                queue.add(0, k.getSuperclass());
            }
            Class[] interfaces = k.getInterfaces();
            for (Class aClass : interfaces) {
                if (!seen.contains(aClass)) {
                    seen.add(aClass);
                    queue.add(0, aClass);
                }
            }
        }
        return null;
    }

}

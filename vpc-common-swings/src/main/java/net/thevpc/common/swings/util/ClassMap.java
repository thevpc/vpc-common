/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.swings.util;

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

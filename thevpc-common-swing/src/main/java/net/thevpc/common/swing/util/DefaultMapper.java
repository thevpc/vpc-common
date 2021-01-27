/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.util;

import java.util.Map;

/**
 *
 * @author thevpc
 */
public class DefaultMapper<K, V> implements Mapper<K, V> {

    private Map<K, V> map;

    public DefaultMapper(Map<K, V> map) {
        this.map = map;
    }

    @Override
    public V get(K k) {
        return map.get(k);
    }
}

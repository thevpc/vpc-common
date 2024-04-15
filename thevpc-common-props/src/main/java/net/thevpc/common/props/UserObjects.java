/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import java.util.Set;
import java.util.function.Function;

/**
 *
 * @author thevpc
 */
public interface UserObjects {

    <V> V get(Object n);

    Set<Object> keySet();

    void put(Object n, Object value);

    <K, V> V computeIfAbsent(K k, Function<? super K, ? extends V> value);
    
}

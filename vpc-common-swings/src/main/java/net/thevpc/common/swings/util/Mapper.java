/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.util;

/**
 *
 * @author vpc
 */
public interface Mapper<K,V> {
    V get(K k);
}

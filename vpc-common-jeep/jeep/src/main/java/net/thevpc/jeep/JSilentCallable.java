/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jeep;

/**
 *
 * @author thevpc
 */
@FunctionalInterface
public interface JSilentCallable<V> {

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    V call();
}

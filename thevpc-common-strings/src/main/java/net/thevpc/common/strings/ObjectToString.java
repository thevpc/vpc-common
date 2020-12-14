/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.strings;

/**
 *
 * @author thevpc
 */
@FunctionalInterface
public interface ObjectToString<T> {

    String toString(T object);
}

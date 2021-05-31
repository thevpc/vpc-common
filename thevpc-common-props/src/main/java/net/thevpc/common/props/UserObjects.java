/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

import java.util.Set;

/**
 *
 * @author thevpc
 */
public interface UserObjects {

    Object get(Object n);

    Set<Object> keySet();

    void put(Object n, Object value);
    
}

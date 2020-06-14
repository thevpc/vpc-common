/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.props;

import java.util.Set;

/**
 *
 * @author vpc
 */
public interface UserObjects {

    Object getUserObject(String n);

    Set<String> names();

    void putUserObject(String n, Object value);
    
}

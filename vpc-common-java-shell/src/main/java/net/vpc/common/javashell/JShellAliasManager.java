/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.javashell;

import java.util.Set;

/**
 *
 * @author vpc
 */
public interface JShellAliasManager {

    String get(String name);

    Set<String> getAll();

    void set(String key, String value);
    
}

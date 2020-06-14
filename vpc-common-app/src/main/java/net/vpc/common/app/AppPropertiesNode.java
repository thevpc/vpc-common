/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app;

/**
 *
 * @author vpc
 */
public interface AppPropertiesNode {

    AppPropertiesNodeFolder parent();

    AppPropertiesTree tree();

    String name();

    String type();

    Object object();

    Object getUserObject(String n);

    void putUserObject(String n, Object value);

}

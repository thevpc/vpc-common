/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.app.swing.core;

import net.thevpc.common.app.AppPropertiesNodeFolder;
import net.thevpc.common.app.AppPropertiesTree;

/**
 *
 * @author vpc
 */
public class EmptyPropertiesTree implements AppPropertiesTree {

    private AppPropertiesNodeFolder root;

    public EmptyPropertiesTree(String name) {
        this.root = new DefaultPropertiesNodeFolder(name, null);
    }

    @Override
    public AppPropertiesNodeFolder root() {
        return root;
    }

    @Override
    public void refresh() {
    
    }
    

}
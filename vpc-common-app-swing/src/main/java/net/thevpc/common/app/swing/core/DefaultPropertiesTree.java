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
 * @author thevpc
 */
public class DefaultPropertiesTree implements AppPropertiesTree {

    private AppPropertiesNodeFolder root;

    public DefaultPropertiesTree(AppPropertiesNodeFolder root) {
        setRoot(root);
    }

    public DefaultPropertiesTree() {
    }

    protected void setRoot(AppPropertiesNodeFolder root) {
        this.root = root;
        if (root != null) {
            ((AbstractPropertiesNode) root).tree = this;
        }
    }

    @Override
    public AppPropertiesNodeFolder root() {
        return root;
    }

    public void refresh() {
    }
}

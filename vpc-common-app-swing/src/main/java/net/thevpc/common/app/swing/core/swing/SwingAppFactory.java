/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.app.swing.core.swing;

import net.thevpc.common.app.AppMenuBar;
import net.thevpc.common.app.AppPopupMenu;
import net.thevpc.common.app.AppToolBar;
import net.thevpc.common.app.Application;

/**
 *
 * @author vpc
 */
public class SwingAppFactory {

    public AppMenuBar createMenuBar(Application app, String rootPath) {
        return new JAppMenuBar(rootPath, app);
    }

    public AppPopupMenu createPopupMenu(Application app, String rootPath) {
        return new JAppPopupMenu(rootPath, app);
    }

    public AppToolBar createToolBar(Application app, String rootPath) {
        return new JAppToolBarGroup(rootPath, app);
    }
}
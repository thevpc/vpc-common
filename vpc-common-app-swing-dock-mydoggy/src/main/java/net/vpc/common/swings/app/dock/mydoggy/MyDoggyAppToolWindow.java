/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.swings.app.dock.mydoggy;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.app.AppToolWindow;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;

/**
 *
 * @author vpc
 */
public class MyDoggyAppToolWindow implements AppToolWindow {

    private MyDoggyAppDockingWorkspace toolWindowManager;
    private WritablePValue<Boolean> active = Props.of("activated").valueOf(Boolean.class, false);
    private ToolWindow toolWindow;
    private String id;

    public MyDoggyAppToolWindow(MyDoggyAppDockingWorkspace toolWindowManager, String id, String title, Icon icon, Component component, ToolWindowAnchor anchor) {
        this.toolWindowManager = toolWindowManager;
        this.id = id;
        toolWindow = toolWindowManager.getToolWindowManager().registerToolWindow(id, title, icon, component, anchor);
        active.set(toolWindow.isActive());
        toolWindow.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                switch (evt.getPropertyName()) {
                    case "active": {
                        active.set((Boolean) evt.getNewValue());
                        break;
                    }
                }
            }
        });
        active.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                toolWindow.setActive((Boolean) event.getNewValue());
            }
        });
        toolWindowManager.toolWindows().put(id, this);
        toolWindow.setAvailable(true);
    }

    @Override
    public String id() {
        return id;
    }

    public WritablePValue<Boolean> active() {
        return active;
    }

}
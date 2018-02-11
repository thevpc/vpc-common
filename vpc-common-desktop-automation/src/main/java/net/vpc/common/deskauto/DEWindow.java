/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.deskauto;

import java.awt.Rectangle;

/**
 *
 * @author vpc
 */
public interface DEWindow {

     String getModuleName();

     DEWindowId getId();

     ProcessId getProcessId();

     String getTitle();

     DEWindowRegion getClientRegion();

     DEWindowRegion getTitleRegion();

     Rectangle getBounds();

     boolean isMinimized();

     boolean isMaximized();

     boolean isVisible();

     boolean moveToFront();

     ContextualRobot getRobot();

     boolean isChild();

     boolean isSpecial();

     boolean isRegular();

     boolean isTool();

     DesktopEnvironment getWindowManager();

     void close();

     void setMinimized(boolean b);

     void setMaximized(boolean b);
}

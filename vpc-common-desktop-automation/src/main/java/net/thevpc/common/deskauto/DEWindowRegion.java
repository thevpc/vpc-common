/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto;

import java.awt.*;

/**
 * @author thevpc
 */
public interface DEWindowRegion {

    DEWindow getWindow();

    ContextualRobot getRobot();

    Rectangle getRelativeBounds();

    Rectangle getAbsoluteBounds();

}

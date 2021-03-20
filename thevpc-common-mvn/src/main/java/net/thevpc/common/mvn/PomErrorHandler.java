/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.mvn;

import java.util.logging.Level;

/**
 *
 * @author vpc
 */
public interface PomErrorHandler {

    void log(Level level, String message, Exception error);
}

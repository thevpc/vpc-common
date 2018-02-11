/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.log;

import java.util.logging.Logger;

/**
 *
 * @author vpc
 */
public interface LoggerProvider {
    public Logger getLogger(String name);
    public static LoggerProvider DEFAULT=new LoggerProvider() {

        @Override
        public Logger getLogger(String name) {
            return Logger.getLogger(name);
        }
    };
}
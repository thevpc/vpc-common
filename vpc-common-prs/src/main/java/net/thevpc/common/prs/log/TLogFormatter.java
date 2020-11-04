/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.log;

/**
 *
 * @author vpc
 */
public interface TLogFormatter {
    public String format(Object message);
    public String format(String message,Object details);
}

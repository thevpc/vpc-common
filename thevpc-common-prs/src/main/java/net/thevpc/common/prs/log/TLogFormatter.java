/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.log;

/**
 *
 * @author thevpc
 */
public interface TLogFormatter {
    public String format(Object message);
    public String format(String message,Object details);
}

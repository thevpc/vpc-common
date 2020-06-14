/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.jeep;

/**
 * @author vpc
 */
public class JParseException extends JeepException {

    public JParseException(Throwable cause) {
        super(cause);
    }

    public JParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public JParseException(String message) {
        super(message);
    }

    public JParseException() {
    }

}

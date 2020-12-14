/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.config;

/**
 *
 * @author thevpc
 */
public class ConfigurationException extends RuntimeException{

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException() {
    }
    
}

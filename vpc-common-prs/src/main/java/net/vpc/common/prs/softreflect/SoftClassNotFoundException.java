/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.common.prs.softreflect;

/**
 *
 * @author vpc
 */
public class SoftClassNotFoundException extends RuntimeException{

    public SoftClassNotFoundException(Throwable cause) {
        super(cause);
    }

    public SoftClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SoftClassNotFoundException(String message) {
        super(message);
    }

    public SoftClassNotFoundException() {
    }

}

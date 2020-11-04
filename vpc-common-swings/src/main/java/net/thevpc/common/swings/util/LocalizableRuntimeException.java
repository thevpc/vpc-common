/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.util;

/**
 *
 * @author vpc
 */
public class LocalizableRuntimeException extends RuntimeException {

    private String messageId;
    private Object[] messageParameters;
    private String messageBundle;

//    public LocalizableRuntimeException(String messageId, Object[] messageParameters, String messageBundle, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//        super(message, cause, enableSuppression, writableStackTrace);
//        this.messageId = messageId;
//        this.messageParameters = messageParameters;
//        this.messageBundle = messageBundle;
//    }

    public LocalizableRuntimeException(String messageId, Object[] messageParameters, String messageBundle, Throwable cause) {
        super(cause);
        this.messageId = messageId;
        this.messageParameters = messageParameters;
        this.messageBundle = messageBundle;
    }

    public LocalizableRuntimeException(String messageId, Object[] messageParameters, String messageBundle, String message, Throwable cause) {
        super(message, cause);
        this.messageId = messageId;
        this.messageParameters = messageParameters;
        this.messageBundle = messageBundle;
    }

    public LocalizableRuntimeException(String messageId, Object[] messageParameters, String messageBundle, String message) {
        super(message);
        this.messageId = messageId;
        this.messageParameters = messageParameters;
        this.messageBundle = messageBundle;
    }

    public LocalizableRuntimeException(String messageId, Object[] messageParameters, String messageBundle) {
        this.messageId = messageId;
        this.messageParameters = messageParameters;
        this.messageBundle = messageBundle;
    }

    public String getMessageBundle() {
        return messageBundle;
    }

    public String getMessageId() {
        return messageId;
    }

    public Object[] getMessageParameters() {
        return messageParameters;
    }
    
}

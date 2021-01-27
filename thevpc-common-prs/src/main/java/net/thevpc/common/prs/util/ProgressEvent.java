package net.thevpc.common.prs.util;

public class ProgressEvent {
    private Object source;
    private float progress;
    private String message;
    private Object[] messageParameters;
    private boolean indeterminate;

    public ProgressEvent(Object source, float progress, String message, Object... messageParameters) {
        this(source,false,progress,message,messageParameters);
    }
    
    public ProgressEvent(Object source, boolean indeterminate, float progress, String message, Object... messageParameters) {
        this.source = source;
        this.indeterminate = indeterminate;
        this.progress = progress;
        this.message = message;
        this.messageParameters = messageParameters;
    }

    public Object getSource() {
        return source;
    }

    public float getProgress() {
        return progress;
    }

    public String getMessage() {
        return message;
    }

    public Object[] getMessageParameters() {
        return messageParameters;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }
}

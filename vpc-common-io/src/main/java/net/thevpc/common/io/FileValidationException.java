package net.thevpc.common.io;

import java.io.IOException;

public class FileValidationException extends IOException{
    public FileValidationException() {
    }

    public FileValidationException(String message) {
        super(message);
    }

    public FileValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileValidationException(IOException cause) {
        super(cause);
    }
}

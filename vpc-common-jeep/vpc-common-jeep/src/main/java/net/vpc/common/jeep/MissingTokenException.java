package net.vpc.common.jeep;

public class MissingTokenException extends JParseException {
    public MissingTokenException(Throwable cause) {
        super(cause);
    }

    public MissingTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingTokenException(String message) {
        super(message);
    }

    public MissingTokenException() {
    }
}

package net.vpc.common.jeep;

public class UnexpectedTokenException extends JParseException {
    public UnexpectedTokenException(Throwable cause) {
        super(cause);
    }

    public UnexpectedTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedTokenException(String message) {
        super(message);
    }

    public UnexpectedTokenException() {
    }
}

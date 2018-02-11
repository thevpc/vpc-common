package net.vpc.common.prs.factory;

public class FactoryVetoException extends RuntimeException{
    public FactoryVetoException() {
    }

    public FactoryVetoException(String message) {
        super(message);
    }

    public FactoryVetoException(String message, Throwable cause) {
        super(message, cause);
    }

    public FactoryVetoException(Throwable cause) {
        super(cause);
    }
}

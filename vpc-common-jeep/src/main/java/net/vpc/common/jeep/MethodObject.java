package net.vpc.common.jeep;

public class MethodObject<T> {
    private ArgumentTypes signature;
    private T method;

    public MethodObject(ArgumentTypes signature, T method) {
        this.signature = signature;
        this.method = method;
    }

    public ArgumentTypes getSignature() {
        return signature;
    }

    public T getMethod() {
        return method;
    }
}

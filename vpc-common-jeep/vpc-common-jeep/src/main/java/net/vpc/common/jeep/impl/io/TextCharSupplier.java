package net.vpc.common.jeep.impl.io;

import java.util.function.Supplier;

public class TextCharSupplier implements Supplier<char[]> {
    private final String text;

    public TextCharSupplier(String text) {
        this.text = text;
    }

    @Override
    public char[] get() {
        return text.toCharArray();
    }

    @Override
    public String toString() {
        return "<text>";
    }
}

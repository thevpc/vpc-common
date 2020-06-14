package net.vpc.common.jeep.impl.io;

import net.vpc.common.jeep.util.JeepUtils;

import java.net.URL;
import java.util.function.Supplier;

public class URLCharSupplier implements Supplier<char[]> {
    private final URL url;

    public URLCharSupplier(URL url) {
        this.url = url;
    }

    @Override
    public char[] get() {
        return JeepUtils.urlToCharArray(url);
    }

    @Override
    public String toString() {
        return url.toString();
    }
}

package net.vpc.common.jeep.impl.io;

import net.vpc.common.classpath.ClassPathResource;
import net.vpc.common.classpath.ClassPathResourceFilter;
import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.common.jeep.util.JeepUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.function.Supplier;

public class ZippedURLFileCharSupplier implements Supplier<char[]> {
    private final URL url;
    private final String path;
    private final ClassPathResourceFilter filter;

    public ZippedURLFileCharSupplier(URL url, String path,ClassPathResourceFilter filter) {
        this.url = url;
        this.path = path;
        this.filter = filter;
    }

    public String getPath() {
        return path;
    }

    @Override
    public char[] get() {
        for (ClassPathResource r : ClassPathUtils.resolveResources(new URL[]{url}, filter)) {
            if (r.getPath().equals(path)) {
                try (InputStream in = r.open()) {
                    return JeepUtils.inputStreamToCharArray(in);
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
        }
        throw new IllegalArgumentException("source path not found " + url + "#" + path);
    }

    @Override
    public String toString() {
        return String.valueOf(url);
    }
}

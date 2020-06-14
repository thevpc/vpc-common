package net.vpc.common.jeep.impl.io;

import net.vpc.common.jeep.util.JeepUtils;

import java.io.File;
import java.util.function.Supplier;

public class FileCharSupplier implements Supplier<char[]> {
    private final File file;

    public FileCharSupplier(File file) {
        this.file = file;
    }

    @Override
    public char[] get() {
        return JeepUtils.fileToCharArray(file);
    }

    @Override
    public String toString() {
        return file.getPath();
    }
}

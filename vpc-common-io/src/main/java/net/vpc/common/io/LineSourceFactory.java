package net.vpc.common.io;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public interface LineSourceFactory {

    int getSupport(String fileName, String mimetype);

    LineSource createLineSource(InputStream stream, String mimeType);

    LineSource createLineSource(File file);

    LineSource createLineSource(Path file);
}

package net.thevpc.common.io;

import java.io.IOException;
import java.nio.file.Path;

public interface PathValidator {
    void validateFile(Path path) throws IOException;
}

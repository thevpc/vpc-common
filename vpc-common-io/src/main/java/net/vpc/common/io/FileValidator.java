package net.vpc.common.io;

import java.io.File;
import java.io.IOException;

public interface FileValidator {
    void validateFile(File file) throws IOException;
}

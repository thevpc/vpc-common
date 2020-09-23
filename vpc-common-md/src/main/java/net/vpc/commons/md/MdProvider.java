package net.vpc.commons.md;

import java.io.Reader;
import java.io.Writer;

public interface MdProvider {
    String getMimeType();

    MdParser createParser(Reader reader);

    MdWriter createWriter(Writer writer);
}

package net.thevpc.common.io;

import java.io.IOException;

public interface LineSource {
    void read(LineVisitor visitor) throws IOException;
}

package net.thevpc.common.app;

import java.io.File;

public interface AppIconResolver {
    String iconIdForFile(File f, boolean selected, boolean expanded);

    String iconIdForFileName(String f, boolean selected, boolean expanded);
}
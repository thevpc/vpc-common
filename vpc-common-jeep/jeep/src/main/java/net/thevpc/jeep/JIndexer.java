package net.thevpc.jeep;

import java.io.File;
import java.net.URL;

public interface JIndexer {
    int indexSDK(String sdkHome, boolean force);

    /**
     *
     * @param compilationUnit
     * @return number of indexed types
     */
    int indexSource(JCompilationUnit compilationUnit);

    /**
     *
     * @param file
     * @param force
     * @return number of indexed types
     */
    int indexLibrary(File file,boolean force);

    /**
     *
     * @param file
     * @param force
     * @return number of indexed types
     */
    int indexLibrary(URL file, boolean force);
}

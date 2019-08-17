package net.vpc.common.io;

import java.io.IOException;

public interface PosApi {

    boolean isSupportedFindJavaProcessList();

    boolean isSupportedKillProcess();

    JpsResult[] findJavaProcessList(String jdkHome, boolean mainArgs, boolean vmArgs, JpsResultFilter filter) throws IOException;

    boolean killProcess(String processId) throws IOException;
}

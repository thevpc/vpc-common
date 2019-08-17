package net.vpc.common.io;

import java.io.IOException;

public class LinuxPosApi extends AbstractPosApi {
    @Override
    public boolean killProcess(String processId) throws IOException {
        ProcessBuilder2 b = new ProcessBuilder2().addCommand("kill", "-9", processId);
        b.waitFor();
        return (b.getResult() == 0);
    }

    @Override
    public boolean isSupportedFindJavaProcessList() {
        return true;
    }

    @Override
    public boolean isSupportedKillProcess() {
        return true;
    }
}

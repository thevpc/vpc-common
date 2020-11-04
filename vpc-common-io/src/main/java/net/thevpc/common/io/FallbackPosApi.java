package net.thevpc.common.io;

public class FallbackPosApi implements PosApi {

    @Override
    public boolean isSupportedFindJavaProcessList() {
        return true;
    }

    @Override
    public JpsResult[] findJavaProcessList(String jdkHome, boolean mainArgs, boolean vmArgs, JpsResultFilter filter) {
        return new JpsResult[0];
    }

    @Override
    public boolean isSupportedKillProcess() {
        return false;
    }

    @Override
    public boolean killProcess(String processId) {
        return false;
    }
}
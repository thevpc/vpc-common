package net.thevpc.common.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPosApi implements PosApi {

    @Override
    public boolean isSupportedFindJavaProcessList() {
        return true;
    }

    @Override
    public JpsResult[] findJavaProcessList(String jdkHome, boolean mainArgs, boolean vmArgs, JpsResultFilter filter) throws IOException {
        String cmd = "jps";
        if (jdkHome != null) {
            cmd = jdkHome + File.separator + "bin" + File.separator + cmd;
        }
        ProcessBuilder2 b = new ProcessBuilder2()
                .addCommand(cmd)
                .addCommand("-l" + (mainArgs ? "m" : "") + (vmArgs ? "v" : ""))
                .setRedirectErrorStream(true)
                .grabOutputString();
        b.waitFor();
        List<JpsResult> r = new ArrayList<>();
        if (b.getResult() == 0) {
            String out = b.getOutputString();
            String[] split = out.split("\n");
            for (String line : split) {
                int s1 = line.indexOf(' ');
                int s2 = line.indexOf(' ', s1 + 1);
                String pid = line.substring(0, s1).trim();
                String cls = line.substring(s1 + 1, s2 < 0 ? line.length() : s2).trim();
                String args = s2 >= 0 ? line.substring(s2 + 1).trim() : "";
                JpsResult rr = new JpsResult(
                        pid, cls, args
                );
                if (filter == null || filter.accept(rr)) {
                    r.add(rr);
                }
            }
        }
        return r.toArray(new JpsResult[r.size()]);
    }

    @Override
    public boolean isSupportedKillProcess() {
        return false;
    }

    @Override
    public boolean killProcess(String processId) throws IOException{
        return false;
    }
}
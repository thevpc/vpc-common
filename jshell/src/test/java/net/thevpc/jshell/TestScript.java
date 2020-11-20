package net.thevpc.jshell;

import net.thevpc.jshell.parser.nodes.Node;
import net.thevpc.jshell.parser2.JShellParser2;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestScript {
    @Test
    public void test() {
        JShell sh = new JShell();
        sh.setExternalExecutor(new JShellExternalExecutor() {
            @Override
            public void execExternalCommand(String[] command, JShellContext context) {
                try {
                    ProcessBuilder pb = new ProcessBuilder();
                    Process p = pb.command(command)
                            .directory(new File(context.getCwd()))
                            .start();
                    JShellContext.Watcher watcher = context.bindStreams(p.getInputStream(), p.getErrorStream(), p.getOutputStream());
                    p.waitFor();
                    Thread.sleep(50);
                    watcher.stop();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        sh.executeLine("echo $(dirname aa/$0)", true, sh.createContext().setServiceName("example"));
    }
}

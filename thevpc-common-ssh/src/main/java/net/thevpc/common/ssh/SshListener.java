package net.thevpc.common.ssh;

import java.io.InputStream;

public interface SshListener {
    void onExec(String command);

    void onGet(String from, String to, boolean mkdir);

    void onPut(String from, String to, boolean mkdir);

    InputStream monitorInputStream(InputStream stream, long length, String name);
}

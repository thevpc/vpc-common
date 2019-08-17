package net.vpc.common.mon;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vpc on 5/14/17.
 */
public class ProgressMonitorOutputStream extends OutputStream {
    private OutputStream target;
    private long written;
    private ProgressMonitor monitor;
    private String message;

    public ProgressMonitorOutputStream(OutputStream target, ProgressMonitor monitor, String messagePrefix) {
        this.target = target;
        this.monitor = ProgressMonitorFactory.nonnull(monitor);
        message = messagePrefix==null?"":messagePrefix.trim();
        if (message.length() > 0) {
            message += " ";
        }
        message += ", written {0}";
    }

    @Override
    public void write(int b) throws IOException {
        target.write(b);
        written++;
        monitor.setProgress(-1, message, _BytesSizeFormat.INSTANCE.format(written));
    }

    @Override
    public void write(byte[] b) throws IOException {
        target.write(b);
        written += b.length;
        monitor.setProgress(-1, message, _BytesSizeFormat.INSTANCE.format(written));
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        target.write(b, off, len);
        written += len;
        monitor.setProgress(-1, message, _BytesSizeFormat.INSTANCE.format(written));
    }

    @Override
    public void close() throws IOException {
        super.close();
        target.close();
    }
}

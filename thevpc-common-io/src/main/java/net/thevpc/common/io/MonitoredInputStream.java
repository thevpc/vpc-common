/**
 * ====================================================================
 * vpc-common-io : common reusable library for
 * input/output
 * <br>
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author thevpc
 */
public class MonitoredInputStream extends InputStream {

    private final InputStream base;
    private long count;
    private long lastCount;
    private long startTime;
    private long lastTime;
    private final long length;
    private final InputStreamMonitor monitor;
    private final Object source;
    private final String sourceName;
    private boolean completed = false;

    public MonitoredInputStream(InputStream base, Object source, String sourceName, long length, InputStreamMonitor monitor) {
        this.base = base;
        if (monitor == null) {
            throw new NullPointerException();
        }
        this.monitor = monitor;
        this.source = source;
        this.sourceName = sourceName;
        this.length = length;
    }

    @Override
    public int read() throws IOException {
        try {
            onBeforeRead();
            int r = this.base.read();
            if (r != -1) {
                onAfterRead(1);
            } else {
                onComplete(null);
            }
            return r;
        } catch (IOException ex) {
            onComplete(ex);
            throw ex;
        }
    }

    @Override
    public boolean markSupported() {
        return base.markSupported();
    }

    @Override
    public synchronized void reset() throws IOException {
        try {
            base.reset();
        } catch (IOException ex) {
            onComplete(ex);
            throw ex;
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        base.mark(readlimit);
    }

    @Override
    public void close() throws IOException {
        onComplete(null);
        base.close();
    }

    @Override
    public int available() throws IOException {
        try {
            return base.available();
        } catch (IOException ex) {
            onComplete(ex);
            throw ex;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            onBeforeRead();
            long r = base.skip(n);
            onAfterRead(r);
            return r;
        } catch (IOException ex) {
            onComplete(ex);
            throw ex;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            onBeforeRead();
            int r = base.read(b, off, len);
            onAfterRead(r);
            return r;
        } catch (IOException ex) {
            onComplete(ex);
            throw ex;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        try {
            onBeforeRead();
            int r = base.read(b);
            onAfterRead(r);
            return r;
        } catch (IOException ex) {
            onComplete(ex);
            throw ex;
        }
    }

    private void onBeforeRead() {
        if (!completed) {
            if (startTime == 0) {
                long now = System.currentTimeMillis();
                this.startTime = now;
                this.lastTime = now;
                this.lastCount = 0;
                this.count = 0;
                monitor.onStart(new InputStreamEvent(source, sourceName, 0, 0, 0, 0, length, null));
            }
        }
    }

    private void onAfterRead(long count) {
        if (!completed) {
            long now = System.currentTimeMillis();
            this.count += count;
            if (monitor.onProgress(new InputStreamEvent(source, sourceName, this.count, now - startTime, this.count - lastCount, now - lastTime, length, null))) {
                this.lastCount = this.count;
                this.lastTime = now;
            }
        }
    }

    private void onComplete(IOException ex) {
        if (!completed) {
            completed = true;
            long now = System.currentTimeMillis();
            monitor.onComplete(new InputStreamEvent(source, sourceName, this.count, now - startTime, this.count - lastCount, now - lastTime, length, ex));
        }
    }


}

/**
 * ====================================================================
 * vpc-prs library
 * <p>
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
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

package net.thevpc.common.mon;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.logging.Level;

import net.thevpc.common.msg.StringMessage;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 7 oct. 2007 15:23:41
 */
/*
 * @(#)ProgressMonitorInputStream.java	1.20 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * this is a modified version that does not use InputStream.available
 */


/**
 * this is a
 * Monitors the progress of reading from some InputStream. This ProgressMonitor
 * is normally invoked in roughly this form:
 * <pre>
 * InputStream in = new BufferedInputStream(
 *                          new ProgressMonitorInputStream(
 *                                  parentComponent,
 *                                  "Reading " + fileName,
 *                                  new FileInputStream(fileName)));
 * </pre><p>
 * This creates a progress monitor to monitor the progress of reading
 * the input stream.  If it's taking a while, a ProgressDialog will
 * be popped up to inform the user.  If the user hits the Cancel button
 * an InterruptedIOException will be thrown on the next read.
 * All the right cleanup is done when the stream is closed.
 *
 *
 * <p>
 * <p>
 * For further documentation and examples see
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/components/progress.html">How to Monitor Progress</a>,
 * a section in <em>The Java Tutorial.</em>
 *
 * @author James Gosling
 * @version 1.20 11/17/05
 * @see javax.swing.ProgressMonitor
 * @see javax.swing.JOptionPane
 */
public class ProgressMonitorInputStream2 extends FilterInputStream {
    private ProgressMonitor pmonitor;
    private long nread = 0;
    private long size = 0;


    /**
     * Constructs an object to monitor the progress of an input stream.
     *
     * @param in The input stream to be monitored.
     */
    public ProgressMonitorInputStream2(InputStream in, long size, ProgressMonitor pmonitor) {
        super(in);
        this.size = size;
        if (pmonitor == null) {
            pmonitor = ProgressMonitors.logger(1000);
        }
        this.pmonitor = pmonitor;
    }


//    /**
//     * Get the ProgressMonitor object being used by this stream. Normally
//     * this isn't needed unless you want to do something like change the
//     * descriptive text partway through reading the file.
//     * @return the ProgressMonitor object used by this object
//     */
//    public javax.swing.ProgressMonitor getProgressMonitor() {
//        return monitor;
//    }


    /**
     * Overrides <code>FilterInputStream.read</code>
     * to update the progress monitor after the read.
     */
    public int read() throws IOException {
        int c = in.read();
        if (c >= 0) {
            nread++;
            int progress = (int) (((double) nread) * 100 / size);
            pmonitor.setProgress(progress);
        }
        if (pmonitor.isCanceled()) {
            InterruptedIOException exc =
                    new InterruptedIOException("progress");
            exc.bytesTransferred = (int) nread;
            throw exc;
        }
        return c;
    }


    /**
     * Overrides <code>FilterInputStream.read</code>
     * to update the progress monitor after the read.
     */
    public int read(byte b[]) throws IOException {
        int nr = in.read(b);
        if (nr > 0) {
            nread += nr;
            int progress = (int) (((double) nread) * 100 / size);
            pmonitor.setProgress(progress);
        }
        if (pmonitor.isCanceled()) {
            InterruptedIOException exc =
                    new InterruptedIOException("progress");
            exc.bytesTransferred = (int) nread;
            throw exc;
        }
        return nr;
    }


    /**
     * Overrides <code>FilterInputStream.read</code>
     * to update the progress monitor after the read.
     */
    public int read(byte b[],
                    int off,
                    int len) throws IOException {
        int nr = in.read(b, off, len);
        if (nr > 0) {
            nread += nr;
            double progress = (((double) nread) / size);
            pmonitor.setProgress(progress, new StringMessage(Level.INFO, ""));
        }
        if (pmonitor.isCanceled()) {
            InterruptedIOException exc =
                    new InterruptedIOException("progress");
            exc.bytesTransferred = (int) nread;
            throw exc;
        }
        return nr;
    }


    /**
     * Overrides <code>FilterInputStream.skip</code>
     * to update the progress monitor after the skip.
     */
    public long skip(long n) throws IOException {
        long nr = in.skip(n);
        if (nr > 0) {
            nread += nr;
            size = nread + in.available();
            int progress = (int) (((double) nread) * 100 / size);
            pmonitor.setProgress(progress, new StringMessage(Level.INFO, ""));
        }
        return nr;
    }


    /**
     * Overrides <code>FilterInputStream.close</code>
     * to close the progress monitor as well as the stream.
     */
    public void close() throws IOException {
        in.close();
        pmonitor.terminate();
    }


    /**
     * Overrides <code>FilterInputStream.reset</code>
     * to reset the progress monitor as well as the stream.
     */
    public synchronized void reset() throws IOException {
        in.reset();
        size = nread + in.available();
        int progress = (int) (((double) nread) * 100 / size);
        pmonitor.setProgress(progress, new StringMessage(Level.INFO, ""));
    }

}
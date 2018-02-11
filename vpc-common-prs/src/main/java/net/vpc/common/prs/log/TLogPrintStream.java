/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.vpc.common.prs.log;

import javax.swing.*;
import java.io.*;
import java.sql.SQLException;


/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  3 aout 2006 15:06:01
 */
public class TLogPrintStream implements TLog {
    private PrintStream errorStream;
    private PrintStream traceStream;
    private PrintStream warnStream;
    private PrintStream debugStream;
    private boolean autoCloseDebugStream;
    private boolean autoCloseWarnStream;
    private boolean autoCloseTraceStream;
    private boolean autoCloseErrorStream;

    public TLogPrintStream() {
    }

    public TLogPrintStream(File logFile) throws FileNotFoundException {
        logFile.getParentFile().mkdirs();
        PrintStream outputStream = null;
        outputStream = new PrintStream(logFile);
        setDebugStream(outputStream);
        setErrorStream(outputStream);
        setTraceStream(outputStream);
        setWarnStream(outputStream);
        setAutoCloseDebugStream(true);
        setAutoCloseErrorStream(true);
        setAutoCloseTraceStream(true);
        setAutoCloseWarnStream(true);
    }

    public TLogPrintStream(PrintStream out,boolean autoClose) throws FileNotFoundException {
        setDebugStream(out);
        setErrorStream(out);
        setTraceStream(out);
        setWarnStream(out);
        setAutoCloseDebugStream(autoClose);
        setAutoCloseErrorStream(autoClose);
        setAutoCloseTraceStream(autoClose);
        setAutoCloseWarnStream(autoClose);
    }


    protected String format(String type, Object msg) {
        StringBuilder sb = new StringBuilder();
        if (msg == null) {
            //sb.append("");
        } else if (msg instanceof SQLException) {
            SQLException s = (SQLException) msg;
            while (s != null) {
                if (sb.length() > 0) {
                    sb.append("\n------------------\n");
                }
                sb.append(throwableToString(s));
                s = s.getNextException();
            }
        } else if(msg instanceof Throwable){
            sb.append(throwableToString((Throwable) msg));
        }else{
            sb.append(msg);
        }
        return sb.toString();
    }

    private String throwableToString(Throwable th) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        th.printStackTrace(new PrintStream(out));
        return (out.toString());
    }

    public void debug(Object msg) {
        if (debugStream != null) {
            debugStream.println(format("debug", msg));
        }
    }

    public void error(Object msg) {
        if (errorStream != null) {
            errorStream.println(format("error", msg));
        }
    }

    public void error(String message, Object msg) {
        if (errorStream != null) {
            errorStream.println(format("error", message));
            errorStream.println(format("error", msg));
        }
    }

    public void trace(Object msg) {
        if (traceStream != null) {
            traceStream.println(format("trace", msg));
        }
    }

    public void warning(Object msg) {
        if (warnStream != null) {
            warnStream.println(format("warning", msg));
        }
    }

    public JComponent getComponent() {
        return null;
    }

    public PrintStream getDebugStream() {
        return debugStream;
    }

    public final void setDebugStream(PrintStream debugStream) {
        this.debugStream = debugStream;
    }

    public PrintStream getErrorStream() {
        return errorStream;
    }

    public final void setErrorStream(PrintStream errorStream) {
        this.errorStream = errorStream;
    }

    public PrintStream getTraceStream() {
        return traceStream;
    }

    public final void setTraceStream(PrintStream traceStream) {
        this.traceStream = traceStream;
    }

    public PrintStream getWarnStream() {
        return warnStream;
    }

    public final void setWarnStream(PrintStream warnStream) {
        this.warnStream = warnStream;
    }

    public void close() {
        if(autoCloseDebugStream){
            debugStream.close();
        }
        if(autoCloseErrorStream){
            errorStream.close();
        }
        if(autoCloseTraceStream){
            traceStream.close();
        }
        if(autoCloseWarnStream){
            warnStream.close();
        }
    }

    public boolean isAutoCloseDebugStream() {
        return autoCloseDebugStream;
    }

    public final void setAutoCloseDebugStream(boolean autoCloseDebugStream) {
        this.autoCloseDebugStream = autoCloseDebugStream;
    }

    public boolean isAutoCloseWarnStream() {
        return autoCloseWarnStream;
    }

    public final void setAutoCloseWarnStream(boolean autoCloseWarnStream) {
        this.autoCloseWarnStream = autoCloseWarnStream;
    }

    public boolean isAutoCloseTraceStream() {
        return autoCloseTraceStream;
    }

    public final void setAutoCloseTraceStream(boolean autoCloseTraceStream) {
        this.autoCloseTraceStream = autoCloseTraceStream;
    }

    public boolean isAutoCloseErrorStream() {
        return autoCloseErrorStream;
    }

    public final void setAutoCloseErrorStream(boolean autoCloseErrorStream) {
        this.autoCloseErrorStream = autoCloseErrorStream;
    }
}

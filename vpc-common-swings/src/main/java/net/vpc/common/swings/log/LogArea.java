/**
 * ====================================================================
 *                        vpc-swingext library
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
package net.vpc.common.swings.log;


import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import net.vpc.common.swings.SwingUtilities3;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 4 juin 2007 17:28:33
 */
public class LogArea extends JTextArea {

    public LogArea() {
        setEditable(false);
    }

    protected String format(String type, Object msg) {
        StringBuilder sb = new StringBuilder();
        if (msg == null) {
            sb.append("");
        } else if (msg instanceof SQLException) {
            SQLException s = (SQLException) msg;
            while (s != null) {
                if (sb.length() > 0) {
                    sb.append("\n------------------\n");
                }
                sb.append(throwableToString(s));
                s = s.getNextException();
            }
        } else if (msg instanceof Throwable) {
            sb.append(throwableToString((Throwable) msg));
        } else {
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
        logString(format("debug", msg));
    }

    public void error(Object msg) {
        logString(format("error", msg));
    }

    public void error(String message, Object msg) {
        logString(format("error", message));
        logString(format("error", msg));
    }

    public void trace(Object msg) {
        logString(format("trace", msg));
    }

    public void warning(Object msg) {
        logString(format("warning", msg));
    }


    public void logString(final String message) {
        SwingUtilities3.invokeLater(new Runnable() {
            public void run() {
                append(message + "\n");
            }
        });
    }

    public void clear() {
        SwingUtilities3.invokeLater(new Runnable() {
            public void run() {
                setText("");
            }
        });
    }

    public void close() {
        //do nothing
    }


}

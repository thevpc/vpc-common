/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
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
package net.thevpc.common.swings.log;


import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import net.thevpc.common.swings.SwingUtilities3;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 4 juin 2007 17:28:33
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

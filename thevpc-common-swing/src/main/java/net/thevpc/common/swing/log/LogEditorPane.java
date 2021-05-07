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
package net.thevpc.common.swing.log;


import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import net.thevpc.common.swing.SwingUtilities3;
import net.thevpc.common.swing.text.TextManipSupport;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 27 juin 2007 19:29:18
 */
public class LogEditorPane extends JTextPane  {
    private final SimpleAttributeSet attrTrace = new SimpleAttributeSet();
    private final SimpleAttributeSet attrDebug = new SimpleAttributeSet();
    private final SimpleAttributeSet attrError = new SimpleAttributeSet();
    private final SimpleAttributeSet attrWarning = new SimpleAttributeSet();
    protected TextManipSupport support;
    public LogEditorPane() {
        setEditable(false);
//        StyleConstants.setForeground(attrTrace, null);
        StyleConstants.setForeground(attrDebug, Color.BLUE.darker());
        StyleConstants.setForeground(attrError, Color.RED);
        StyleConstants.setForeground(attrWarning, Color.ORANGE.darker());
        support = new TextManipSupport(this);
        putClientProperty("clearEnabled", Boolean.TRUE);
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
            logString(attrDebug,format("debug", msg));
    }

    public void error(Object msg) {
        logString(attrError,format("error", msg));
    }

    public void error(String message, Object msg) {
        logString(attrError,format("error", message));
        logString(attrError,format("error", msg));
    }

    public void trace(Object msg) {
            logString(attrTrace, format("trace", msg));
    }

    public void warning(Object msg) {
            logString(attrWarning,format("warning", msg));
    }



    public void logString(final SimpleAttributeSet attrTrace,final String message) {
        SwingUtilities3.invokeLater(new Runnable() {
            public void run() {
                StyledDocument styledDocument = getStyledDocument();
                try {
                    styledDocument.insertString(styledDocument.getLength(), message+"\n", attrTrace);
                    if (styledDocument.getLength() > 1024 * 10) {
                        styledDocument.remove(0, 1024);
                    }
                } catch (BadLocationException e) {
                    //e.printStackTrace();
                }
                //showError();
            }
        });
    }

    public void clear(){
        SwingUtilities3.invokeLater(new Runnable() {
            public void run() {
                setText("");
            }
        });
    }


    @Override
    public void updateUI() {
        super.updateUI();
        if (support != null) {
            support.updateUI();
        }
    }

    public void close() {
        //do nothing
    }

}

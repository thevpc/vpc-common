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

import net.vpc.common.prs.iconset.IconSet;
import net.vpc.common.prs.messageset.MessageSet;
import net.vpc.common.prs.log.TLog;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import net.vpc.common.swings.ComponentResourcesUpdater;
import net.vpc.common.swings.PRSManager;
import net.vpc.common.swings.SwingUtilities3;
import net.vpc.common.swings.TextManipSupport;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 juin 2007 19:29:18
 */
public class TLogEditorPane extends JTextPane implements TLog {
    private final SimpleAttributeSet attrTrace = new SimpleAttributeSet();
    private final SimpleAttributeSet attrDebug = new SimpleAttributeSet();
    private final SimpleAttributeSet attrError = new SimpleAttributeSet();
    private final SimpleAttributeSet attrWarning = new SimpleAttributeSet();
    private TextManipSupport support;
    public TLogEditorPane() {
        setEditable(false);
//        StyleConstants.setForeground(attrTrace, null);
        StyleConstants.setForeground(attrDebug, Color.BLUE.darker());
        StyleConstants.setForeground(attrError, Color.RED);
        StyleConstants.setForeground(attrWarning, Color.ORANGE.darker());
        support = new TextManipSupport(this);
        putClientProperty("clearEnabled", Boolean.TRUE);
        PRSManager.addSupport(this, "LogTextArea", new ComponentResourcesUpdater() {
            public void update(JComponent comp, String id, MessageSet messageSet, IconSet iconSet) {
                PRSManager.update(support.getActions(), messageSet, iconSet);
            }
        });
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author vpc
 */
public class SimpleLogFormatter extends Formatter {

    Date dat = new Date();
    private final static DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Object args[] = new Object[1];
    private String lineSeparator = System.getProperty("line.separator");

    /**
     * Format the given LogRecord.
     * @param record the log record to be formatted.
     * @return a formatted log record
     */
    public synchronized String format(LogRecord record) {
        StringBuilder sb = new StringBuilder(80);
        // Minimize memory allocations here.
        dat.setTime(record.getMillis());
        args[0] = dat;
        sb.append("[").append(dformat.format(dat)).append("]");
        sb.append("[").append(record.getLevel().getLocalizedName()).append("]");
        if (record.getSourceClassName() != null) {
            String scn = record.getSourceClassName();
            if (scn != null) {
                int x = scn.lastIndexOf('.');
                if (x > 0) {
                    scn = scn.substring(x + 1);
                }
            }
            sb.append("[").append(scn);
            if (record.getSourceMethodName() != null) {
                sb.append(".");
                sb.append(record.getSourceMethodName());
                sb.append("(...)");
            }
            sb.append("]");
        } else {
            sb.append("[").append(record.getLoggerName()).append("]");
        }
        sb.append("  ");
        String message = formatMessage(record);
        sb.append(message);
        sb.append(lineSeparator);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter(80);
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
                //
            }
        }
        return sb.toString();
    }
}

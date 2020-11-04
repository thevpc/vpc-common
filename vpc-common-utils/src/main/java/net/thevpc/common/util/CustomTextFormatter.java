package net.thevpc.common.util;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by vpc on 6/19/16.
 */
public class CustomTextFormatter extends Formatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private String format;

    public CustomTextFormatter() {
        this(null);
    }

    public CustomTextFormatter(String format) {
        this.format = format;
        if(format!=null && format.equals("default")){
            this.format= getSimpleFormat();
        }
    }

    private static String getSimpleFormat() {
        String value = System.getProperty("java.util.logging.SimpleFormatter.format");

        String defaultFormat = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%6$s%n";
        if(value != null && value.length()>0) {
            try {
                String.format(value, new Object[]{new Date(), "", "", "", "", ""});
            } catch (Exception var3) {
                value = defaultFormat;
            }
        } else {
            value = defaultFormat;
        }

        return value;
    }


    @Override
    public String format(LogRecord record) {
        if (format == null) {
            StringBuilder sb = new StringBuilder();

            sb.append(new Date(record.getMillis()))
                    .append(" ")
                    .append(record.getLevel().getLocalizedName())
                    .append(" ")
                    .append(record.getSourceClassName())
                    .append(".")
                    .append(record.getSourceMethodName())
                    .append("(...) : ")
                    .append(formatMessage(record))
                    .append(LINE_SEPARATOR);

            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(sw.toString());
                } catch (Exception ex) {
                    // ignore
                }
            }

            return sb.toString();
        }
        StringBuilder source = new StringBuilder();
        if (record.getSourceClassName() != null) {
            source.append(record.getSourceClassName());
            if (record.getSourceMethodName() != null) {
                source.append(" ").append(record.getSourceMethodName());
            }
        } else {
            source.append(record.getLoggerName());
        }
        String message = formatMessage(record);
        String throwable = "";
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                pw.println();
                record.getThrown().printStackTrace(pw);
                pw.close();
                throwable = sw.toString();
            } catch (Exception ex) {
                // ignore
            }
        }
        return String.format(format,
                new Date(record.getMillis()),
                source,
                record.getLoggerName(),
                record.getLevel().getName(),
                message,
                throwable);
    }
}

package net.vpc.common.log;


import java.io.PrintStream;
import java.util.Date;

public class PrintStreamLog extends Log {

    public PrintStreamLog(String name, String title, String desc, String type, String pattern, String[] acceptedTypes, PrintStream writer) {
        super(name, title, desc, type, pattern, acceptedTypes);
        this.writer = writer;
    }

    public void processLog(String type, String message, int logLevel, Date date, long delta, Thread thread, StackTrace stack, String user_id, String user_name) {
        writer.println(buildMessage(type, message, logLevel, date, delta, thread, stack, getName(), getPattern(), user_id, user_name));
    }

    public void clear() {
    }

    private PrintStream writer;
    //private String currentFileName;
}

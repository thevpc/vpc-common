package net.vpc.common.mon;

import java.io.PrintStream;
import java.util.Date;
import net.vpc.common.msg.Message;

public class PrintStreamProgressMonitor extends AbstractProgressMonitor {
    private static _BytesSizeFormat MF = new _BytesSizeFormat("0K0TF");
    private String messageFormat;
    private PrintStream printStream;
    private double progress=Double.NaN;
    private Message message=EMPTY_MESSAGE;

    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public PrintStreamProgressMonitor(String messageFormat, PrintStream printStream) {
        super(nextId());
        if (messageFormat == null) {
            messageFormat = "%date% | %inuse-mem% | %free-mem% : %value%";
        }
        if (messageFormat.indexOf("%value%") < 0) {
            messageFormat = messageFormat + " %value%";
        }
        this.messageFormat = messageFormat;
        if (printStream == null) {
            printStream = System.out;
        }
        this.printStream = printStream;
    }

    public void setMessageImpl(Message message) {
        this.message=message;
        long newd = System.currentTimeMillis();
        printStream.println(messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(getProgress()) ? "   ?%" : ProgressMonitors.PERCENT_FORMAT.format(getProgress()))
                .replace("%inuse-mem%", MF.format(_MemUtils.inUseMemory()))
                .replace("%free-mem%", MF.format(_MemUtils.maxFreeMemory()))
                + " " + message
        );
    }

    @Override
    public String toString() {
        return "PrintStream(" + "value=" + getProgress() +
                "," + printStream + '}';
    }

    @Override
    protected void setProgressImpl(double progress) {
        this.progress=progress;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public double getProgress() {
        return progress;
    }
}

package net.vpc.common.mon;

import java.io.PrintStream;
import java.util.Date;

public class PrintStreamProgressMonitor extends AbstractProgressMonitor {
    private static _BytesSizeFormat MF = new _BytesSizeFormat("0K0TF");
    private String messageFormat;
    private PrintStream printStream;

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

    public void setMessageImpl(TaskMessage message) {
        long newd = System.currentTimeMillis();
        printStream.println(messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(getProgressValue()) ? "   ?%" : ProgressMonitors.PERCENT_FORMAT.format(getProgressValue()))
                .replace("%inuse-mem%", MF.format(_MemUtils.inUseMemory()))
                .replace("%free-mem%", MF.format(_MemUtils.maxFreeMemory()))
                + " " + message
        );
    }

    @Override
    public String toString() {
        return "PrintStream(" + "value=" + getProgressValue() +
                "," + printStream + '}';
    }

}

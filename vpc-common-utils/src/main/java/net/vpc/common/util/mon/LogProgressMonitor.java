package net.vpc.common.util.mon;

import net.vpc.common.util.BytesSizeFormat;
import net.vpc.common.util.Utils;

import java.util.Date;
import java.util.logging.Logger;

public class LogProgressMonitor extends AbstractProgressMonitor {
    private static BytesSizeFormat MF = new BytesSizeFormat("0K0TF");
    private static Logger defaultLog = Logger.getLogger(LogProgressMonitor.class.getName());

    static {
        defaultLog.setUseParentHandlers(false);
    }

    private double progress;
    private ProgressMessage message;
    private String messageFormat;
    private Logger logger;

    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public LogProgressMonitor(String messageFormat, Logger logger) {
        if (messageFormat == null) {
            messageFormat = "%inuse-mem% | %free-mem% : %value%";
        }
        if (messageFormat.indexOf("%value%") < 0) {
            messageFormat = messageFormat + " %value%";
        }
        this.messageFormat = messageFormat;
        if (logger == null) {
            logger = getDefaultLogger();
        }
        this.logger = logger;
    }

    public static Logger getDefaultLogger() {
        return defaultLog;
    }

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress = progress;
        this.message = message;
        long newd = System.currentTimeMillis();
        String msg = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(progress)?"   ?%": ProgressMonitorFactory.PERCENT_FORMAT.format(progress))
                .replace("%inuse-mem%", MF.format(Utils.inUseMemory()))
                .replace("%free-mem%", MF.format(Utils.maxFreeMemory()))
                + " " + message;
        logger.log(message.getLevel(), msg);
    }

    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    @Override
    public String toString() {
        return logger + "(" + getProgressValue() + ")";
    }

}

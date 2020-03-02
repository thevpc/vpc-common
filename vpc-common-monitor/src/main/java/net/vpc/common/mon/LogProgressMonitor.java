package net.vpc.common.mon;

import java.util.Date;
import java.util.logging.Logger;

public class LogProgressMonitor extends AbstractProgressMonitor {
    private static _BytesSizeFormat MF = new _BytesSizeFormat("0K0TF");
    private static Logger defaultLog = Logger.getLogger(LogProgressMonitor.class.getName());

    static {
        defaultLog.setUseParentHandlers(false);
    }

    private TaskMessage message;
    private String messageFormat;
    private Logger logger;

    /**
     * %value%
     * %date%
     *
     * @param messageFormat
     */
    public LogProgressMonitor(String messageFormat, Logger logger) {
        super(nextId());
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


    public void setMessageImpl(TaskMessage message) {
        this.message = message;
        long newd = System.currentTimeMillis();
        String msg = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(getProgressValue()) ? "   ?%" : ProgressMonitors.PERCENT_FORMAT.format(getProgressValue()))
                .replace("%inuse-mem%", MF.format(_MemUtils.inUseMemory()))
                .replace("%free-mem%", MF.format(_MemUtils.maxFreeMemory()))
                + " " + message;
        logger.log(message.getLevel(), msg);
    }

    @Override
    public TaskMessage getProgressMessage() {
        return message==null? EMPTY_MESSAGE : message;
    }

    @Override
    public String toString() {
        return logger + "(" + getProgressValue() + ")";
    }


}

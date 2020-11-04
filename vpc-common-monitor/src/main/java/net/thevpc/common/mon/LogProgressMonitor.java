package net.thevpc.common.mon;

import java.util.Date;
import java.util.logging.Logger;

import net.thevpc.common.msg.Message;

public class LogProgressMonitor extends AbstractProgressMonitor {
    private static _BytesSizeFormat MF = new _BytesSizeFormat("0K0TF");
    private static Logger defaultLog = Logger.getLogger(LogProgressMonitor.class.getName());

    static {
        defaultLog.setUseParentHandlers(false);
    }

    private Message message=EMPTY_MESSAGE;
    private String messageFormat;
    private Logger logger;
    private double progress=Double.NaN;

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

    @Override
    protected void setProgressImpl(double progress) {
        this.progress=progress;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    public static Logger getDefaultLogger() {
        return defaultLog;
    }


    public void setMessageImpl(Message message) {
        this.message = message;
        long newd = System.currentTimeMillis();
        String msg = messageFormat
                .replace("%date%", new Date(newd).toString())
                .replace("%value%", Double.isNaN(getProgress()) ? "   ?%" : ProgressMonitors.PERCENT_FORMAT.format(getProgress()))
                .replace("%inuse-mem%", MF.format(_MemUtils.inUseMemory()))
                .replace("%free-mem%", MF.format(_MemUtils.maxFreeMemory()))
                + " " + message;
        logger.log(message.getLevel(), msg);
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return logger + "(" + getProgress() + ")";
    }


}

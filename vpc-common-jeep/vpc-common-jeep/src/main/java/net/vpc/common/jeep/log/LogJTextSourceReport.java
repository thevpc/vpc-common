package net.vpc.common.jeep.log;

import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.textsource.JTextSourceReport;

public class LogJTextSourceReport implements JTextSourceReport {
    private JCompilerLog clog;

    public LogJTextSourceReport(JCompilerLog clog) {
        this.clog = clog;
    }

    @Override
    public void reportError(String id, String group, String message) {
        clog.error(id,group,message,null);
    }

    @Override
    public void reportWarning(String id, String group, String message) {
        clog.warn(id,group,message,null);
    }
}

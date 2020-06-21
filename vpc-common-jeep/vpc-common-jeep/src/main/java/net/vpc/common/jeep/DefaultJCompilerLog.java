package net.vpc.common.jeep;


import net.vpc.common.textsource.log.impl.DefaultJTextSourceLog;

import java.io.PrintStream;

public class DefaultJCompilerLog extends DefaultJTextSourceLog implements JCompilerLog {

    public DefaultJCompilerLog() {
        super("Compilation");
    }

    public DefaultJCompilerLog(PrintStream out) {
        super("Compilation", out);
    }

}

package net.vpc.common.util.mon;

import net.vpc.common.util.MemorySizeFormatter;
import net.vpc.common.util.Utils;

import java.io.PrintStream;
import java.util.Date;

public class PrintStreamProgressMonitor extends AbstractEnhancedProgressMonitor {
    private double progress;
    private ProgressMessage message;
    private String messageFormat;
    private PrintStream printStream;
    private static MemorySizeFormatter MF=new MemorySizeFormatter("0K0TF");

    /**
     * %value%
     * %date%
     * @param messageFormat
     */
    public PrintStreamProgressMonitor(String messageFormat, PrintStream printStream) {
        if(messageFormat==null){
            messageFormat="%date% | %inuse-mem% | %free-mem% : %value%";
        }
        if(messageFormat.indexOf("%value%")<0){
            messageFormat=messageFormat+" %value%";
        }
        this.messageFormat = messageFormat;
        if(printStream==null){
            printStream=System.out;
        }
        this.printStream=printStream;
    }

    public double getProgressValue() {
        return progress;
    }

    public void setProgressImpl(double progress, ProgressMessage message) {
        this.progress=progress;
        this.message=message;
        long newd=System.currentTimeMillis();
        printStream.println(messageFormat
                        .replace("%date%", new Date(newd).toString())
                        .replace("%value%", Double.isNaN(progress)?"   ?%": ProgressMonitorFactory.PERCENT_FORMAT.format(progress))
                        .replace("%inuse-mem%", MF.format(Utils.inUseMemory()))
                        .replace("%free-mem%", MF.format(Utils.maxFreeMemory()))
                        +" "+message
        );
    }
    @Override
    public ProgressMessage getProgressMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "PrintStream("+"value=" + getProgressValue() +
                "," + printStream +'}';
    }

}

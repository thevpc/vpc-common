package net.thevpc.common.mon;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.thevpc.common.msg.StringMessage;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 31 juil. 2007 23:57:10
 */
public class ProgressMonitors {
    public static final DecimalFormat PERCENT_FORMAT =new DecimalFormat("#00.00%");
    public static ProgressMonitor none() {
        return new SilentProgressMonitor();
    }


    public static boolean isSilent(ProgressMonitor monitor){
        return monitor==null || monitor instanceof SilentProgressMonitor;
    }

    /**
     * creates Monitors for each enabled Element or null if false
     *
     * @param baseMonitor translated
     * @return ProgressMonitor[] array that contains nulls or  translated baseMonitor
     */
    public static ProgressMonitor[] split(ProgressMonitor baseMonitor, int nbrElements) {
        double[] dd = new double[nbrElements];
        Arrays.fill(dd, 1);
        return split(baseMonitor, dd);
    }

    public static ProgressMonitor[] split(ProgressMonitor baseMonitor, double... weight) {
        ProgressMonitor[] all = new ProgressMonitor[weight.length];
        double[] coeffsOffsets = new double[weight.length];
        double[] xweight = new double[weight.length];
        double coeffsSum = 0;
        for (int i = 0; i < weight.length; i++) {
            if (weight[i]>0) {
                coeffsSum += weight[i];
            }
        }
        double coeffsOffset = 0;
        for (int i = 0; i < weight.length; i++) {
            boolean enabledElement = weight[i]>0;
            if (enabledElement) {
                coeffsOffsets[i] = coeffsOffset;
                xweight[i] = (weight[i] / coeffsSum);
                coeffsOffset += xweight[i];
            }
        }
        for (int i = 0; i < weight.length; i++) {
            boolean enabledElement = weight[i]>0;
            if (enabledElement) {
                all[i] = translate(baseMonitor, xweight[i], coeffsOffsets[i]);
            }else{
                all[i]= none();
            }
        }
        return all;
    }

    public static ProgressMonitor translate(ProgressMonitor baseMonitor, int index, int max) {
        return new ProgressMonitorTranslator(baseMonitor, 1.0 / max, index * (1.0 / max));
    }

    public static ProgressMonitor translate(ProgressMonitor baseMonitor, double factor, double start) {
        return new ProgressMonitorTranslator(nonnull(baseMonitor), factor,start);
    }

    public static void setProgress(ProgressMonitor baseMonitor, int i, int max, String message) {
        baseMonitor.setProgress((1.0 * i / max), new StringMessage(Level.FINE, message));
    }

    public static void setProgress(ProgressMonitor baseMonitor, int i, int j, int maxi, int maxj, String message) {
        baseMonitor.setProgress(((1.0 * i * maxi) + j) / (maxi * maxj), new StringMessage(Level.FINE, message));
    }

    public static ProgressMonitor[] createSilentMonitors(int count) {
        ProgressMonitor[] mon = new ProgressMonitor[count];
        for (int i = 0; i < count; i++) {
            mon[i] = none();
        }
        return mon;
    }

    public static ProgressMonitor incremental(ProgressMonitor baseMonitor, int iterations) {
        ProgressMonitor mon = nonnull(baseMonitor);
        mon.setIncrementor(new IntIterationProgressMonitorInc(iterations));
        return mon;
    }

    public static ProgressMonitor incremental(ProgressMonitor baseMonitor, long iterations) {
        ProgressMonitor mon = nonnull(baseMonitor);
        mon.setIncrementor(new LongIterationProgressMonitorInc(iterations));
        return mon;
    }

    public static ProgressMonitor incremental(ProgressMonitor baseMonitor, double delta) {
        ProgressMonitor mon = nonnull(baseMonitor);
        mon.setIncrementor(new DeltaProgressMonitorInc(delta));
        return mon;
    }

    public static ProgressMonitor createLogMonitor(long freq) {
        return logger().temporize(freq);
    }

    public static ProgressMonitor createLogMonitor(String message, long freq) {
        return temporize(new LogProgressMonitor(message, null), freq);
    }

    public static ProgressMonitor createLogMonitor(String message, long freq, Logger out) {
        return logger(message, out).temporize(freq);
    }

    public static ProgressMonitor createOutMonitor(long freq) {
        return out().temporize(freq);
    }

    public static ProgressMonitor createOutMonitor(String message, long freq) {
        return temporize(new PrintStreamProgressMonitor(message, null), freq);
    }

    public static ProgressMonitor createOutMonitor(String message, long freq, PrintStream out) {
        return printStream(message, out).temporize(freq);
    }

    public static ProgressMonitor temporize(ProgressMonitor baseMonitor, long freq) {
        return new FreqProgressMonitor(nonnull(baseMonitor), freq);
    }

    public static ProgressMonitor printStream(String messageFormat, PrintStream printStream) {
        return new PrintStreamProgressMonitor(messageFormat, printStream);
    }

    public static ProgressMonitor logger(String messageFormat, Logger printStream) {
        return new LogProgressMonitor(messageFormat, printStream);
    }

    public static ProgressMonitor logger(Logger printStream) {
        return new LogProgressMonitor(null, printStream);
    }
    public static ProgressMonitor logger(long milliseconds) {
        return logger().temporize(milliseconds);
    }

    public static ProgressMonitor logger() {
        return new LogProgressMonitor(null, null);
    }

//    public static ProgressMonitor logger(String messageFormat, TLog printStream) {
//        return new TLogProgressMonitor(messageFormat, printStream);
//    }


    public static ProgressMonitor out(String messageFormat) {
        return printStream(messageFormat, System.out);
    }

    public static ProgressMonitor out() {
        return printStream(null, System.out);
    }

    public static ProgressMonitor err() {
        return printStream(null, System.err);
    }

    public static ProgressMonitor err(String messageFormat) {
        return printStream(messageFormat, System.err);
    }

    public static ProgressMonitor nonnull(ProgressMonitor monitor) {
        if (monitor == null) {
            return none();
        }
        return monitor;
    }

    public static class Config{
        private static boolean strictComputationMonitor = false;

        public static boolean isStrictComputationMonitor() {
            return strictComputationMonitor;
        }

        public static void setStrictComputationMonitor(boolean strictComputationMonitor) {
            Config.strictComputationMonitor = strictComputationMonitor;
        }
    }

    public static <T> T invokeMonitoredAction(ProgressMonitor mon, String messagePrefix, MonitoredAction<T> run) {
        ProgressMonitor monitor = nonnull(mon);
        monitor.start(messagePrefix + ", starting...");
        String error = null;
        T val = null;
        long startTime = System.currentTimeMillis();
        try {
            val = run.process(monitor, messagePrefix);
        } catch (RuntimeException e) {
            error = e.toString();
            if (error==null || error.length()==0) {
                error = e.getClass().getName();
            }
            throw e;
        } catch (Throwable e) {
            error = e.toString();
            if (error==null || error.length()==0) {
                error = e.getClass().getName();
            }
            throw new RuntimeException(e);
        } finally {
            long endTime = System.currentTimeMillis();
            if (error != null) {
                monitor.terminate(messagePrefix + ", terminated with error after {0}", (endTime-startTime)+"ms");
            } else {
                monitor.terminate(messagePrefix + ", terminated after {0}", (endTime-startTime)+"ms");
            }
        }
        return val;
    }
}

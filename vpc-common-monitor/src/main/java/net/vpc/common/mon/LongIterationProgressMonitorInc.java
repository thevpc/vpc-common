package net.vpc.common.mon;

/**
 * @author taha.bensalah@gmail.com on 7/22/16.
 */
public class LongIterationProgressMonitorInc implements ProgressMonitorInc {
    private double max;
    private long index;

    public LongIterationProgressMonitorInc(long max) {
        this.max = max;
    }

    @Override
    public double inc(double last) {
        index++;
        return index / max;
    }

//    public static class LogProgressMonitor extends BaseProgressMonitor {
//        private static MemorySizeFormatter MF = new MemorySizeFormatter("0K0TF");
//        private static Logger defaultLog = Logger.getLogger(LogProgressMonitor.class.getName());
//        private static FastMessageFormat defaultFastMessageFormat = createFastMessageFormat("used=${inuse-mem} | free=${free-mem} : ${value}");
//
//        static {
//            defaultLog.setUseParentHandlers(false);
//        }
//
//        private double progress;
//        private Message message;
//        private FastMessageFormat fastMessageFormat;
//        private Logger logger;
//
//        /**
//         * %value%
//         * %date%
//         *
//         * @param messageFormat
//         */
//        public LogProgressMonitor(String messageFormat, Logger logger) {
//            if (messageFormat == null) {
//                fastMessageFormat = defaultFastMessageFormat;
//            } else {
//                if (!messageFormat.contains("${value}")) {
//                    messageFormat = messageFormat + " ${value}";
//                }
//                fastMessageFormat = createFastMessageFormat(messageFormat);
//            }
//
//
//            if (logger == null) {
//                logger = getDefaultLogger();
//            }
//            this.logger = logger;
//        }
//
//        private static FastMessageFormat createFastMessageFormat(String format) {
//            FastMessageFormat fastMessageFormat = new FastMessageFormat();
//            fastMessageFormat.addVar("inuse-mem", new FastMessageFormat.Evaluator() {
//                @Override
//                public String eval(Map<String, Object> context) {
//                    return MF.format(Maths.inUseMemory());
//                }
//            });
//            fastMessageFormat.addVar("free-mem", new FastMessageFormat.Evaluator() {
//                @Override
//                public String eval(Map<String, Object> context) {
//                    return MF.format(Maths.maxFreeMemory());
//                }
//            });
//            fastMessageFormat.addVar("date", new FastMessageFormat.Evaluator() {
//                @Override
//                public String eval(Map<String, Object> context) {
//                    return new Date().toString();
//                }
//            });
//            fastMessageFormat.addVar("value", new FastMessageFormat.Evaluator() {
//                @Override
//                public String eval(Map<String, Object> context) {
//                    double progress = (double) context.get("progress");
//                    return Double.isNaN(progress) ? "   ?%" : StringUtils.PERCENT_FORMAT.format(progress);
//                }
//            });
//            fastMessageFormat.parse(format);
//            return fastMessageFormat;
//        }
//
//        public static Logger getDefaultLogger() {
//            return defaultLog;
//        }
//
//        public double getProgressValue() {
//            return progress;
//        }
//
//        public void setProgressImpl(double progress, Message message) {
//            this.progress = progress;
//            this.message = message;
//            Map<String, Object> context = new HashMap<>();
//            context.put("progress", progress);
//            String msg = fastMessageFormat.format(context) + " " + message;
//            logger.log(message.getLevel(), msg);
//        }
//
//        @Override
//        public Message getProgressMessage() {
//            return message;
//        }
//
//        @Override
//        public String toString() {
//            return logger + "(" + getProgressValue() + ")";
//        }
//
//    }
//
//    public static class PercentDoubleFormatter implements DoubleFormat {
//        public static DoubleFormat INSTANCE = new PercentDoubleFormatter();
//
//        DecimalFormat format;
//        DecimalFormat simpleFormat;
//
//        public PercentDoubleFormatter() {
//            format = new DecimalFormat("00.00%");
//            format.setMaximumIntegerDigits(1);
//            simpleFormat = new DecimalFormat("00.00%");
//        }
//
//        @Override
//        public String formatDouble(double value) {
//            if (Double.isNaN(value)) {
//                return ("NaN");
//            } else {
//                DecimalFormat f = format;
//                if ((value >= 1E-3 && value <= 1E4) || (value <= -1E-3 && value >= -1E4)) {
//                    f = simpleFormat;
//                }
//                String v = f == null ? String.valueOf(value) : f.format(value);
//                if (v.endsWith("E0")) {
//                    v = v.substring(0, v.length() - 2);
//                }
//                return v;
//            }
//        }
//    }
}

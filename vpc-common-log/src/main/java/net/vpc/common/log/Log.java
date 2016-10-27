package net.vpc.common.log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class Log extends LogPropertyChangeSupportObject implements
        Comparable {

    private static int static_maxOnHoldMessagesCount = 1000;
    // private static PropertyChangeSupport propertyChangeSupport = new
    // PropertyChangeSupport(Log.class);
    public static final String TYPE_CONSOLE = "Console";
    public static final String TYPE_FILE = "File";
    public static final String TYPE_DATABASE = "Database";
    public static final String TYPE_WINDOW = "Window";
    public static final DecimalFormat DELTA_FORMAT = new DecimalFormat("00000");
    public static final String TRACE_LOGGER = "TRACE_LOGGER";
    public static final String ERROR_LOGGER = "ERROR_LOGGER";
    public static final String WARNING_LOGGER = "WARNING_LOGGER";
    public static final String DEBUG_LOGGER = "DEBUG_LOGGER";
    public static final String STARUP_LOGGER = "STARUP_LOGGER";
    public static final String BUG_LOGGER = "BUG_LOGGER";
    public static final String DEFAULT_LOGGER = "DEFAULT_LOGGER";
    public static final String CONSOLE_LOGGER = "CONSOLE_LOGGER";
    private static final String INTERNAL = "INTERNAL";
    public static final String FATAL = "FATAL";
    public static final String ERROR = "ERROR";
    public static final String WARNING = "WARNING";
    public static final String DEV_WARNING = "DEV_WARNING";
    public static final String TRACE = "TRACE";
    public static final String DEBUG = "DEBUG";
    public static final String BUG = "BUG";
    public static final String METHOD = "METHOD";
    public static final String METHOD_ENTER = "METHOD_ENTER";
    public static final String METHOD_EXIT = "METHOD_EXIT";
    public static final String ALL = "*";
    public static final String[] ALL_ARRAY = {ALL};
    public static final int DEFAULT_LOG_LEVEL = 1;
    public static final String ADVANCED_PATTERN = "[$d->$-][$U:$u][$n/$t][$F:$f:$l][$H/$h($p)] $m";
    public static final String DEFAULT_PATTERN = "[$d][$U:$u][$n][$t] $m";
    public static final String SIMPLE_PATTERN = "[$d][$U:$u] $m";
    public static final String CLIENT_TRACE_MSG_GROUP = "CLIENT_TRACE";
    public static final String DEV_DEPLOY_MSG_GROUP = "DEV_DEPLOYMENT";
    public static final String DEV_PROCESS_MSG_GROUP = "DEV_PROCESS";
    // Log instance
    private SimpleDateFormat dateFormat;
    private String name;
    private String title;
    private String[] acceptedMessageTypes;
    private String pattern;
    private String logType;
    private String description;
    private boolean enabled;
    // Log Management
    // private static String static_current_user_id="?";
    // private static String static_current_user_name="?";
    private static boolean isInitialized = false;
    public static Log SYSTEM_OUT_LOG;
    private static LogThread logTimer;
    private static long lastLogTime;
    private static boolean doStopExcessiveStream;
    private static boolean logGlobalEnabled = true;
    private static Log defaultLogger = null;
    private static Hashtable<String, String> messageType_groupMap = new Hashtable<String, String>();
    private static LinkedHashMap<String, Log> loggers = new LinkedHashMap<String, Log>();
    private static Hashtable<String, HashSet<Log>> loggersByTypesMap = new Hashtable<String, HashSet<Log>>();
    private static HashSet<Log> loggersAcceptingAllTypesHashSet = new HashSet<Log>();
    // private static LogPropertyChangeSupportObject globalPropertyChangeSupport
    // = new GlobalPropertyChangeSupportObject();
    private HashSet<String> acceptedMessageTypeGroups;
    private static boolean isDieing;
    private static LogEventSupport eventSupport = new LogEventSupport();
    private static LogConfig config;

    static {
        init();
    }
    public static final Log SILENT_LOGGER = new SilentLog();
//    private static PropertyChangeListener configListener = new PropertyChangeListener() {
//
//        public void propertyChange(PropertyChangeEvent evt) {
//            // Log.staticAddConfigModules();
//            staticLoadConfig();
//        }
//    };

    public static void setConfig(LogConfig c) {
        if (c != config) {
//            if (config != null) {
//                config.removeConfigurationChangeListener(configListener);
//            }
            config = c;
//            if (config != null) {
//                config.addConfigurationChangeListener(configListener);
//            }
        }
    }

    public static LogConfig getConfig() {
        return config;
    }

    public static void init() {
        if (!isInitialized) {
            addMessageType(FATAL, CLIENT_TRACE_MSG_GROUP);
            addMessageType(ERROR, CLIENT_TRACE_MSG_GROUP);
            addMessageType(WARNING, CLIENT_TRACE_MSG_GROUP);
            addMessageType(TRACE, CLIENT_TRACE_MSG_GROUP);
            addMessageType(DEV_WARNING, DEV_PROCESS_MSG_GROUP);
            addMessageType(DEBUG, DEV_PROCESS_MSG_GROUP);
            addMessageType(BUG, DEV_DEPLOY_MSG_GROUP);
            addMessageType(METHOD, DEV_PROCESS_MSG_GROUP);
            addMessageType(METHOD_ENTER, DEV_PROCESS_MSG_GROUP);
            addMessageType(METHOD_EXIT, DEV_PROCESS_MSG_GROUP);

            logTimer = new LogThread();
            logTimer.start();
            // logTimer.processLog();
            getDefaultLogger();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

                public void run() {
                    Log.dispose();
                }
            }, "ShutdownHookLogDisposer"));
            isInitialized = true;
            staticLoadConfig();
        }
    }
    // Log instance

    public Log(String name, String title, String description, String logType,
            String pattern, String[] acceptedTypes) {
        this.name = name;
        this.title = title;
        this.logType = logType;
        this.description = description;
        this.pattern = pattern;
        enabled = true;
        privateSetAcceptedTypesWithoutSaving(acceptedTypes != null ? acceptedTypes
                : null);
    }

    protected void fireEvent(String propertyName, Object oldValue,
            Object newValue) {
        eventSupport.fireEvent(this, propertyName, oldValue, newValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((logType == null) ? 0 : logType.hashCode());
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Log other = (Log) obj;
        if (logType == null) {
            if (other.logType != null) {
                return false;
            }
        } else if (!logType.equals(other.logType)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public static void unsetDefaultLogger() {
        if (defaultLogger != null) {
            unregisterLogger(defaultLogger);
        }
    }

    public static void staticLoadConfig() {
        if (config != null) {
            Log.setMaxOnHoldMessagesCount(config.getInt(Log.getConfigurationGlobalKey("max_onhold_messages"), Log.getMaxOnHoldMessagesCount()));
            Log.setLogThreadSleepPeriod(config.getLong(Log.getConfigurationGlobalKey("log_thread_sleep_period"), Log.getLogThreadSleepPeriod()));
            Log.setGlobalEnabled(config.getBoolean(Log.getConfigurationGlobalKey("enable"), Log.isGlobalEnabled()));
        }
    }

    public void saveConfig() {
        if (config != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < acceptedMessageTypes.length; i++) {
                if (i > 0) {
                    sb.append(";");
                }
                sb.append(acceptedMessageTypes[i]);
            }
            config.setString(getConfigurationKey("accept"), sb.toString());
            if (dateFormat != null) {
                config.setString(getConfigurationKey("dateFormat"), dateFormat.toPattern());
            } else {
                config.setString(getConfigurationKey("dateFormat"), "");
            }
            config.setString(getConfigurationKey("pattern"), pattern);
            config.setBoolean(getConfigurationKey("enabled"), enabled);
        }
    }

    public abstract void clear();

    public abstract void processLog(String s, String s1, int level,
            Date date, long l, Thread thread, StackTrace stacktrace,
            String user_id, String user_name);

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title != null ? title : name;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public String getLogType() {
        return logType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public SimpleDateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        }
        return dateFormat;
    }

    public static String arrayToString(Object[] o) {
        if (o == null) {
            o = new Object[0];
        }
        StringBuffer sb = new StringBuffer("{");
        if (o != null && o.length > 0) {
            sb.append(o[0]);
            for (int i = 1; i < o.length; i++) {
                sb.append(',').append(o[i]);
            }

        }
        return sb.append('}').toString();
    }

    public void setDateFormat(String format) {
        if (format == null) {
            setDateFormat((SimpleDateFormat) null);
        } else {
            if (dateFormat != null && !dateFormat.toPattern().equals(format)) {
                setDateFormat(new SimpleDateFormat(format));
            }
        }
    }

    public void setDateFormat(SimpleDateFormat format) {
        dateFormat = format;
        if (isInitialized && config != null) {
            if (dateFormat != null) {
                config.setString(getConfigurationKey("dateFormat"), dateFormat.toPattern());
            } else {
                config.setString(getConfigurationKey("dateFormat"), "");
            }
        }
    }

    public boolean isEnabled() {
        return enabled && logGlobalEnabled;
    }

    public void setEnabled(boolean e) {
        enabled = e;
        if (isInitialized && config != null) {
            config.setBoolean(getConfigurationKey("enable"), enabled);
        }
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String newPattern) {
        if (newPattern != null) {
            pattern = newPattern;
            if (isInitialized && config != null) {
                config.setString(getConfigurationKey("pattern"), pattern);
            }
        }
    }

    // instance facility
    public String now() {
        return getDateFormat().format(new Date());
    }

    private void privateSetAcceptedTypesWithoutSaving(String[] types) {
        if (types == null || (types.length > 0 && "*".equals(types[0]))) {
            acceptedMessageTypes = ALL_ARRAY;
            privateAddAcceptAllTypes(this);
            privateRemoveAcceptType(this, null);
        } else {
            acceptedMessageTypes = types;
            privateRemoveAcceptAllTypes(this);
            privateRemoveAcceptType(this, null);
            for (int i = 0; i < types.length; i++) {
                privateAddAcceptType(this, types[i]);
            }

        }
    }

    // public String buildMessage(String type, String message, Date date, long
    // delta, Thread thread, StackTrace stack) {
    // return buildMessage(type, message, date, delta, thread, stack, name,
    // pattern);
    // }
    //
    public String buildMessage(String type, String message, int logLevel,
            Date date, long delta, Thread thread, StackTrace stack,
            String loggerName, String pattern, String user_id, String user_name) {
        StringBuffer sb = new StringBuffer();
        // int logLevel = 2;
        stack.setLevel(logLevel);
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            switch (c) {
                case 36: // '$'
                    i++;
                    c = pattern.charAt(i);
                    switch (c) {
                        case '$': // '$'
                            sb.append('$');
                            break;

                        case 'd': // 'd'
                            sb.append(getDateFormat().format(date));
                            break;

                        case '-': // 'D'
                            sb.append(DELTA_FORMAT.format(delta));
                            break;

                        case 'n': // 'g'
                            sb.append(loggerName);
                            break;
                        case 'N': // 'g'
                            sb.append(title);
                            break;

                        case 't': // 't'
                            sb.append(type);
                            break;

                        case 'm': // 'm'
                            sb.append(message);
                            break;

                        case 'l': // 'l'
                            sb.append(stack.getLineNumberString());
                            break;

                        case 'F': // 'F'
                            sb.append(stack.getFilename());
                            break;

                        case 'f': // 'f'
                            sb.append(stack.getFunction());
                            break;

                        case 'c': // 'c'
                            sb.append(stack.getShortClassname());
                            break;

                        case 'C': // 'C'
                            sb.append(stack.getClassname());
                            break;

                        case 'h': // 'h'
                            sb.append(thread.getName());
                            break;

                        case 'p': // 'p'
                            sb.append(thread.getPriority());
                            break;

                        case 'H': // 'H'
                            sb.append(thread.getThreadGroup());
                            break;
                        case 'U': // 'U'
                            sb.append(user_name);
                            break;
                        case 'u': // 'U'
                            sb.append(user_id);
                            break;

                        case 48: // '0'
                        case 49: // '1'
                        case 50: // '2'
                        case 51: // '3'
                        case 52: // '4'
                        case 53: // '5'
                        case 54: // '6'
                        case 55: // '7'
                        case 56: // '8'
                        case 57: // '9'
                            logLevel = c - 48;
                            stack.setLevel(c - 48);
                            break;
                        default:
                            throw new RuntimeException("Unexpected char " + c
                                    + " after $ in pattern.");
                    }
                    break;

                default:
                    sb.append(c);
                    break;
            }
        }

        return sb.toString();
    }

    // Log use
    public static void dev_warning(String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(DEV_WARNING, message, DEFAULT_LOG_LEVEL,
                    new StackTrace());
        }
    }

    public static void warning(String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(WARNING, message, DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void warning(Throwable message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(WARNING, StackTrace.toString(message),
                    DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void method_enter() {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(new Object[0], st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_enter(Object p1) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(new Object[]{p1},
                    st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_enter(Object p1, Object p2) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(
                    new Object[]{p1, p2}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_enter(Object p1, Object p2, Object p3) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(new Object[]{p1,
                        p2, p3}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_enter(Object p1, Object p2, Object p3, Object p4) {
        StackTrace st = new StackTrace();
        private_log(METHOD_ENTER, privateMethodString(new Object[]{p1, p2,
                    p3, p4}, st), DEFAULT_LOG_LEVEL, st);
    }

    public static void method_enter(Object p1, Object p2, Object p3, Object p4,
            Object p5) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(new Object[]{p1,
                        p2, p3, p4, p5}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_enter(Object p1, Object p2, Object p3, Object p4,
            Object p5, Object p6) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(new Object[]{p1,
                        p2, p3, p4, p5, p6}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit() {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(new Object[0], st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit(Object p1) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(new Object[]{p1},
                    st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit(Object p1, Object p2) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(
                    new Object[]{p1, p2}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit(Object p1, Object p2, Object p3) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(new Object[]{p1, p2,
                        p3}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit(Object p1, Object p2, Object p3, Object p4) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(new Object[]{p1, p2,
                        p3, p4}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit(Object p1, Object p2, Object p3, Object p4,
            Object p5) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(new Object[]{p1, p2,
                        p3, p4, p5}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit(Object p1, Object p2, Object p3, Object p4,
            Object p5, Object p6) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(new Object[]{p1, p2,
                        p3, p4, p5, p6}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method() {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD, privateMethodString(new Object[0], st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(int level) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            String msg = privateMethodString(new Object[0], st);
            st.setLevel(level);
            private_log(METHOD, msg, level, st);
        }
    }

    public static void method(Object p1) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD, privateMethodString(new Object[]{p1}, st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(Object p1, Object p2) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD,
                    privateMethodString(new Object[]{p1, p2}, st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(Object p1, Object p2, Object p3) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD, privateMethodString(
                    new Object[]{p1, p2, p3}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(Object p1, Object p2, Object p3, Object p4) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(new Object[]{p1,
                        p2, p3, p4}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(Object p1, Object p2, Object p3, Object p4,
            Object p5) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD, privateMethodString(new Object[]{p1, p2, p3,
                        p4, p5}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(Object p1, Object p2, Object p3, Object p4,
            Object p5, Object p6) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD, privateMethodString(new Object[]{p1, p2, p3,
                        p4, p5, p6}, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(Object[] params) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD, privateMethodString(params, st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method(String prefix, Object[] params) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD, (prefix == null ? "" : prefix)
                    + privateMethodString(params, st), DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_enter(Object[] params) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_ENTER, privateMethodString(params, st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    public static void method_exit(Object[] params) {
        if (logGlobalEnabled && logTimer != null) {
            StackTrace st = new StackTrace();
            private_log(METHOD_EXIT, privateMethodString(params, st),
                    DEFAULT_LOG_LEVEL, st);
        }
    }

    private static String privateMethodString(Object[] params, StackTrace stack) {
        stack.setLevel(1);
        String functionName = stack.getFunction();
        StringBuffer sb = new StringBuffer().append(functionName).append("(");
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                Object o = params[i];
                if (o != null && o.getClass().isArray()) {
                    sb.append(arrayToString((Object[]) o));
                } else {
                    sb.append(o);
                }
            }

        }
        sb.append(")");
        return sb.toString();
    }

    private static String[] toStringArray(String listedStrings, char separator) {
        ArrayList<String> values = new ArrayList<String>();
        StringBuffer current = new StringBuffer();
        boolean escape = false;
        for (int i = 0; i < listedStrings.length(); i++) {
            char c = listedStrings.charAt(i);
            if (c == separator && !escape) {
                String s = current.toString();
                if (s.length() > 0) {
                    values.add(fromSpecialString(current.toString()));
                    current.delete(0, current.length());
                }
            } else if (c == '\\' && !escape) {
                escape = true;
            } else if (escape) {
                current.append("\\").append(c);
                escape = false;
            } else {
                current.append(c);
            }
        }

        String s = current.toString();
        if (s.length() > 0) {
            values.add(fromSpecialString(current.toString()));
        }
        return (String[]) values.toArray(new String[0]);
    }

    public static void trace(Throwable message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(TRACE, StackTrace.toString(message), DEFAULT_LOG_LEVEL,
                    new StackTrace());
        }
    }

    public static void debug(String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(DEBUG, message, DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void log(String type, String message, Throwable throwable) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(type, message + "\n" + StackTrace.toString(throwable),
                    DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void error(String message, Throwable throwable) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(ERROR, message + "\n" + StackTrace.toString(throwable),
                    DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void warning(String message, Throwable throwable) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(WARNING, message + "\n"
                    + StackTrace.toString(throwable), DEFAULT_LOG_LEVEL,
                    new StackTrace());
        }
    }

    public static void log(String type, Throwable message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(type, StackTrace.toString(message), DEFAULT_LOG_LEVEL,
                    new StackTrace());
        }
    }

    public static void debug(Throwable message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(DEBUG, StackTrace.toString(message), DEFAULT_LOG_LEVEL,
                    new StackTrace());
        }
    }

    public static void bug(String bugId, String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(BUG, "{ID=" + bugId + "} " + message,
                    DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void bug(String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(BUG, message, DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void bug(Throwable message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(BUG, StackTrace.toString(message), DEFAULT_LOG_LEVEL,
                    new StackTrace());
        }
    }

    public static void error(String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(ERROR, message, DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void error(Throwable message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(ERROR, StackTrace.toString(message), DEFAULT_LOG_LEVEL,
                    new StackTrace());
        }
    }

    public static void trace(String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(TRACE, message, DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    public static void log(String type, String message) {
        if (logGlobalEnabled && logTimer != null) {
            private_log(type, message, DEFAULT_LOG_LEVEL, new StackTrace());
        }
    }

    private static void private_log(String type, String message, int logLevel,
            StackTrace trace) {
        // System.out.println("!log : ");
        if (logTimer == null) {
            return;
        }
        if (static_maxOnHoldMessagesCount > 0
                && logTimer.getOnHoldMessagesCount() > static_maxOnHoldMessagesCount) {
            if (doStopExcessiveStream) {
                return;
            }
            doStopExcessiveStream = true;
            type = INTERNAL;
            message = "\n"
                    + ("LOG STOPPED FOR A WHILE because Log Stream is too much rapid! I'm already logging "
                    + logTimer.getOnHoldMessagesCount()
                    + " but i can't accept more that "
                    + static_maxOnHoldMessagesCount + " messages at a time");
        } else {
            doStopExcessiveStream = false;
        }
        Date date = new Date();
        long delta = lastLogTime != 0L ? date.getTime() - lastLogTime : 0L;
        Thread thread = Thread.currentThread();
        if (trace == null) {
            trace = new StackTrace();
        }
        Message data = new Message(
                // type, message, new Integer(logLevel),date, new Long(delta),
                // thread, trace,static_current_user_id,static_current_user_name
                type, message, logLevel, date, delta,
                thread, trace, System.getProperty("application.user.id"),
                System.getProperty("application.user.name"));
        TreeSet<String> alreadylogged = new TreeSet<String>();
        synchronized (loggers) {
            if (INTERNAL.equals(type)) {
                for (Iterator i = loggers.entrySet().iterator(); i.hasNext();) {
                    Log l = (Log) ((java.util.Map.Entry) i.next()).getValue();
                    if (l.enabled) {
                        logTimer.addMessage(new LogMessage(l, data));
                        alreadylogged.add(l.getName());
                    }
                }

                Log d = getDefaultLogger();
                if (d.enabled && alreadylogged.size() == 0) {
                    logTimer.addMessage(new LogMessage(d, data));
                    alreadylogged.add(d.getName());
                }
            } else {
                for (Iterator i = loggersAcceptingAllTypesHashSet.iterator(); i.hasNext();) {
                    Log l = (Log) i.next();
                    if (l != getDefaultLogger() && l.enabled) {
                        logTimer.addMessage(new LogMessage(l, data));
                        alreadylogged.add(l.getName());
                    }
                }

                HashSet loggersAcceptingThisType = (HashSet) loggersByTypesMap.get(type);
                if (loggersAcceptingThisType != null) {
                    for (Iterator i = loggersAcceptingThisType.iterator(); i.hasNext();) {
                        Log l = (Log) i.next();
                        if (l != getDefaultLogger() && l.enabled
                                && loggers.containsKey(l.getName())
                                && !alreadylogged.contains(l.getName())) {
                            alreadylogged.add(l.getName());
                            logTimer.addMessage(new LogMessage(l, data));
                        }
                    }

                }
                if (alreadylogged.size() == 0) {
                    Log d = getDefaultLogger();
                    if (d != SILENT_LOGGER
                            && d.enabled
                            && (loggersAcceptingAllTypesHashSet.contains(d) || loggersAcceptingThisType != null
                            && loggersAcceptingThisType.contains(d))) {
                        alreadylogged.add(d.getName());
                        logTimer.addMessage(new LogMessage(d, data));
                    }
                }
            }
        }
        if (alreadylogged.size() > 0) {
            lastLogTime = date.getTime();
        }
        // else{
        // System.out.println("["+type+" : "+message+"] never logged ...");
        // dump();
        // }
    }

    public static void dispose() {
        if (logTimer != null) {
            Log.isDieing = true;
            logTimer.die();
            logTimer = null;
        }
        unregisterAllLoggers();
    }

    public static void kill() {
        if (logTimer != null) {
            logTimer.kill();
            logTimer = null;
        }
        unregisterAllLoggers();
    }

    public static Log getLogger(String name) {
        Log l = (Log) loggers.get(name);
        return l == null ? defaultLogger : l;
    }

    public boolean isDeclared(String loggerName) {
        return loggerName != null && loggers.containsKey(loggerName);
    }

    /*
     * private boolean accept(String type) { if (!enabled) return false; Log log
     * = this; if (loggersAcceptingAllTypesHashSet.contains(log)) return true;
     * HashSet hs = (HashSet) loggersByTypesMap.get(type); if (hs == null)
     * return false; else return hs.contains(log); }
     */
    public boolean isAcceptingAll() {
        return loggersAcceptingAllTypesHashSet.contains(this);
    }

    public void setAcceptedTypes(String[] types) {
        privateSetAcceptedTypesWithoutSaving(types);
        if (isInitialized && config != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < acceptedMessageTypes.length; i++) {
                if (i > 0) {
                    sb.append(";");
                }
                sb.append(acceptedMessageTypes[i]);
            }
            config.setString(getConfigurationKey("accept"), sb.toString());
        }
    }

    public void setAcceptedTypes(String types) {
        setAcceptedTypes(types != null ? toStringArray(types, ';') : null);
    }

    public String[] getAcceptedTypes() {
        if (isAcceptingAll()) {
            return new String[]{"*"};
        } else {
            return acceptedMessageTypes;
        }
    }

    public void activate() {
    }

    public void loadConfig() {
        if (config != null) {
            enabled = config.getBoolean(getConfigurationKey("enable"),
                    isEnabled());
            String p = config.getString(getConfigurationKey("pattern"));
            if (p != null) {
                pattern = p;
            }
            String v = config.getString(getConfigurationKey("accept"));
            if (v != null) {
                String[] all = v.trim().split(";");
                ArrayList<String> found = new ArrayList<String>();
                for (int i = 0; i < all.length; i++) {
                    String x = all[i].trim();
                    if (x.length() != 0) {
                        found.add(x);
                    }
                }
                privateSetAcceptedTypesWithoutSaving(found.toArray(new String[found.size()]));
            }
            String s = config.getString(getConfigurationKey("dateFormat"));
            if (s == null) {
                // do nothing
            } else if (s.length() == 0) {
                dateFormat = null;
            } else {
                dateFormat = new SimpleDateFormat(s);
            }
        }
    }

    public String getConfigurationKey(String key) {
        return "net.vpc.lib.pheromone.ariana.util.log." + getName() + "." + key;
    }

    public void passivate() {
    }

    public int compareTo(Object o) {
        Log other = (Log) o;
        return name.compareTo(other.name);
    }

    public String toString() {
        return super.toString() + "/" + getName();
    }

    public static void dump() {
        System.out.println("## LOG DUMP");
        System.out.println("  declared loggers      :" + loggers);
        System.out.println("  default logger        :" + defaultLogger);
        System.out.println("  logGlobalEnabled      :" + Log.logGlobalEnabled);
        System.out.println("  doStopExcessiveStream :"
                + Log.doStopExcessiveStream);
        System.out.println("  onHoldMessagesCount   :"
                + Log.logTimer.getOnHoldMessagesCount());
        System.out.println("  loggersAcceptingAllTypesHashSet   :"
                + loggersAcceptingAllTypesHashSet);
        System.out.println("  loggersByTypesMap                 :"
                + loggersByTypesMap);
        System.out.println("## END LOG DUMP");
    }

    public static void addMessageType(String logType, String group) {
        if (!messageType_groupMap.containsKey(logType)) {
            messageType_groupMap.put(logType, group);
            eventSupport.fireEvent(null, LogEvent.MESSAGE_TYPE_ADDED, null,
                    logType);
        }
    }

    public static void removeMessageType(String logType) {
        messageType_groupMap.remove(logType);
        eventSupport.fireEvent(null, LogEvent.MESSAGE_TYPE_REMOVED, null,
                logType);
    }

    public static Collection getMessageTypes() {
        return messageType_groupMap.keySet();
    }

    public static Collection getMessageTypeGroups() {
        TreeSet<String> ts = new TreeSet<String>();
        for (Enumeration<String> e = messageType_groupMap.elements(); e.hasMoreElements();) {
            ts.add(e.nextElement());
        }
        return ts;
    }

    public static Collection getMessageTypesByGroup(String group) {
        TreeSet<String> ts = new TreeSet<String>();
        for (Iterator i = messageType_groupMap.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            if (group.equals(e.getValue())) {
                ts.add((String) e.getKey());
            }
        }
        return ts;
    }

    public void addAcceptedMessageTypeGroup(String group) {
        if (acceptedMessageTypeGroups == null) {
            acceptedMessageTypeGroups = new HashSet<String>();
        }
        acceptedMessageTypeGroups.add(group);
    }

    public Collection getAcceptedMessageTypeGroups() {
        if (acceptedMessageTypeGroups == null) {
            return getMessageTypeGroups();
        } else {
            return (Collection) acceptedMessageTypeGroups.clone();
        }
    }

    public void removeAcceptedMessageTypeGroup(String group) {
        if (acceptedMessageTypeGroups != null) {
            acceptedMessageTypeGroups.remove(group);
        }
    }

    public boolean isAcceptingAllMessageTypeGroups() {
        return acceptedMessageTypeGroups == null;
    }

    public void setAcceptingAllMessageTypeGroups() {
        acceptedMessageTypeGroups = null;
    }

    public static int getMaxOnHoldMessagesCount() {
        return Log.static_maxOnHoldMessagesCount;
    }

    public static void setMaxOnHoldMessagesCount(int i) {
        Log.static_maxOnHoldMessagesCount = i;
        if (Log.isInitialized && config != null) {
            config.setInt(getConfigurationGlobalKey("max_onhold_messages"),
                    Log.static_maxOnHoldMessagesCount);
        }
    }

    public static long getLogThreadSleepPeriod() {
        return LogThread.static_logThreadSleepPeriod;
    }

    public static void setLogThreadSleepPeriod(long i) {
        LogThread.static_logThreadSleepPeriod = i;
        if (logTimer != null) {
            logTimer.setPeriod(i);
        }
        if (Log.isInitialized && config != null) {
            config.setLong(
                    getConfigurationGlobalKey("log_thread_sleep_period"),
                    LogThread.static_logThreadSleepPeriod);
        }
    }

    public static String getConfigurationGlobalKey(String key) {
        return "net.vpc.lib.pheromone.ariana.util.log.*." + key;
    }

    public static Log getDefaultLogger() {
        if (defaultLogger == null) {
            registerLogger(defaultLogger = getSystemLogger());
        }
        return defaultLogger;
    }

    public static Log getSystemLogger() {
        if (Log.SYSTEM_OUT_LOG == null) {
            Log.isInitialized = false;
            Log.SYSTEM_OUT_LOG = new PrintStreamLog(Log.DEFAULT_LOGGER,
                    Log.DEFAULT_LOGGER, Log.DEFAULT_LOGGER, Log.TYPE_CONSOLE,
                    Log.DEFAULT_PATTERN, Log.ALL_ARRAY, System.out);
            Log.isInitialized = true;
        }
        return Log.SYSTEM_OUT_LOG;
    }

    public static void setDefaultLogger(Log alogger) {
        // System.out.println("LOG : setDefaultLogger("+alogger.getName()+") old
        // one is "+defaultLogger);
        if (alogger == defaultLogger) {
            return;
        }
        if (defaultLogger != null) {
            eventSupport.fireEvent(null, LogEvent.DEFAULT_LOGGER_SET,
                    defaultLogger, alogger);

            // if (cache_globalConfigurator != null)
            // cache_globalConfigurator.remove(defaultLogger.getName());
            if (defaultLogger == Log.SYSTEM_OUT_LOG) {
                // System.out.println("remove SYSTEM_OUT_LOG as defaultLogger
                // ");
                Log toRemoveLogger = Log.SYSTEM_OUT_LOG;
                loggers.remove(toRemoveLogger.getName());
                privateRemoveAcceptAllTypes(toRemoveLogger);
                privateRemoveAcceptType(toRemoveLogger, null);
            } else {
                defaultLogger.passivate();
            }
        }
        defaultLogger = null;
        if (alogger != null) {
            defaultLogger = alogger;
            if (!loggers.containsKey(alogger.getName())) {
                registerLogger(alogger);
            }
        }
        // System.out.println("LOG : end setDefaultLogger : "+loggers+ " /
        // "+defaultLogger);
        // printLogInfos();
    }

    public static void registerLogger(Log logger) {
        String loggerName = logger.getName();
        // System.out.println("LOG : declare "+logger.getName()+" as
        // "+logger.getTitle()+" ("+logger.getClass().getName()+")");
        if (loggers.containsKey(logger.getName())) {
            throw new IllegalArgumentException("Logger name " + loggerName
                    + " in use! (" + loggers + ")");
        } else {
            loggers.put(loggerName, logger);
            logger.privateSetAcceptedTypesWithoutSaving(logger.getAcceptedTypes());
            // logger.loadConfig();
            logger.activate();
            eventSupport.fireEvent(logger, LogEvent.LOG_REGISTRED);
        }
    }

    public static void setGlobalEnabled(boolean e) {
        Log.logGlobalEnabled = e;
        if (Log.isInitialized && config != null) {
            config.setBoolean(getConfigurationGlobalKey("enable"),
                    Log.logGlobalEnabled);
        }
    }

    public static boolean isGlobalEnabled() {
        return Log.logGlobalEnabled;
    }

    public static void unregisterLogger(Log logger) {
        unregisterLogger(logger.getName());
    }

    public static void unregisterLogger(String name) {
        Log l = (Log) loggers.get(name);
        if (l != null) {
            loggers.remove(name);
            privateRemoveAcceptAllTypes(l);
            privateRemoveAcceptType(l, null);
            l.passivate();
            if (l == defaultLogger) {
                defaultLogger = SILENT_LOGGER;
            }
            eventSupport.fireEvent(l, LogEvent.LOG_UNREGISTRED);
        }

        // System.out.println("LOG : end unregisterLogger");
        // printLogInfos();
    }

    public static void unregisterAllLoggers() {
        // System.out.println("LOG : undeclare all loggers");
        Log l;
        for (Iterator i = loggers.entrySet().iterator(); i.hasNext();) {
            java.util.Map.Entry e = (java.util.Map.Entry) i.next();
            l = (Log) e.getValue();
            l.passivate();
            eventSupport.fireEvent(l, LogEvent.LOG_UNREGISTRED);
        }

        loggers.clear();
        loggersAcceptingAllTypesHashSet.clear();
        loggersByTypesMap.clear();
        defaultLogger = null;
        // System.out.println("LOG : end unregisterAllLoggers");
        // printLogInfos();
    }

    public static Collection<Log> getLoggers() {
        return loggers.values();
    }

    private static void privateAddAcceptType(Log logger, String type) {
        if (!loggers.containsKey(logger.getName())) {
            return;
        }
        HashSet<Log> hs = loggersByTypesMap.get(type);
        if (hs == null) {
            hs = new HashSet<Log>();
            loggersByTypesMap.put(type, hs);
        }
        hs.add(logger);
    }

    private static void privateAddAcceptAllTypes(Log logger) {
        if (!loggers.containsKey(logger.getName())) {
            return;
        }
        loggersAcceptingAllTypesHashSet.add(logger);
    }

    private static void privateRemoveAcceptType(Log logger, String type) {
        // if(!loggers.containsKey(logger.getName())) return;
        if (type == null) {
            HashSet m;
            ArrayList<String> toRemove = new ArrayList<String>();
            for (Iterator<Map.Entry<String, HashSet<Log>>> i = loggersByTypesMap.entrySet().iterator(); i.hasNext();) {
                Map.Entry<String, HashSet<Log>> e = i.next();
                m = (HashSet<Log>) e.getValue();
                m.remove(logger);
                if (m.isEmpty()) {
                    toRemove.add(e.getKey());
                }
            }
            for (int i = toRemove.size() - 1; i >= 0; i--) {
                loggersByTypesMap.remove(toRemove.get(i));
            }
        } else {
            Map m = (Map) loggersByTypesMap.get(type);
            m.remove(logger);
        }
    }

    private static void privateRemoveAcceptAllTypes(Log logger) {
        // if(!loggers.containsKey(logger.getName())) return;
        loggersAcceptingAllTypesHashSet.remove(logger);
    }

    public static int getOnHoldMessagesCount() {
        return logTimer.getOnHoldMessagesCount();
    }

    public static boolean isDieing() {
        return isDieing;
    }

    public static void addLogListener(String property, LogListener listener) {
        eventSupport.addLogListener(property, listener);
    }

    public void removeLogListener(String property, LogListener listener) {
        eventSupport.removeLogListener(property, listener);
    }

    public static String fromSpecialString(String theString) {
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            char aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case 48: // '0'
                            case 49: // '1'
                            case 50: // '2'
                            case 51: // '3'
                            case 52: // '4'
                            case 53: // '5'
                            case 54: // '6'
                            case 55: // '7'
                            case 56: // '8'
                            case 57: // '9'
                                value = ((value << 4) + aChar) - 48;
                                break;

                            case 97: // 'a'
                            case 98: // 'b'
                            case 99: // 'c'
                            case 100: // 'd'
                            case 101: // 'e'
                            case 102: // 'f'
                                value = ((value << 4) + 10 + aChar) - 97;
                                break;

                            case 65: // 'A'
                            case 66: // 'B'
                            case 67: // 'C'
                            case 68: // 'D'
                            case 69: // 'E'
                            case 70: // 'F'
                                value = ((value << 4) + 10 + aChar) - 65;
                                break;

                            case 58: // ':'
                            case 59: // ';'
                            case 60: // '<'
                            case 61: // '='
                            case 62: // '>'
                            case 63: // '?'
                            case 64: // '@'
                            case 71: // 'G'
                            case 72: // 'H'
                            case 73: // 'I'
                            case 74: // 'J'
                            case 75: // 'K'
                            case 76: // 'L'
                            case 77: // 'M'
                            case 78: // 'N'
                            case 79: // 'O'
                            case 80: // 'P'
                            case 81: // 'Q'
                            case 82: // 'R'
                            case 83: // 'S'
                            case 84: // 'T'
                            case 85: // 'U'
                            case 86: // 'V'
                            case 87: // 'W'
                            case 88: // 'X'
                            case 89: // 'Y'
                            case 90: // 'Z'
                            case 91: // '['
                            case 92: // '\\'
                            case 93: // ']'
                            case 94: // '^'
                            case 95: // '_'
                            case 96: // '`'
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }

                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }

        return outBuffer.toString();
    }
}

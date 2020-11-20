package net.thevpc.common.util;

import java.util.logging.*;
import java.util.logging.Filter;

/**
 * @author thevpc
 * %creationtime 9/16/12 10:00 PM
 */
public class LogUtils {
    public static boolean logEnabled=true;
    
    public static void configure(Level level,String... packages){
        java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");
        rootLogger.setLevel(level);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(level);
            handler.setFormatter(new CustomTextFormatter());
            handler.setFilter(new FilterImpl(packages));
        }
    }

    private static class FilterImpl implements Filter {
        String[] prefixes; 
        public FilterImpl(String[] prefixes) {
            this.prefixes=prefixes;
            for (int i = 0; i < prefixes.length; i++) {
                String p = prefixes[i];
                if(p.length()>0){
                    if(!p.endsWith(".")){
                        p+=".";
                        prefixes[i]=p;
                    }
                }
            }
        }

        @Override
        public boolean isLoggable(LogRecord record) {
            if(logEnabled){
                String logname = record.getLoggerName();
                for (String p : prefixes) {
                    if(logname.startsWith(p)){
                        return true;
                    }
                }
                
            }
//                    return record.getLoggerName().startsWith("*net.thevpc.upa.impl.persistence.DefaultUConnection");
            return false;
        }
    }
}

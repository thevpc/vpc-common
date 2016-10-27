/*
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 20 sept. 02
 * Time: 22:00:40
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package net.vpc.common.log;


import java.util.Date;

public class SilentLog extends Log {
    public SilentLog() {
        super("Silent", "Silent", "Silent", "Silent", "", new String[0]);
    }

    public void processLog(String s, String s1, int logLevel, Date date, long l, Thread thread, StackTrace stacktrace, String user_id, String user_name) {
        // do nothing
    }

    public void clear() {
        // do nothing
    }
}

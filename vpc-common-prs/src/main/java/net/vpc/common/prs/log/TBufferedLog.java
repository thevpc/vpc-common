/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.prs.log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author vpc
 */
public class TBufferedLog implements TLog {

    private boolean closed;
    private TLog base;
    private LinkedBlockingQueue<LogMethod> queue;
    private final static HashSet<TBufferedLog> queues = new HashSet<TBufferedLog>(10);

    static {
        Timer t = new Timer(true);
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                synchronized (queues) {
                    for (Iterator<TBufferedLog> i = queues.iterator(); i.hasNext();) {
                        TBufferedLog n = i.next();
                        LogMethod r = null;
                        while ((r = n.queue.poll()) != null) {
                            r.run();
                        }
                        if (n.closed) {
                            i.remove();
                        }
                    }

                }
            }
        }, 1000, 5 * 1000);
    }

    public TBufferedLog(TLog base) {
        this(base,-1);
    }
    
    public TBufferedLog(TLog base, int queueSize) {
        this.base = base;
        queue = new LinkedBlockingQueue<LogMethod>(queueSize <= 0 ? Integer.MAX_VALUE : queueSize);
        synchronized (queues) {
            queues.add(this);
        }
    }

    @Override
    public void close() {
        try {
            synchronized (queues) {
                queue.put(new CloseLogMethod(this));
            }
        } catch (InterruptedException ex) {
            //ignore
        }
    }

    @Override
    public void debug(Object msg) {
        try {
            synchronized (queues) {
                queue.put(new DebugLogMethod(msg, base));
            }
        } catch (InterruptedException ex) {
            //ignore
        }
    }

    @Override
    public void error(Object msg) {
        try {
            synchronized (queues) {
                queue.put(new ErrorLogMethod(msg, base));
            }
        } catch (InterruptedException ex) {
            //ignore
        }
    }

    @Override
    public void error(String message, Object msg) {
        try {
            synchronized (queues) {
                queue.put(new Error2LogMethod(message, msg, base));
            }
        } catch (InterruptedException ex) {
            //ignore
        }
    }

    @Override
    public void trace(Object msg) {
        try {
            synchronized (queues) {
                queue.put(new TraceLogMethod(msg, base));
            }
        } catch (InterruptedException ex) {
            //ignore
        }
    }

    @Override
    public void warning(Object msg) {
        try {
            synchronized (queues) {
                queue.put(new WarningLogMethod(msg, base));
            }
        } catch (InterruptedException ex) {
            //ignore
        }
    }

    private static abstract class LogMethod {

        protected TLog base;

        public LogMethod(TLog base) {
            this.base = base;
        }

        public abstract void run();
    }

    private static class CloseLogMethod extends LogMethod {

        TBufferedLog log;

        public CloseLogMethod(TBufferedLog log) {
            super(log.base);
            this.log = log;
        }

        public void run() {
            base.close();
            log.closed = true;
        }
    }

    private static class DebugLogMethod extends LogMethod {

        Object obj;

        public DebugLogMethod(Object obj, TLog base) {
            super(base);
            this.obj = obj;
        }

        public void run() {
            base.debug(obj);
        }
    }

    private static class TraceLogMethod extends LogMethod {

        Object obj;

        public TraceLogMethod(Object obj, TLog base) {
            super(base);
            this.obj = obj;
        }

        public void run() {
            base.trace(obj);
        }
    }

    private static class WarningLogMethod extends LogMethod {

        Object obj;

        public WarningLogMethod(Object obj, TLog base) {
            super(base);
            this.obj = obj;
        }

        public void run() {
            base.warning(obj);
        }
    }

    private static class ErrorLogMethod extends LogMethod {

        Object obj;

        public ErrorLogMethod(Object obj, TLog base) {
            super(base);
            this.obj = obj;
        }

        public void run() {
            base.error(obj);
        }
    }

    private static class Error2LogMethod extends LogMethod {

        String message;
        Object obj;

        public Error2LogMethod(String message, Object obj, TLog base) {
            super(base);
            this.message = message;
            this.obj = obj;
        }

        public void run() {
            base.error(message, obj);
        }
    }
}

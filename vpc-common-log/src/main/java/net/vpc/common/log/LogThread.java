package net.vpc.common.log;

import java.util.LinkedList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 15:48:45
 * To change this template use File | Settings | File Templates.
 */
class LogThread extends Thread {

    static long static_logThreadSleepPeriod = 4000L;
    private long period;
    private boolean killed;
    private static final LinkedList<LogMessage> pipe = new LinkedList<LogMessage>();

    LogThread() {
        setDaemon(true);
        setPeriod(static_logThreadSleepPeriod);
        //setPriority(1);
        setName("LoggerThread(sleep:" + static_logThreadSleepPeriod + ",prio:" + 1 + ")");
    }

    public void run() {
        //System.out.println("Thread " + getName() + " started with period = "+period+" at " + new Date());
        try {
            do {
                if (getPeriod() == 0L) {
                    //System.out.println("Thread " + getName() + " stopped as period = 0 at " + new Date());
                    break;
                }
                Thread.sleep(getPeriod());
                try {
                    if (!execute()) {
                        break;
                    }
                    //System.out.println("Thread " + getName() + " stopped as excution finished");
                } catch (Throwable e) {
                    //System.out.println("Thread " + getName() + " encountred exception " + e + " :{***************");
                    e.printStackTrace();
                    //System.out.println("***************}");
                }
            } while (true);
        } catch (InterruptedException e) {
            System.out.println("Thread " + getName() + " interrupted at " + new Date());
        }
    }

    protected boolean execute() {
        if (killed) {
            return false;
        }
        while (true) {
            if (!processLog()) {
                return true;
            }
        }

    }

    public void processOnHoldMessages() {
        while (true) {
            if (!processLog()) {
                return;
            }
        }
    }

    public int getOnHoldMessagesCount() {
        synchronized (pipe) {
            return pipe.size();
        }
    }

    public void addMessage(LogMessage msg) {
//            System.out.println("!M");
        synchronized (pipe) {
            pipe.addLast(msg);
        }
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void die() {
        processOnHoldMessages();
        setPeriod(0L);
    }

    public void kill() {
        killed = true;
        setPeriod(0L);
    }

    @Override
    public void finalize()
            throws Throwable {
        die();
        super.finalize();
    }

    private boolean processLog() {
        LogMessage data = null;
        synchronized (pipe) {
            if (!pipe.isEmpty()) {
//                System.out.println(new Date()+"/"+getPeriod()+" : log processed "+pipe.size());
                data = pipe.removeFirst();
            } else {
                return false;
            }
        }
        Message m = data.msg;
        data.logger.processLog(m.type, m.message, m.logLevel, m.date, m.delta, m.thread, m.stack, m.user_id, m.user_name);
        return true;
    }
}

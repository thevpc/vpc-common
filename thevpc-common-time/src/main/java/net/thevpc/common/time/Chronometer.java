/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.time;

import java.io.Serializable;

/**
 * @author taha.bensalah@gmail.com
 */
public class Chronometer implements Serializable {

    private final static long serialVersionUID = 1L;
    private long accumulated;
    private long startDate;
    private long endDate;
    private String name;
    private long lastTime;
    private boolean running;

//    public static void main(String[] args) {
//        Chronometer c = Chronometer.start();
//        long a = System.currentTimeMillis();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        c.stop();
//        long b = System.currentTimeMillis();
//        System.out.println(c.getDuration().getTime());
//        System.out.println(c.getDuration().getSeconds());
//        System.out.println(c.getDuration());
//        System.out.println(b - a);
//    }

    public static Chronometer start() {
        Chronometer c = new Chronometer();
        c.startNow();
        return c;
    }

    public static Chronometer start(String name) {
        Chronometer c = new Chronometer(name);
        c.startNow();
        return c;
    }

    private Chronometer() {
    }

    public Chronometer copy() {
        Chronometer c = new Chronometer();
        c.name = name;
        c.endDate = endDate;
        c.startDate = startDate;
        c.accumulated = accumulated;
        c.lastTime = lastTime;
        c.running = running;
        return c;
    }

    /**
     * restart chronometer and returns a stopped snapshot/copy of the current
     *
     * @return
     */
    public Chronometer restart() {
        stop();
        Chronometer c = copy();
        startNow();
        return c;
    }

    /**
     * restart chronometer with new name and returns a stopped snapshot/copy of
     * the current (with old name)
     *
     * @param newName
     * @return
     */
    public Chronometer restart(String newName) {
        stop();
        Chronometer c = copy();
        setName(newName);
        startNow();
        return c;
    }

    public Chronometer(String name) {
        this.name = name;
        startNow();
    }

    public Chronometer setName(String desc) {
        this.name = desc;
        return this;
    }

    public Chronometer updateDescription(String desc) {
        setName(desc);
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isStarted() {
        return startDate != 0;
    }

    public boolean isStopped() {
        return endDate != 0;
    }

    public Chronometer startNow() {
        endDate = 0;
        startDate = System.nanoTime();
        lastTime = startDate;
        accumulated = 0;
        running = true;
        return this;
    }

    public Chronometer accumulate() {
        if (running) {
            long n = System.nanoTime();
            accumulated += n - lastTime;
            lastTime = n;
        }
        return this;
    }

    public TimeDuration lap() {
        if (running) {
            long n = System.nanoTime();
            long lapValue = n - lastTime;
            this.accumulated += lapValue;
            lastTime = n;
            return TimeDuration.ofNanos(lapValue);
        }
        return TimeDuration.ZERO;
    }

    public boolean isSuspended() {
        return !running;
    }

    public Chronometer suspend() {
        if (running) {
            long n = System.nanoTime();
            accumulated += n - lastTime;
            lastTime = -1;
            running = false;
        }
        return this;
    }

    public Chronometer resume() {
        if (!running) {
            lastTime = System.nanoTime();
            running = true;
        }
        return this;
    }

    public Chronometer stop() {
        if (running) {
            endDate = System.nanoTime();
            accumulated += endDate - lastTime;
            lastTime = -1;
            running = false;
        }
        return this;
    }

    public long getStartTime() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public TimeDuration getDuration() {
        if (startDate == 0) {
            return TimeDuration.ZERO;
        }
        if (running) {
            long curr = System.nanoTime() - lastTime;
            return TimeDuration.ofNanos(curr + accumulated);
        }
        return TimeDuration.ofNanos(accumulated);

    }

    public long getTime() {
        if (startDate == 0) {
            return 0;
        }
        if (running) {
            long curr = System.nanoTime() - lastTime;
            return (curr + accumulated);
        }
        return accumulated;
    }

    public String toString() {
        String s = name == null ? "" : name + "=";
        return s + getDuration().toString();
    }

    public String toString(DatePart precision) {
        String s = name == null ? "" : name + "=";
        return s + getDuration().toString(precision);
    }
}

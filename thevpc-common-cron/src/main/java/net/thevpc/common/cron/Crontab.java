/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 *
 * @author thevpc
 */
public class Crontab {

    private List<CronTask> rows = new ArrayList<CronTask>();
    private Timer timer;
    private Executor executor = Executors.newScheduledThreadPool(3);

    public Timer start() {
        timer = new Timer("crontab", true);
        int ms = Calendar.getInstance().get(Calendar.MILLISECOND);
        int s = Calendar.getInstance().get(Calendar.SECOND);
        long wait = (59 - s) * 1000 + (1000 - ms);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                for (CronTask row : rows.toArray(new CronTask[0])) {
                    if (row.getCron().accept(now)) {
                        row.getTask().run();
                    }
                }
            }

        }, wait, 60*1000);
        return timer;
    }

    public void perform(Runnable row) {
        executor.execute(row);
    }

    public Crontab add(Cron cron, Runnable task) {
        return add(new CronTask(cron, task));
    }

    public Crontab add(String cron, Runnable task) {
        return add(new CronTask(cron, task));
    }

    public CronTask get(int index) {
        return rows.get(index);
    }

    public Crontab remove(int index) {
        rows.remove(index);
        return this;
    }

    public Crontab add(CronTask task) {
        if (task == null) {
            throw new NullPointerException("Task cannnot be null");
        }
        rows.add(task);
        return this;
    }

    public int size() {
        return rows.size();
    }

    public CronTask[] toArray() {
        return rows.toArray(new CronTask[0]);
    }

    public Stream<CronTask> stream() {
        return rows.stream();
    }

    public List<CronTask> toList() {
        return new ArrayList<>(rows);
    }
}

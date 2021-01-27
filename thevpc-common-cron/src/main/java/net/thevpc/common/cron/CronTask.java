/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.cron;

import java.util.Objects;

/**
 *
 * @author thevpc
 */
public class CronTask {

    private Cron cron;
    private Runnable task;

    public CronTask(String cron, Runnable task) {
        this(new Cron(cron), task);
    }

    public CronTask(Cron cron, Runnable task) {
        if (cron == null) {
            throw new NullPointerException("Cron cannnot be null");
        }
        if (task == null) {
            throw new NullPointerException("Task cannnot be null");
        }
        this.cron = cron;
        this.task = task;
    }

    public Cron getCron() {
        return cron;
    }

    public Runnable getTask() {
        return task;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.cron);
        hash = 41 * hash + Objects.hashCode(this.task);
        return hash;
    }

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
        final CronTask other = (CronTask) obj;
        if (!Objects.equals(this.cron, other.cron)) {
            return false;
        }
        if (!Objects.equals(this.task, other.task)) {
            return false;
        }
        return true;
    }

}

/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.swings.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public abstract class TaskMonitor {
    public static final int INDEX_PARAM = 0;
    public static final int MAX_PARAM = 1;
    public static final int PERCENT_PARAM = 2;
    public static final TaskMonitor NONE = new TaskMonitor() {
        long index;
        long max;

        public void progressImpl(long index, String progressKey, Object[] progressParams) {
            this.index = index;
        }

        public long getIndex() {
            return index;
        }

        public long getMax() {
            return max;
        }

        public void setMax(long max) {
            this.max = max;
        }
    };
    private TaskMonitor child;
    private TaskMonitor parent;
    private int level;
    private boolean indeterminate;
    private String taskLabel;
    private boolean stopped;
    private Stack<LevelStruct> deeps = new Stack<LevelStruct>();

    public TaskMonitor() {
    }

    public TaskMonitor(TaskMonitor child) {
        this();
        setChild(child);
    }

    public void setChild(TaskMonitor child) {
        if (this.child != null) {
            this.child.parent = null;
            this.child.level = 0;
            TaskMonitor tm = this.child.child;
            while (tm != null) {
                tm.level = tm.parent.level + 1;
                tm = tm.child;
            }
        }
        this.child = child;
        if (this.child != null) {
            this.child.parent = this;
            this.child.level = level + 1;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public boolean isIndeterminate() {
        return this.indeterminate;
    }

    public TaskMonitor getChild() {
        return this.child;
    }

    public TaskMonitor getParent() {
        return this.parent;
    }

    public int getLevel() {
        return this.level;
    }


    public abstract void setMax(long max);

    public abstract long getMax();

    public TaskMonitor[] getHierarchy() {
        ArrayList<TaskMonitor> v = new ArrayList<TaskMonitor>();
        TaskMonitor tm = this;
        while (tm != null) {
            v.add(tm);
            tm = tm.child;
        }
        return v.toArray(new TaskMonitor[0]);
    }

    private static DecimalFormat $DF$ = new DecimalFormat("#0.00%");

    public final void progress(long index, String progressKey, Object[] progressParams) {
        Object[] newParams = new Object[3 + ((progressParams == null) ? 0 : progressParams.length)];
        newParams[INDEX_PARAM] = index;
        newParams[MAX_PARAM] = getMax();
        newParams[PERCENT_PARAM] =
                //(getIndex() +"="+ index+"/"+getMax()+":")+
                $DF$.format(((double) index) / getMax());
        if (((double) index) / getMax() > 1) {
//            System.out.println("why");
        }
        if (progressParams != null) {
            System.arraycopy(progressParams, 0, newParams, 3, progressParams.length);
        }
        progressImpl(index, progressKey, newParams);
    }

    public abstract void progressImpl(long index, String progressKey, Object[] progressParams);

    public void progress(String progressKey, Object[] progressParams) {
        progress(getIndex(), progressKey, progressParams);
    }

    public void next(String progressKey, Object[] progressParams) {
        progress(getIndex() + 1, progressKey, progressParams);
    }

    public void next() {
        progress(getIndex() + 1, null, null);
    }

    public void progress(long index) {
        progress(index, null, null);
    }

    public abstract long getIndex();

    public String getTaskLabel() {
        return taskLabel;
    }

    public void setTaskLabel(String taskLabel) {
        this.taskLabel = taskLabel;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
        if (!stopped) {
            progress(0, "TaskMonitor.StartProcess", null);
        } else {
            progress(getMax(), "TaskMonitor.EndProcess", null);
        }
        if (this.child != null) {
            this.child.setStopped(stopped);
        }
    }

    public void reset() {
        setStopped(false);
    }


    public void stepInto(long newMax, String key, Object[] objects) {
        if (newMax > 0) {
            long oldMax = getMax();
            long oldIndex = getIndex();
            setMax(oldMax * newMax);
            deeps.push(new LevelStruct(oldMax, key, objects));
            progress(oldIndex * newMax, key + ".stepInto", objects);
        } else {
            deeps.push(new LevelStruct(0L, key, objects));
        }
    }

    public void stepOut() {
        LevelStruct oldMax = deeps.pop();
        if (oldMax.max == 0) {
            return;
        }
        long oldIndex = getIndex();
        long newMax = oldMax.max == 0 ? getMax() : (getMax() / oldMax.max);
        long newIndex = newMax == 0 ? 0 : (oldIndex / newMax);
        setMax(oldMax.max);
        progress(newIndex, oldMax.key + ".stepOut", oldMax.progressParams);
    }

    private static class LevelStruct {
        long max;
        String key;
        Object[] progressParams;

        public LevelStruct(long max, String key, Object[] progressParams) {
            this.max = max;
            this.key = key;
            this.progressParams = progressParams;
        }
    }
}

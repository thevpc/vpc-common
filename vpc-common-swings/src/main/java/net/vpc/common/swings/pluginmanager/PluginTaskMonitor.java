/**
 * ====================================================================
 *                        vpc-swingext library
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
package net.vpc.common.swings.pluginmanager;

import net.vpc.common.swings.util.TaskMonitor;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 21 oct. 2007 00:30:43
 */
public class PluginTaskMonitor extends TaskMonitor {
    private JProgressBar jpb = new JProgressBar(0, 100);
    private final Vector<Component> comps = new Vector<Component>();
    private Hashtable<Integer, Integer> running = new Hashtable<Integer, Integer>();
    private int lastId = 1000;

    public JComponent getComponent() {
        return jpb;
    }

    public long getIndex() {
        return jpb.getValue();
    }

    public long getMax() {
        return jpb.getMaximum();
    }

    public void progressImpl(long index, String progressKey, Object[] progressParams) {
        jpb.setValue((int) index);
    }

    @Override
    public void setIndeterminate(boolean indeterminate) {
        super.setIndeterminate(indeterminate);
        jpb.setIndeterminate(indeterminate);
    }

    public void setMax(long max) {
        jpb.setMaximum((int) max);
    }

    public void setVisible(boolean b) {
        //jpb.setVisible(b);
    }


    public synchronized int startIndeterminate() {
        synchronized (comps) {
            for (Component comp : comps) {
                comp.setEnabled(false);
            }
        }
        //jpb.setVisible(true);
        jpb.setIndeterminate(false);
        lastId++;
        running.put(lastId, lastId);
        return lastId;
    }

    public synchronized int start() {
        for (Component comp : comps) {
            comp.setEnabled(false);
        }
        //jpb.setVisible(true);
        jpb.setIndeterminate(false);
        lastId++;
        running.put(lastId, lastId);
        return lastId;
    }

    public synchronized void stop(int id) {
        Integer integer = running.remove(id);
        if (integer != null) {
            for (Component comp : comps) {
                comp.setEnabled(true);
            }
            if (running.size() == 0) {
                //jpb.setVisible(false);
                jpb.setIndeterminate(false);
            }
        }
    }

    public synchronized void add(Component cmp) {
        comps.add(cmp);
    }

    public boolean isRunning() {
        return running.size()>0;
    }
}

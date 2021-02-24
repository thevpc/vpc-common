/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.swings.pluginmanager;

import net.thevpc.common.swings.util.TaskMonitor;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 21 oct. 2007 00:30:43
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

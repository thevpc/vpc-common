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
package net.thevpc.common.swings;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 13 juil. 2006 22:14:21
 */
public abstract class TimerLabel extends JDropDownLabel {

    private Timer timer;
    private int period = 1000;

    public TimerLabel() {
        super();
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return this.period;
    }

    public void start() {
        timer = new Timer(period, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tic();
            }
        });
        timer.start();
    }

    public void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    protected abstract void tic();

    @Override
    protected void finalize()
            throws Throwable {
        super.finalize();
        if (timer != null) {
            if (timer.isRunning()) timer.stop();
            timer = null;
        }
    }

}

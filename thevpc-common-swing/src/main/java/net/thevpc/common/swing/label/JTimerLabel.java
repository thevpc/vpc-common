/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc] Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br> ====================================================================
 */
package net.thevpc.common.swing.label;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 13 juil. 2006
 * 22:14:21
 */
public abstract class JTimerLabel extends JDropDownLabel {

    private Timer timer;
    private int period = 1000;

    public JTimerLabel() {
        super();
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriod() {
        return this.period;
    }

    public JTimerLabel start() {
        if (timer == null) {
            timer = new Timer(period, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tic();
                }
            });
            timer.start();
        }
        return this;
    }

    public JTimerLabel stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        return this;
    }

    protected abstract void tic();

    @Override
    protected void finalize()
            throws Throwable {
        super.finalize();
        if (timer != null) {
            if (timer.isRunning()) {
                timer.stop();
            }
            timer = null;
        }
    }

}

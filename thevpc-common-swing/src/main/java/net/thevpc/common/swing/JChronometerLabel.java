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
package net.thevpc.common.swing;

import net.thevpc.common.swing.util.Chronometer;

import java.text.MessageFormat;
import java.util.Date;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 13 juil. 2006
 * 22:14:21
 */
public class JChronometerLabel extends JTimerLabel {

    Chronometer chronometer;
    private String messagePattern;

    public JChronometerLabel() {
        setText(" ");
        chronometer = new Chronometer();
    }

    public JChronometerLabel start() {
        chronometer.start();
        tic();
        super.start();
        return this;
    }

    public JChronometerLabel stop() {
        chronometer.stop();
        tic();
        super.stop();
        return this;
    }

    protected void tic() {
        setText(messagePattern == null
                ? chronometer.formatPeriodFixed(Chronometer.DatePart.ms, Chronometer.DatePart.h)
                : MessageFormat.format(messagePattern,
                        chronometer.toString(),
                        new Date(chronometer.getStartDate()),
                        String.valueOf(chronometer.getHours()),
                        String.valueOf(chronometer.getMinutes()),
                        String.valueOf(chronometer.getSeconds()),
                        String.valueOf(chronometer.getMilliSeconds()))
        );
    }

    public String getMessagePattern() {
        return messagePattern;
    }

    public void setMessagePattern(String messagePattern) {
        this.messagePattern = messagePattern;
        if (chronometer.isStarted()) {
            tic();
        }
    }
}

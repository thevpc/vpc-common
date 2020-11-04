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

import net.thevpc.common.swings.util.Chronometer;

import java.text.MessageFormat;
import java.util.Date;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public class JChronometerLabel extends TimerLabel {
    Chronometer chronometer;
    private String messagePattern;

    public JChronometerLabel() {
        setText(" ");
        chronometer = new Chronometer();
    }

    public void start() {
        chronometer.start();
        tic();
        super.start();
    }

    public void stop() {
        chronometer.stop();
        tic();
        super.stop();
    }

    protected void tic() {
        setText(messagePattern == null ?
                chronometer.formatPeriodFixed(Chronometer.DatePart.ms, Chronometer.DatePart.h) :
                MessageFormat.format(messagePattern,
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

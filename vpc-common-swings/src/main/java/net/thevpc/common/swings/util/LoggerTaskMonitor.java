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

import java.io.PrintStream;
import java.text.MessageFormat;

/**
 * @author vpc
 *         Date: 29 janv. 2005
 *         Time: 12:48:28
 */
public class LoggerTaskMonitor extends TaskMonitor {
    private long max;
    private long index;
    private long lastLogTime;
    private long logInterval;
    private PrintStream out;

    public LoggerTaskMonitor(long logInterval, PrintStream out) {
        this.logInterval = logInterval;
        lastLogTime = -1;
        this.out = out;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMax() {
        return max;
    }

    public void progressImpl(long index, String progressKey, Object[] progressParams) {
        this.index = index;
        long nowTime = System.currentTimeMillis();
        if (index == 0 || index == max || lastLogTime < 0 || logInterval <= 0 || (nowTime - lastLogTime) >= logInterval)
        {
            lastLogTime = nowTime;
            String str = MessageFormat.format(progressKey,progressParams);
            out.println(progressParams[PERCENT_PARAM] + " : " + str);
        }

    }

    public long getIndex() {
        return index;
    }
}

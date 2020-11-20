/**
 * ====================================================================
 *                        vpc-commons library
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
package net.thevpc.common.swings.util;

import java.text.DecimalFormat;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 13 juil. 2006 22:14:21
 */
public class Chronometer {

    public static enum DatePart {

        h,
        mn,
        s,
        ms
    }
    private long startDate;
    private long endDate;
    private String desc;

    public Chronometer() {
    }

    public Chronometer(String desc) {
        this.desc = desc;
    }

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public String getDescription() {
        return desc;
    }

    public boolean isStarted() {
        return startDate != 0 && endDate == 0;
    }

    public boolean isStopped() {
        return endDate == 0;
    }

    public Chronometer start() {
        endDate = 0;
        startDate = System.currentTimeMillis();
        return this;
    }

    public void stop() {
        endDate = System.currentTimeMillis();
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public long getTime() {
        return ((endDate <= 0) ? System.currentTimeMillis() : endDate) - startDate;
    }

    public int getMilliSeconds() {
        return (int) (getTime() % 1000L);
    }

    public int getSeconds() {
        return (int) ((getTime() % 60000L) / 1000L);
    }

    public int getMinutes() {
        return (int) ((getTime() % (1000L * 60L * 60L)) / 60000L);
    }

    public int getHours() {
        //old 0x36ee80L
        return (int) (getTime() / (1000L * 60L * 60L));
    }

    public static String formatPeriod(long period) {
        StringBuffer sb = new StringBuffer();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);

        if (h > 0) {
            sb.append(h).append(" h ");
            started = true;
        }
        if (mn > 0 || started) {
            sb.append(mn).append(" mn ");
            started = true;
        }
        if (s > 0 || started) {
            sb.append(s).append(" s ");
        //started=true;
        }
        sb.append(ms).append(" ms");
        return sb.toString();
    }

    public static String formatPeriod(long period, DatePart precision) {
        StringBuffer sb = new StringBuffer();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);
        if (precision.ordinal() >= DatePart.h.ordinal()) {
            if (h > 0) {
                sb.append(h).append(" h ");
                started = true;
            }
            if (precision.ordinal() >= DatePart.mn.ordinal()) {
                if (mn > 0 || started) {
                    sb.append(mn).append(" mn ");
                    started = true;
                }
                if (precision.ordinal() >= DatePart.s.ordinal()) {
                    if (s > 0 || started) {
                        sb.append(s).append(" s ");
                    //started=true;
                    }
                    if (precision.ordinal() >= DatePart.ms.ordinal()) {
                        sb.append(ms).append(" ms");
                    }
                }
            }
        }
        return sb.toString();
    }
    private static DecimalFormat $TF2$ = new DecimalFormat("#00");
    private static DecimalFormat $TF3$ = new DecimalFormat("#000");

    public static String formatPeriodShort(long period, DatePart precision) {
        StringBuffer sb = new StringBuffer();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);
        if (precision.ordinal() >= DatePart.h.ordinal()) {
            if (h > 0) {
                sb.append($TF2$.format(h));
                started = true;
            }
            if (precision.ordinal() >= DatePart.mn.ordinal()) {
                if (mn > 0 || started) {
                    if (sb.length() > 0) {
                        sb.append(":");
                    }
                    sb.append($TF2$.format(mn));
                    started = true;
                }
                if (precision.ordinal() >= DatePart.s.ordinal()) {
                    if (sb.length() > 0) {
                        sb.append(":");
                    }
                    if (s > 0 || started) {
                        sb.append($TF2$.format(s));
                    //started=true;
                    }
                    if (precision.ordinal() >= DatePart.ms.ordinal()) {
                        if (sb.length() > 0) {
                            sb.append(":");
                        }
                        sb.append($TF3$.format(ms));
                    }
                }
            }
        }
        return sb.toString();
    }

    public String formatPeriodFixed(DatePart min,DatePart max) {
        return formatPeriodFixed(getTime(),min,max);
    }

    public static String formatPeriodFixed(long period, DatePart min,DatePart max) {
        StringBuffer sb = new StringBuffer();
        boolean started = false;
        int h = (int) (period / (1000L * 60L * 60L));
        int mn = (int) ((period % (1000L * 60L * 60L)) / 60000L);
        int s = (int) ((period % 60000L) / 1000L);
        int ms = (int) (period % 1000L);
        int mino = min.ordinal();
        int maxo = max.ordinal();
        if (mino >= DatePart.h.ordinal()) {
            if (h > 0 || maxo<=DatePart.h.ordinal()) {
                sb.append($TF2$.format(h));
                started = true;
            }
            if (mino >= DatePart.mn.ordinal()) {
                if (mn > 0 || started || maxo<=DatePart.mn.ordinal()) {
                    if (sb.length() > 0) {
                        sb.append(":");
                    }
                    sb.append($TF2$.format(mn));
                    started = true;
                }
                if (mino >= DatePart.s.ordinal()) {
                    if (s > 0 || started || maxo<=DatePart.s.ordinal()) {
                        if (sb.length() > 0) {
                            sb.append(":");
                        }
                        sb.append($TF2$.format(s));
                    //started=true;
                    }
                    if (mino >= DatePart.ms.ordinal()) {
                        if (sb.length() > 0) {
                            sb.append(".");
                        }
                        sb.append($TF3$.format(ms));
                    }
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return formatPeriod(getTime());
    }

    public String toString(DatePart precision) {
        return formatPeriod(getTime(), precision);
    }
}

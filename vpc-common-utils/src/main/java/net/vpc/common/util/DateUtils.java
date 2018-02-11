package net.vpc.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date setDateOnly(Date base) {
        Calendar c = Calendar.getInstance();
        c.setTime(base);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        try {
            return base.getClass().getConstructor(Long.TYPE).newInstance(c.getTime().getTime());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date setTimeOnly(Date base) {
        Calendar c = Calendar.getInstance();
        c.setTime(base);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.YEAR, 1900);
        try {
            return base.getClass().getConstructor(Long.TYPE).newInstance(c.getTime().getTime());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date addMonths(Date base, int value) {
        return add(base, Calendar.MONTH, value);
    }

    public static Date addWeeks(Date base, int value) {
        return add(base, Calendar.WEEK_OF_YEAR, value);
    }

    public static Date addDaysOfYear(Date base, int value) {
        return add(base, Calendar.DAY_OF_YEAR, value);
    }

    public static Date addHoursOfDay(Date base, int value) {
        return add(base, Calendar.HOUR_OF_DAY, value);
    }

    public static Date addMinutes(Date base, int value) {
        return add(base, Calendar.MINUTE, value);
    }

    public static Date addSeconds(Date base, int value) {
        return add(base, Calendar.SECOND, value);
    }

    public static Date add(Date base, int calendarField, int value) {
        Calendar c = Calendar.getInstance();
        c.setTime(base);
        c.add(calendarField, value);
        try {
            return base.getClass().getConstructor(Long.TYPE).newInstance(c.getTime().getTime());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Date set(Date base, int calendarField, int value) {
        Calendar c = Calendar.getInstance();
        c.setTime(base);
        c.set(calendarField, value);
        try {
            return base.getClass().getConstructor(Long.TYPE).newInstance(c.getTime().getTime());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.time;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author thevpc
 */
public class MutableDate {

    private Class baseTime = java.util.Date.class;
    private Calendar value;

    public MutableDate(Calendar value) {
        this.value = value == null ? Calendar.getInstance() : value;
    }

    public MutableDate() {
        this(Calendar.getInstance());
    }

    public MutableDate(long timeInMilis) {
        this(Calendar.getInstance());
        this.value.setTimeInMillis(timeInMilis);
    }

    public MutableDate(java.util.Date date) {
        this(Calendar.getInstance());
        if (date != null) {
            this.value.setTime(date);
            this.baseTime = value.getClass();
        }
    }

    public int getYear() {
        return value.get(Calendar.YEAR);
    }

    public MutableDate setYear(int v) {
        value.set(Calendar.YEAR, v);
        return this;
    }

    public MutableDate addYears(int v) {
        value.add(Calendar.YEAR, v);
        return this;
    }

    public int getMonth() {
        return value.get(Calendar.MONTH) + 1;
    }

    public MonthEnum getMonthEnum() {
        return MonthEnum.values()[value.get(Calendar.MONTH)];
    }

    public MutableDate setMonth(MonthEnum v) {
        return setMonth(v.ordinal() + 1);
    }

    public MutableDate setMonth(int v) {
//        checkInterval(v, "month", Calendar.JANUARY+1, Calendar.DECEMBER+1);
        value.set(Calendar.MONTH, v - 1);
        return this;
    }

    public MutableDate addMonths(int v) {
        value.add(Calendar.MONTH, v);
        return this;
    }

    public MutableDate rollMonths(int v) {
        value.roll(Calendar.MONTH, v);
        return this;
    }

    public int getWeekOfYear() {
        return value.get(Calendar.WEEK_OF_YEAR);
    }

    public MutableDate setWeekOfYear(int v) {
        value.set(Calendar.WEEK_OF_YEAR, v);
        return this;
    }

    public MutableDate addWeeksOfYear(int v) {
        value.add(Calendar.WEEK_OF_YEAR, v);
        return this;
    }

    public MutableDate rollWeeksOfYear(int v) {
        value.roll(Calendar.WEEK_OF_YEAR, v);
        return this;
    }

    public int getWeekOfMonth() {
        return value.get(Calendar.WEEK_OF_MONTH);
    }

    public MutableDate setWeekOfMonth(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.WEEK_OF_MONTH, v);
        return this;
    }

    public MutableDate addWeeksOfMonth(int v) {
        value.add(Calendar.WEEK_OF_MONTH, v);
        return this;
    }

    public MutableDate rollWeeksOfMonth(int v) {
        value.roll(Calendar.WEEK_OF_MONTH, v);
        return this;
    }

    public int getDayOfWeek() {
        return value.get(Calendar.DAY_OF_WEEK);
    }

    public DayOfWeekEnum getDayOfWeekEnum() {
        return DayOfWeekEnum.values()[value.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public MutableDate setDayOfWeek(DayOfWeekEnum v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.DAY_OF_WEEK, v.ordinal() + 1);
        return this;
    }

    public MutableDate setDayOfWeek(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.DAY_OF_WEEK, v);
        return this;
    }

    public MutableDate addDaysOfWeek(int v) {
        value.add(Calendar.DAY_OF_WEEK, v);
        return this;
    }

    public MutableDate rollDaysOfWeek(int v) {
        value.roll(Calendar.DAY_OF_WEEK, v);
        return this;
    }

    public int getDayOfMonth() {
        return value.get(Calendar.DAY_OF_MONTH);
    }

    public MutableDate setDayOfMonth(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.DAY_OF_MONTH, v);
        return this;
    }

    public MutableDate addDaysOfMonth(int v) {
        value.add(Calendar.DAY_OF_MONTH, v);
        return this;
    }

    public MutableDate rollDaysOfMonth(int v) {
        value.roll(Calendar.DAY_OF_MONTH, v);
        return this;
    }

    public int getDayOfYear() {
        return value.get(Calendar.DAY_OF_YEAR);
    }

    public MutableDate setDayOfYear(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.DAY_OF_YEAR, v);
        return this;
    }

    public MutableDate addDaysOfYear(int v) {
        value.add(Calendar.DAY_OF_YEAR, v);
        return this;
    }

    public MutableDate rollDaysOfYear(int v) {
        value.roll(Calendar.DAY_OF_YEAR, v);
        return this;
    }

    public int getDayOfWeekInMonth() {
        return value.get(Calendar.DAY_OF_YEAR);
    }

    public MutableDate setDayOfWeekInMonth(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.DAY_OF_YEAR, v);
        return this;
    }

    public MutableDate addDaysOfWeekInMonth(int v) {
        value.add(Calendar.DAY_OF_WEEK_IN_MONTH, v);
        return this;
    }

    public MutableDate rollDaysOfWeekInMonth(int v) {
        value.roll(Calendar.DAY_OF_WEEK_IN_MONTH, v);
        return this;
    }

    public int getHourOfDay() {
        return value.get(Calendar.HOUR_OF_DAY);
    }

    public MutableDate setHourOfDay(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.HOUR_OF_DAY, v);
        return this;
    }

    public MutableDate addHoursOfDay(int v) {
        value.add(Calendar.HOUR_OF_DAY, v);
        return this;
    }

    public MutableDate rollHoursOfDay(int v) {
        value.roll(Calendar.HOUR_OF_DAY, v);
        return this;
    }

    public int getHour() {
        return value.get(Calendar.HOUR);
    }

    public MutableDate setHour(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.HOUR, v);
        return this;
    }

    public MutableDate addHours(int v) {
        value.add(Calendar.HOUR, v);
        return this;
    }

    public MutableDate rollHours(int v) {
        value.roll(Calendar.HOUR, v);
        return this;
    }

    public int getMinutes() {
        return value.get(Calendar.MINUTE);
    }

    public MutableDate setMinutes(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.MINUTE, v);
        return this;
    }

    public MutableDate addMinutes(int v) {
        value.add(Calendar.MINUTE, v);
        return this;
    }

    public MutableDate rollMinutes(int v) {
        value.roll(Calendar.MINUTE, v);
        return this;
    }

    public int getSeconds() {
        return value.get(Calendar.SECOND);
    }

    public MutableDate setSeconds(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.SECOND, v);
        return this;
    }

    public MutableDate addSeconds(int v) {
        value.add(Calendar.SECOND, v);
        return this;
    }

    public MutableDate rollSeconds(int v) {
        value.roll(Calendar.SECOND, v);
        return this;
    }

    public int getMilliSeconds() {
        return value.get(Calendar.MILLISECOND);
    }

    public MutableDate setMilliSeconds(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.MILLISECOND, v);
        return this;
    }

    public MutableDate addMilliSeconds(int v) {
        value.add(Calendar.MILLISECOND, v);
        return this;
    }

    public MutableDate rollMilliSeconds(int v) {
        value.roll(Calendar.MILLISECOND, v);
        return this;
    }

    public int getZoneOffset() {
        return value.get(Calendar.ZONE_OFFSET);
    }

    public MutableDate setZoneOffset(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.ZONE_OFFSET, v);
        return this;
    }

    public int getDaylightSavingOffset() {
        return value.get(Calendar.DST_OFFSET);
    }

    public MutableDate setDaylightSavingOffset(int v) {
//        checkInterval(v, "month", Calendar.JANUARY, Calendar.DECEMBER);
        value.set(Calendar.DST_OFFSET, v);
        return this;
    }

    public boolean isAM() {
        return value.get(Calendar.AM_PM) == Calendar.AM;
    }

    public boolean isPM() {
        return value.get(Calendar.AM_PM) == Calendar.PM;
    }

    public MutableDate setAM() {
        value.set(Calendar.AM_PM, Calendar.AM);
        return this;
    }

    public MutableDate setPM() {
        value.set(Calendar.AM_PM, Calendar.PM);
        return this;
    }

    public MutableDate setDate(int year, int month, int day) {
        value.set(Calendar.DAY_OF_MONTH, day);
        value.set(Calendar.MONTH, month - 1);
        value.set(Calendar.YEAR, year);
        return this;
    }

    public MutableDate setTime(int hourOfDay, int minutes, int seconds, int milliSeconds) {
        value.set(Calendar.HOUR_OF_DAY, hourOfDay);
        value.set(Calendar.MINUTE, minutes);
        value.set(Calendar.SECOND, seconds);
        value.set(Calendar.MILLISECOND, milliSeconds);
        return this;
    }

    public MutableDate setTime(int hourOfDay, int minutes, int seconds) {
        value.set(Calendar.HOUR_OF_DAY, hourOfDay);
        value.set(Calendar.MINUTE, minutes);
        value.set(Calendar.SECOND, seconds);
        value.set(Calendar.MILLISECOND, 0);
        return this;
    }

    public MutableDate clear() {
        clearDate();
        clearTime();
        return this;
    }

    public MutableDate clearDate() {
        value.set(Calendar.DAY_OF_MONTH, 1);
        value.set(Calendar.MONTH, 0);
        value.set(Calendar.YEAR, 1900);
        return this;
    }

    public MutableDate clearMinutes() {
        value.set(Calendar.MINUTE, 0);
        value.set(Calendar.SECOND, 0);
        value.set(Calendar.MILLISECOND, 0);
        return this;
    }

    public MutableDate clearSeconds() {
        value.set(Calendar.SECOND, 0);
        value.set(Calendar.MILLISECOND, 0);
        return this;
    }

    public MutableDate clearMilliSeconds() {
        value.set(Calendar.MILLISECOND, 0);
        return this;
    }

    public MutableDate clearTime() {
        value.set(Calendar.HOUR_OF_DAY, 0);
        value.set(Calendar.MINUTE, 0);
        value.set(Calendar.SECOND, 0);
        value.set(Calendar.MILLISECOND, 0);
        return this;
    }

    private void checkInterval(int value, String name, int min, int max) {
        if (value < min) {
            throw new IllegalArgumentException(name + " should not be less than " + min);
        }
        if (value > max) {
            throw new IllegalArgumentException(name + " should not be greater than " + max);
        }
    }

    public Date getStdDateTime() {
        return value.getTime();
    }

    public <T extends java.util.Date> T getDateTime(Class<T> type) {
        try {
            return (T) baseTime.getConstructor(Long.TYPE).newInstance(value.getTimeInMillis());
        } catch (Exception e) {
            return (T) value.getTime();
        }
    }

    public java.util.Date getDateTime() {
        try {
            return (java.util.Date) baseTime.getConstructor(Long.TYPE).newInstance(value.getTimeInMillis());
        } catch (Exception e) {
            return new java.util.Date(value.getTime().getTime());
        }
    }

    public final long getTimeInMillis() {
        return value.getTimeInMillis();
    }

    public int compareTo(java.util.Date anotherDate) {
        return value.getTime().compareTo(anotherDate);
    }

    public int compareTo(MutableDate anotherDate) {
        return value.compareTo(anotherDate.value);
    }

    public int compareTo(long anotherDate) {
        return Long.compare(value.getTimeInMillis(), anotherDate);
    }

    public boolean before(java.util.Date anotherDate) {
        return value.getTime().before(anotherDate);
    }

    public boolean before(MutableDate anotherDate) {
        return value.before(anotherDate.value);
    }

    public boolean before(long anotherDate) {
        return Long.compare(value.getTimeInMillis(), anotherDate) < 0;
    }

    public boolean after(java.util.Date anotherDate) {
        return value.getTime().after(anotherDate);
    }

    public boolean after(MutableDate anotherDate) {
        return value.after(anotherDate.value);
    }

    public boolean after(long anotherDate) {
        return Long.compare(value.getTimeInMillis(), anotherDate) > 0;
    }

    public MutableDate copy() {
        return new MutableDate(getDateTime());
    }

    public MutableDate addField(int field, int value) {
        this.value.add(field, value);
        return this;
    }

    public MutableDate rollField(int field, int value) {
        this.value.roll(field, value);
        return this;
    }

    public int getField(int field) {
        return this.value.get(field);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.cron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 *
 * @author vpc
 */
public class Cron {

    private static NumberPatternAlways ALWAYS = new NumberPatternAlways();
    private NumberPattern minutes = ALWAYS;
    private NumberPattern hours = ALWAYS;
    private NumberPattern days = ALWAYS;
    private NumberPattern months = ALWAYS;
    private NumberPattern daysOfWeek = ALWAYS;

    public Cron(String pattern) {
        if (pattern == null) {
            pattern = "";
        }
        int pos = 0;
        for (String item : pattern.trim().split("[ ]+")) {
            pos++;
            switch (pos) {
                case 1: {
                    minutes = parseNumber(item, s -> parseInt(s, 0, 59));
                    break;
                }
                case 2: {
                    hours = parseNumber(item, s -> parseInt(s, 0, 23));
                    break;
                }
                case 3: {
                    days = parseNumber(item, s -> parseInt(s, 1, 31));
                    break;
                }
                case 4: {
                    months = parseNumber(item, s -> {
                        switch (s.toLowerCase()) {
                            case "jan":
                                return 1;
                            case "feb":
                                return 2;
                            case "mar":
                                return 3;
                            case "apr":
                                return 4;
                            case "may":
                                return 5;
                            case "jun":
                                return 6;
                            case "jul":
                                return 7;
                            case "aug":
                                return 8;
                            case "sep":
                                return 9;
                            case "oct":
                                return 10;
                            case "nov":
                                return 11;
                            case "dec":
                                return 12;
                        }
                        return parseInt(s, 1, 12);
                    }
                    );
                    break;
                }
                case 5: {
                    daysOfWeek = parseNumber(item, s -> {
                        switch (s.toLowerCase()) {
                            case "sun":
                                return 0;
                            case "mon":
                                return 1;
                            case "tue":
                                return 2;
                            case "wed":
                                return 3;
                            case "thu":
                                return 4;
                            case "fri":
                                return 5;
                            case "sat":
                                return 6;
                        }
                        return parseInt(s, 0, 7) % 7;
                    });
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return minutes.toString()
                + " " + hours.toString()
                + " " + days.toString()
                + " " + months.toString()
                + " " + daysOfWeek.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.minutes);
        hash = 19 * hash + Objects.hashCode(this.hours);
        hash = 19 * hash + Objects.hashCode(this.days);
        hash = 19 * hash + Objects.hashCode(this.months);
        hash = 19 * hash + Objects.hashCode(this.daysOfWeek);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cron other = (Cron) obj;
        if (!Objects.equals(this.minutes, other.minutes)) {
            return false;
        }
        if (!Objects.equals(this.hours, other.hours)) {
            return false;
        }
        if (!Objects.equals(this.days, other.days)) {
            return false;
        }
        if (!Objects.equals(this.months, other.months)) {
            return false;
        }
        if (!Objects.equals(this.daysOfWeek, other.daysOfWeek)) {
            return false;
        }
        return true;
    }

    public boolean accept(Date date) {
        Calendar i = Calendar.getInstance();
        i.setTime(date);
        if (!minutes.accept(i.get(Calendar.MINUTE))) {
            return false;
        }
        if (!hours.accept(i.get(Calendar.HOUR_OF_DAY))) {
            return false;
        }
        if (!days.accept(i.get(Calendar.DAY_OF_MONTH))) {
            return false;
        }
        if (!months.accept(i.get(Calendar.MONTH) + 1)) {
            return false;
        }
        if (!days.accept(i.get(Calendar.DAY_OF_WEEK) - 1)) {
            return false;
        }
        return true;
    }

    public int parseInt(String s, int min, int max) {
        int x = Integer.parseInt(s);
        if (x < min || x > max) {
            throw new IllegalArgumentException("Invalid intervall " + x + " <> [" + min + "," + max + "]");
        }
        return x;
    }

    public static class Intervall implements Comparable<Intervall> {

        int from;
        int to;

        public Intervall(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int compareTo(Intervall o) {
            int x = Integer.compare(from, o.from);
            if (x != 0) {
                return x;
            }
            x = Integer.compare(to, o.to);
            if (x != 0) {
                return x;
            }
            return 0;
        }

        public boolean accept(int x) {
            return x >= from && x <= to;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + this.from;
            hash = 79 * hash + this.to;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Intervall other = (Intervall) obj;
            if (this.from != other.from) {
                return false;
            }
            if (this.to != other.to) {
                return false;
            }
            return true;
        }

    }

    private static NumberPattern parseNumber(String r, Function<String, Integer> parser) {
        if (r == null) {
            r = "*";
        }
        r = r.trim();
        Set<Integer> mods = new HashSet<Integer>();
        Set<Integer> items = new HashSet<Integer>();
        List<Intervall> intervalls = new ArrayList<Intervall>();
        for (String diffs : r.split(",")) {
            if (diffs.startsWith("*/")) {
                mods.add(parser.apply(diffs.substring("*/".length())));
            } else {
                String[] o = diffs.split("-");
                switch (o.length) {
                    case 0:
                        return ALWAYS;
                    case 1: {
                        String y = o[0].trim();
                        if (y.isEmpty() || y.equals("*")) {
                            return ALWAYS;
                        }
                        int i = parser.apply(y);
                        items.add(i);
                        break;
                    }
                    case 2: {
                        String a = o[0].trim();
                        if (a.isEmpty() || a.equals("*")) {
                            return ALWAYS;
                        }
                        String b = o[1].trim();
                        if (b.isEmpty() || b.equals("*")) {
                            return ALWAYS;
                        }
                        int i = parser.apply(a);
                        int j = parser.apply(b);
                        if (j < i) {
                            throw new IllegalArgumentException("invalid intervall " + diffs);
                        }
                        intervalls.add(new Intervall(i, j));
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unsupported pattern " + diffs);
                    }
                }
            }
        }
        return new SimpleNumberPattern(
                items.toArray(new Integer[0]),
                mods.toArray(new Integer[0]),
                intervalls.toArray(new Intervall[0])
        );
    }

    private static interface NumberPattern {

        boolean accept(int s);
    }

    private static class NumberPatternAlways implements NumberPattern {

        public boolean accept(int s) {
            return true;
        }

        @Override
        public int hashCode() {
            return 7;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NumberPatternAlways other = (NumberPatternAlways) obj;
            return true;
        }

        @Override
        public String toString() {
            return "*";
        }

    }

    public static class SimpleNumberPattern implements NumberPattern {

        private Set<Integer> mods = new HashSet<Integer>();
        private Set<Integer> items = new HashSet<Integer>();
        private List<Intervall> intervalls = new ArrayList<Intervall>();

        public SimpleNumberPattern(Integer[] i, Integer[] m, Intervall[] j) {
            items.addAll(Arrays.asList(i));
            mods.addAll(Arrays.asList(m));
            intervalls.addAll(Arrays.asList(j));
            intervalls.sort(null);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Integer item : items) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(item);
            }
            for (Integer item : mods) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append("*/").append(item);
            }
            for (Intervall item : intervalls) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(item.from).append("-").append(item.to);
            }
            return sb.toString();
        }

        @Override
        public boolean accept(int s) {
            if (items.contains(s)) {
                return true;
            }
            for (Integer mod : mods) {
                if ((s % mod) == 0) {
                    return true;
                }
            }
            for (Intervall ii : intervalls) {
                if (ii.accept(s)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.mods);
            hash = 53 * hash + Objects.hashCode(this.items);
            hash = 53 * hash + Objects.hashCode(this.intervalls);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SimpleNumberPattern other = (SimpleNumberPattern) obj;
            if (!Objects.equals(this.mods, other.mods)) {
                return false;
            }
            if (!Objects.equals(this.items, other.items)) {
                return false;
            }
            if (!Objects.equals(this.intervalls, other.intervalls)) {
                return false;
            }
            return true;
        }

    }
}

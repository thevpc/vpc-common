package net.thevpc.common.strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern2 {

    private final Pattern patternObj;

    public static Pattern2 compile(String regex) {
        return new Pattern2(Pattern.compile(regex));
    }

    public static Pattern2 compile(String regex, int flags) {
        return new Pattern2(Pattern.compile(regex, flags));
    }

    public static String quote(String regex) {
        return Pattern.quote(regex);
    }

    public static Pattern2 quotePattern(String regex) {
        return compile(Pattern.quote(regex));
    }

    public static boolean matches(String regex, CharSequence input) {
        return Pattern.matches(regex, input);
    }

    private Pattern2(Pattern pattern) {
        this.patternObj = pattern;
    }

//    private static Set<String> extractRegexpGroupNames(String pattern) {
//        Pattern p = Pattern.compile("\\?\\<(?<n>[a-zA-Z0-9-_]+)\\>");
//        Set<String> all = new HashSet<String>();
//        Matcher m = p.matcher(pattern);
//        while (m.find()) {
//            all.add(m.group("n"));
//        }
//        return all;
//    }
    public Matcher2 matcher(String s) {
        return new Matcher2(
                patternObj.matcher(s)
        );
    }

    public static class Matcher2 {

        Matcher m;

        public Matcher2(Matcher m) {
            this.m = m;
        }

        public boolean find() {
            return m.find();
        }

        public boolean find(int start) {
            return m.find(start);
        }

        public boolean hasAnchoringBounds() {
            return m.hasAnchoringBounds();
        }

        public boolean hasTransparentBounds() {
            return m.hasTransparentBounds();
        }

        public boolean hitEnd() {
            return m.hitEnd();
        }

        public boolean lookingAt() {
            return m.lookingAt();
        }

        public boolean matches() {
            return m.matches();
        }

        public boolean requireEnd() {
            return m.requireEnd();
        }

        public int groupCount() {
            return m.groupCount();
        }

        public Matcher2 reset() {
            m.reset();
            return this;
        }

//        public Stream<MatchResult> results() {
//            return m.results();
//        }
        public String group() {
            return m.group();
        }

        public String group(int index) {
            return m.group(index);
        }

        public String group(String name) {
            return m.group(name);
        }

        public int start() {
            return m.start();
        }

        public int start(int index) {
            return m.start(index);
        }

        public int start(String name) {
            return m.start(name);
        }

        public int end() {
            return m.end();
        }

        public int end(int index) {
            return m.end(index);
        }

        public int end(String name) {
            return m.end(name);
        }

        public String buildString(String str) {
            return StringUtils.replaceDollarPlaceHolders(str, new StringConverter() {
                @Override
                public String convert(String str) {
                    return m.group(str);
                }
            });
        }
    }

}

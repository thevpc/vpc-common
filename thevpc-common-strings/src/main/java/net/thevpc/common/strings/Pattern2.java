package net.thevpc.common.strings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern2 {

    private final Pattern patternObj;
    private final String[] subExpressions;

    private static Pattern SUB_PATTERN = Pattern.compile("(£(?<index>[0-9]+))|(£\\((?<name>[a-zA-Z][a-zA-Z0-9]*)\\))|(£((?<name2>[a-zA-Z][a-zA-Z0-9]*)))");

    /**
     * create a pattern that
     *
     * @param text raw text (no need to escape) that includes
     * @param subPatterns
     * @return
     */
    public static Pattern2 compile2(String text, String... subPatterns) {
        Matcher matcher = SUB_PATTERN.matcher(text);
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        LinkedHashSet<String> names = new LinkedHashSet<>();
        boolean withIndex = false;
        boolean withName = false;
        int subPatternIndex = 0;
        while (matcher.find()) {
            sb.append(Pattern.quote(text.substring(pos, matcher.start())));
            String subPatternName = null;
            String subPatternExpr = null;
            if (matcher.group("index") != null) {
                if (withName) {
                    throw new IllegalArgumentException("cannot match name and index");
                }
                withIndex = true;
                int ii = Integer.parseInt(matcher.group("index"));
                if(ii<=0 || ii>subPatterns.length){
                    throw new IllegalArgumentException("invvalid index. must be >=1 and <="+subPatterns.length);
                }
                subPatternName = "SPN" + (ii);
                subPatternExpr = subPatterns[ii-1];
            } else if (matcher.group("name") != null) {
                if (withIndex) {
                    throw new IllegalArgumentException("cannot match name and index");
                }
                withName = true;
                subPatternName = matcher.group("name");
                if (names.contains(subPatternName)) {
                    throw new IllegalArgumentException("cannot reuse name " + subPatternName);
                }
                names.add(subPatternName);
                subPatternExpr = subPatterns[subPatternIndex];
                subPatternIndex++;
            } else if (matcher.group("name2") != null) {
                if (withIndex) {
                    throw new IllegalArgumentException("cannot match name and index");
                }
                withName = true;
                subPatternName = matcher.group("name2");
                if (names.contains(subPatternName)) {
                    throw new IllegalArgumentException("cannot reuse name " + subPatternName);
                }
                names.add(subPatternName);
                subPatternExpr = subPatterns[subPatternIndex];
                subPatternIndex++;
            } else {
                throw new IllegalArgumentException("Unexpected");
            }
            sb.append("(?<").append(subPatternName).append(">(");
            sb.append(subPatternExpr);
            sb.append("))");
            pos = matcher.end();
        }
        sb.append(Pattern.quote(text.substring(pos)));
        List<String> all = new ArrayList<>();
        if (withIndex) {
            for (int i = 0; i < subPatterns.length; i++) {
                all.add("SPN" + (i+1));
            }
        } else {
            all.addAll(names);
        }
        return new Pattern2(Pattern.compile(sb.toString()), all.toArray(new String[all.size()]));
    }

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
        this.subExpressions = new String[0];
    }

    private Pattern2(Pattern pattern, String[] subExpressions) {
        this.patternObj = pattern;
        this.subExpressions = subExpressions;
    }

    @Override
    public String toString() {
        return patternObj.toString();
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
    
    public boolean matches(String s) {
        return matcher(s).matches();
    }
    
    public Matcher2 matcher(String s) {
        return new Matcher2(
                patternObj.matcher(s),
                this
        );
    }

    public Iterable<Matcher2> allMatches(String a) {
        return new Iterable<Matcher2>() {
            @Override
            public Iterator<Matcher2> iterator() {
                return new Iterator<Matcher2>() {
                    boolean ok = false;
                    Matcher2 e;

                    {
                        e = matcher(a);
                    }

                    @Override
                    public boolean hasNext() {
                        ok = e.find();
                        return ok;
                    }

                    @Override
                    public Matcher2 next() {
                        return e;
                    }
                };
            }
        };
    }

    public static class Matcher2 {

        Matcher m;
        Pattern2 p;

        public Matcher2(Matcher m, Pattern2 p) {
            this.m = m;
            this.p = p;
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

        public String subPuttern(int index) {
            return group(p.subExpressions[index-1]);
        }

        public String subPuttern(String name) {
            return group(name);
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

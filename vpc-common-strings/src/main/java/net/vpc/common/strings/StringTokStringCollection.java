package net.vpc.common.strings;

import java.util.*;

class StringTokStringCollection implements StringCollection {
    protected Collection<String> values;
    private String separators;
    private StringConverter converter;

    public StringTokStringCollection(Collection<String> data, String separators, StringConverter converter) {
        if (data == null) {
            throw new NullPointerException();
        }
        this.values = data;
        this.separators = separators;
        this.converter = converter;
    }

    public String[] split(String value) {
        if (value != null && !value.isEmpty()) {
            StringTokenizer st = new StringTokenizer(separators);
            List<String> all = new ArrayList<>();
            while (st.hasMoreTokens()) {
                String rewrite = rewrite(st.nextToken());
                if (rewrite != null) {
                    all.add(rewrite);
                }
            }
            return all.toArray(new String[all.size()]);
        }
        return new String[0];
    }

    protected String rewrite(String value) {
        if (converter != null) {
            return converter.convert(value);
        }
        return value;
    }

    public void add(String value) {
        Collections.addAll(values, split(value));
    }

    public void remove(String value) {
        for (String s : split(value)) {
            values.remove(s);
        }
    }

    public String concat(String[] values) {
        String sep = String.valueOf(separators.charAt(0));
        StringBuilder sb = new StringBuilder();
        for (Object x : values) {
            if (x != null) {
                String v = (x instanceof String) ? ((String) x) : String.valueOf(x);
                if (!StringUtils.isEmpty(v)) {
                    if (sb.length() > 0) {
                        sb.append(sep);
                    }
                    sb.append(v);
                }
            }
        }
        return sb.toString();
    }

    public String toString() {
        return concat(values.toArray(new String[values.size()]));
    }

    @Override
    public List<String> getValues() {
        return new ArrayList<>(values);
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public boolean contains(String value) {
        return values.contains(rewrite(value));
    }
}

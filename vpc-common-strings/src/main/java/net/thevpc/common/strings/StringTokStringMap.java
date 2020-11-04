package net.thevpc.common.strings;

import java.util.*;

class StringTokStringMap implements StringCollection {
    protected Map<String, String> values;
    private String separators;
    private StringConverter keyConverter;
    private StringConverter valueConverter;

    public StringTokStringMap(Map<String, String> values, String separators, StringConverter keyConverter, StringConverter valueConverter) {
        if (values == null) {
            throw new NullPointerException();
        }
        this.values = values;
        this.separators = separators;
        this.keyConverter = keyConverter;
        this.valueConverter = valueConverter;
    }

    public Map<String, String> split(String value) {
        if (value != null && !value.isEmpty()) {
            StringTokenizer st = new StringTokenizer(value,separators);
            Map<String, String> all = new HashMap<>();
            while (st.hasMoreTokens()) {
                String key0 = st.nextToken();
                String key = rewriteKey(key0);
                String val = rewriteValue(key0);
                if (key != null) {
                    all.put(key, val);
                }
            }
            return all;
        }
        return Collections.EMPTY_MAP;
    }

    protected String rewriteKey(String value) {
        if (keyConverter != null) {
            return keyConverter.convert(value);
        }
        return value;
    }

    protected String rewriteValue(String value) {
        if (valueConverter != null) {
            return valueConverter.convert(value);
        }
        return value;
    }

    public void add(String value) {
        values.putAll(split(value));
    }

    public void remove(String value) {
        for (String s : split(value).keySet()) {
            values.remove(s);
        }
    }

    public String toString() {
        return concat(values.values().toArray(new String[values.size()]));
    }

    public String concat(String[] values) {
        String sep = String.valueOf(separators.charAt(0));
        StringBuilder sb = new StringBuilder();
        for (Object x : values) {
            if (x != null) {
                String v = (x instanceof String) ? ((String) x) : String.valueOf(x);
                if (!StringUtils.isBlank(v)) {
                    if (sb.length() > 0) {
                        sb.append(sep);
                    }
                    sb.append(v);
                }
            }
        }
        return sb.toString();
    }

    @Override
    public List<String> getValues() {
        return new ArrayList<>(values.values());
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
        return values.containsKey(rewriteKey(value));
    }

}

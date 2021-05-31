package net.thevpc.common.i18n;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

class InternalFmtStr implements Str {

    private String value;
    private Object[] params;

    InternalFmtStr(String value, Object... params) {
        this.value = value == null ? "" : value.trim();
        this.params = Arrays.copyOf(params, params.length);
    }

    @Override
    public String value() {
        return MessageFormat.format(value, params);
    }

    @Override
    public boolean is18n() {
        return false;
    }

    @Override
    public String toString() {
        return value();
    }

    @Override
    public String value(I18n n, Locale locale) {
        return value();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.value);
        hash = 47 * hash + Arrays.deepHashCode(this.params);
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
        final InternalFmtStr other = (InternalFmtStr) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Arrays.deepEquals(this.params, other.params)) {
            return false;
        }
        return true;
    }

}

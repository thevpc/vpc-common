package net.thevpc.common.i18n;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

class InternalI18nStrFormatted implements Str {

    private String value;
    private Object[] params;

    InternalI18nStrFormatted(String value, Object... params) {
        this.value = value == null ? "" : value.trim();
        this.params = Arrays.copyOf(params, params.length);
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean is18n() {
        return true;
    }

    @Override
    public String toString() {
        String str = value();
        return MessageFormat.format(str, params);
    }

    @Override
    public String value(I18n n, Locale locale) {
        String str = n.locale(locale).getString(value());
        Object[] newParams = Arrays.copyOf(params, params.length);
        for (int i = 0; i < newParams.length; i++) {
            Object object = newParams[i];
            if (object instanceof Str) {
                newParams[i] = ((Str) object).value(n,locale);
            }
        }
        return MessageFormat.format(str, newParams);
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
        final InternalI18nStrFormatted other = (InternalI18nStrFormatted) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Arrays.deepEquals(this.params, other.params)) {
            return false;
        }
        return true;
    }

}

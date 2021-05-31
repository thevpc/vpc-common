package net.thevpc.common.i18n;

import java.util.Locale;
import java.util.Objects;

class InternalI18nStr implements Str {
    private String value;

    InternalI18nStr(String value) {
        this.value = value == null ? "" : value.trim();
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
        return String.valueOf("<i18n>:" + value);
    }

    @Override
    public String value(I18n n, Locale locale) {
        return n.locale(locale).getString(value());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalI18nStr that = (InternalI18nStr) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

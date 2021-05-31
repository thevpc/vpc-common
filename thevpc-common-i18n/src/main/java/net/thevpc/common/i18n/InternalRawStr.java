package net.thevpc.common.i18n;

import java.util.Locale;
import java.util.Objects;

class InternalRawStr implements Str {
    private String value;

    InternalRawStr(String value) {
        this.value = value == null ? "" : value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean is18n() {
        return false;
    }

    @Override
    public String value(I18n n, Locale locale) {
        return value();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalRawStr that = (InternalRawStr) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

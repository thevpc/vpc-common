package net.thevpc.common.i18n;

import java.util.Locale;

public interface Str {

    static Str of(int s) {
        return of(String.valueOf(s));
    }

    static Str of(float s) {
        return of(String.valueOf(s));
    }

    static Str of(long s) {
        return of(String.valueOf(s));
    }

    static Str of(double s) {
        return of(String.valueOf(s));
    }

    static Str of(char s) {
        return of(String.valueOf(s));
    }

    static Str of(boolean s) {
        return of(String.valueOf(s));
    }

    static Str of(byte s) {
        return of(String.valueOf(s));
    }

    static Str of(short s) {
        return of(String.valueOf(s));
    }

    static Str empty() {
        return of("");
    }

    static Str of(String s) {
        return new InternalRawStr(s);
    }

    static Str i18n(String s) {
        return new InternalI18nStr(s);
    }

    static Str i18nfmt(String s, Object... messageParams) {
        return new InternalI18nStrFormatted(s, messageParams);
    }

    static Str fmt(String s, Object... messageParams) {
        return new InternalI18nStrFormatted(s, messageParams);
    }

    String value();

    String value(I18n n, Locale locale);

    boolean is18n();

    default boolean isEmpty() {
        return value().isEmpty();
    }

}

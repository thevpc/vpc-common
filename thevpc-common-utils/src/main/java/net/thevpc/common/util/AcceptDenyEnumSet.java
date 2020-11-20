package net.thevpc.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

public class AcceptDenyEnumSet<T extends Enum<T>> {
    private Class<T> enumType;
    private EnumSet<T> accept;
    private EnumSet<T> deny;

    public AcceptDenyEnumSet(Class<T> enumType) {
        this.enumType = enumType;
        accept = EnumSet.noneOf(enumType);
        deny = EnumSet.noneOf(enumType);
    }

    public static <T extends Enum<T>> AcceptDenyEnumSet<T> of(Class<T> enumType) {
        return new AcceptDenyEnumSet<>(enumType);
    }

    public AcceptDenyEnumSet<T> accept(T t) {
        accept.add(t);
        deny.remove(t);
        return this;
    }

    public AcceptDenyEnumSet<T> deny(T t) {
        deny.add(t);
        accept.remove(t);
        return this;
    }

    public AcceptDenyEnumSet<T> acceptAll(Collection<T> t) {
        accept.addAll(t);
        deny.removeAll(t);
        return this;
    }

    public AcceptDenyEnumSet<T> denyAll(Collection<T> t) {
        deny.addAll(t);
        accept.removeAll(t);
        return this;
    }

    public AcceptDenyEnumSet<T> acceptAll(T... t) {
        accept.addAll(Arrays.asList(t));
        return this;
    }

    public AcceptDenyEnumSet<T> denyAll(T... t) {
        deny.addAll(Arrays.asList(t));
        deny.removeAll(Arrays.asList(t));
        return this;
    }

    public AcceptDenyEnumSet<T> removeAccept(T t) {
        accept.remove(t);
        return this;
    }

    public AcceptDenyEnumSet<T> removeDeny(T t) {
        deny.remove(t);
        return this;
    }

    public boolean isAccept(T t) {
        if (deny.contains(t) && !accept.contains(t)) {
            return false;
        }
        if (accept.contains(t) && !deny.contains(t)) {
            return true;
        }
        if (deny.size() == 0 && accept.size() == 0) {
            return true;
        }
        if (deny.size() > 0 && accept.size() == 0) {
            return true;
        }
        if (accept.size() > 0 && deny.size() == 0) {
            return false;
        }
        throw new IllegalArgumentException("Ambiguous situation. Both accept and deny are defined");
    }
}

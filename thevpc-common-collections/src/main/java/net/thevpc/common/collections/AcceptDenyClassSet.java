package net.thevpc.common.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class AcceptDenyClassSet<T> {
    private Class<T> baseType;
    private ClassMap<Boolean> acceptOrDeny;
    private int accepted = 0;
    private int denied = 0;

    public AcceptDenyClassSet(AcceptDenyClassSet<T> other) {
        this(other.baseType);
        for (Map.Entry<Class, Boolean> e : other.acceptOrDeny.entrySet()) {
            if (e.getValue()) {
                accept(e.getKey());
            } else {
                deny(e.getKey());
            }
        }
    }

    public Class<T> getBaseType() {
        return baseType;
    }

    public AcceptDenyClassSet(Class<T> base) {
        this.baseType = base;
        acceptOrDeny = new ClassMap<>(base, Boolean.class);
    }

    public AcceptDenyClassSet<T> accept(Class<? extends T> t) {
        Boolean b = acceptOrDeny.get(t);
        if (b == null) {
            acceptOrDeny.put(t, true);
            accepted++;
        } else if (b.booleanValue()) {
            //
        } else {
            acceptOrDeny.put(t, true);
            denied--;
            accepted++;
        }
        return this;
    }

    public AcceptDenyClassSet<T> deny(Class<? extends T> t) {
        Boolean b = acceptOrDeny.get(t);
        if (b == null) {
            acceptOrDeny.put(t, false);
            denied++;
        } else if (b.booleanValue()) {
            acceptOrDeny.put(t, true);
            denied++;
            accepted--;
        } else {
            //
        }
        return this;
    }

    public AcceptDenyClassSet<T> acceptAll(Class<? extends T>... t) {
        acceptAll(Arrays.asList(t));
        return this;
    }

    public AcceptDenyClassSet<T> acceptAll(Collection<Class<? extends T>> t) {
        for (Class<? extends T> t1 : t) {
            accept(t1);
        }
        return this;
    }

    public AcceptDenyClassSet<T> denyAll(Class<? extends T>... t) {
        denyAll(Arrays.asList(t));
        return this;
    }

    public AcceptDenyClassSet<T> denyAll(Collection<Class<? extends T>> t) {
        for (Class<? extends T> t1 : t) {
            deny(t1);
        }
        return this;
    }

    public AcceptDenyClassSet<T> removeAccept(Class<? extends T> t) {
        Boolean b = acceptOrDeny.get(t);
        if (b != null && b.booleanValue()) {
            acceptOrDeny.remove(t);
        }
        return this;
    }

    public AcceptDenyClassSet<T> removeDeny(Class<? extends T> t) {
        Boolean b = acceptOrDeny.get(t);
        if (b != null && !b.booleanValue()) {
            acceptOrDeny.remove(t);
        }
        return this;
    }

    public boolean isAccept(Class<? extends T> t) {
        Boolean b = acceptOrDeny.get(t);
        if (b != null) {
            return b;
        }
        if (accepted == 0 && denied == 0) {
            return true;
        }
        if (accepted > 0 && denied == 0) {
            return false;
        }
        if (accepted == 0 && denied > 0) {
            return true;
        }
        throw new IllegalArgumentException("Ambiguous situation. Both accept and deny are defined");
    }
}

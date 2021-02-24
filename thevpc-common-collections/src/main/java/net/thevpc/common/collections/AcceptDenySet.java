package net.thevpc.common.collections;

import java.util.*;

public class AcceptDenySet<T> {
    private Map<T,Boolean> acceptOrDeny;
    private int accepted = 0;
    private int denied = 0;

    public AcceptDenySet() {
        this.acceptOrDeny =new HashMap<>();
    }

    public AcceptDenySet<T> accept(T t) {
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

    public AcceptDenySet<T> deny(T t) {
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

    public AcceptDenySet<T> acceptAll(T... t) {
        acceptAll(Arrays.asList(t));
        return this;
    }

    public AcceptDenySet<T> acceptAll(Collection<T> t) {
        for (T t1 : t) {
            accept(t1);
        }
        return this;
    }

    public AcceptDenySet<T> denyAll(T... t) {
        denyAll(Arrays.asList(t));
        return this;
    }

    public AcceptDenySet<T> denyAll(Collection<T> t) {
        for (T t1 : t) {
            deny(t1);
        }
        return this;
    }



    public AcceptDenySet<T> removeAccept(T t) {
        Boolean b = acceptOrDeny.get(t);
        if (b != null && b.booleanValue()) {
            acceptOrDeny.remove(t);
        }
        return this;
    }

    public AcceptDenySet<T> removeDeny(T t) {
        Boolean b = acceptOrDeny.get(t);
        if (b != null && !b.booleanValue()) {
            acceptOrDeny.remove(t);
        }
        return this;
    }

    public boolean isAccept(T t){
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

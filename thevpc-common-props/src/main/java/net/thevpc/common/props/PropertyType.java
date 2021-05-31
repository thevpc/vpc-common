package net.thevpc.common.props;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PropertyType {

    private static Map<String, PropertyType> simpleCache = new HashMap<>();

    private String name;
    private PropertyType[] args;

    public PropertyType(String name, PropertyType[] args) {
        this.name = name;
        this.args = args;
    }

    public Class getTypeClass() {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public static PropertyType of(Class cls) {
        String tname = cls.getName();
        PropertyType n = simpleCache.get(tname);
        if (n != null) {
            return n;
        }
        n = new PropertyType(tname, new PropertyType[0]);
        simpleCache.put(tname, n);
        return n;
    }
    public static PropertyType of(Class cls, Class... others) {
        PropertyType[] rr=new PropertyType[others.length];
        for (int i = 0; i < rr.length; i++) {
            rr[i]=PropertyType.of(others[i]);
        }
        return of(cls,rr);
    }

    public static PropertyType of(Class cls, PropertyType... others) {
        String tname = cls.getName();
        PropertyType n = simpleCache.get(tname);
        if (n == null) {
            n = new PropertyType(tname, new PropertyType[0]);
            simpleCache.put(tname, n);
        }
        if (others.length == 0) {
            return n;
        }
        return new PropertyType(tname, others);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PropertyType that = (PropertyType) o;
        return Objects.equals(name, that.name)
                && Arrays.equals(args, that.args);
    }

    public String getName() {
        return name;
    }

    public PropertyType[] getArgs() {
        return args.length == 0 ? args : Arrays.copyOf(args, args.length);
    }

    @Override
    public String toString() {
        if (args.length == 0) {
            return name;
        }
        return name + "<"
                + Arrays.stream(args).map(x -> x.toString()).collect(Collectors.joining(","))
                + ">";
    }
}

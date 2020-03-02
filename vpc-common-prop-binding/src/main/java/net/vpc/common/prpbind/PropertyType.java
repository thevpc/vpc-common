package net.vpc.common.prpbind;

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

    public static PropertyType of(Class cls,PropertyType... other) {
        String tname = cls.getName();
        PropertyType n = simpleCache.get(tname);
        if (n != null) {
            return n;
        }
        n = new PropertyType(tname, new PropertyType[0]);
        simpleCache.put(tname, n);
        if(other.length==0){
            return n;
        }
        return new PropertyType(tname,other);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name);
        result = 31 * result + Arrays.hashCode(args);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyType that = (PropertyType) o;
        return Objects.equals(name, that.name) &&
                Arrays.equals(args, that.args);
    }

    public String getName() {
        return name;
    }

    public PropertyType[] getArgs() {
        return args.length==0?args:Arrays.copyOf(args,args.length);
    }

    @Override
    public String toString() {
        if(args.length==0){
            return name;
        }
        return name+"<"
                +Arrays.stream(args).map(x->x.toString()).collect(Collectors.joining(","))
                +">";
    }
}

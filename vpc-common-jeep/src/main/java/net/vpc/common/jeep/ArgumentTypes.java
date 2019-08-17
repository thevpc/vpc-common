package net.vpc.common.jeep;

import java.util.Arrays;
import java.util.Objects;

public class ArgumentTypes {

    private final Class[] types;
    private final boolean varArgs;

    public ArgumentTypes(Class[] types, boolean varArgs) {
        this.types = types;
        this.varArgs = varArgs;
        if (varArgs) {
            if (types.length == 0) {
                throw new IllegalArgumentException("Expected Array");
            }
            if (!types[types.length - 1].isArray()) {
                throw new IllegalArgumentException("Expected Array");
            }
        }
    }

    public Class[] getTypes() {
        return Arrays.copyOf(types, types.length);
    }

    public boolean isVarArgs() {
        return varArgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArgumentTypes that = (ArgumentTypes) o;
        return varArgs == that.varArgs &&
                Arrays.equals(types, that.types);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(varArgs);
        result = 31 * result + Arrays.hashCode(types);
        return result;
    }
}

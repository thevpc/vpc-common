package net.vpc.common.jeep.core;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.util.JTypeUtils;

public abstract class AbstractJConverter implements JConverter {
    private JTypePattern from;
    private JTypePattern to;
    private double weight;

    public AbstractJConverter(Class from, Class to, double weight, JTypes type) {
        this(
                JTypePattern.of(type.forName(from.getName()))
                , JTypePattern.of(type.forName(to.getName())),
                weight
        );
    }
    public AbstractJConverter(JTypePattern from, JTypePattern to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public AbstractJConverter(JType from, JType to, double weight) {
        this.from = JTypePattern.of(from);
        this.to = JTypePattern.of(to);
        this.weight = weight;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public JTypePattern originalType() {
        return from;
    }

    @Override
    public JTypePattern targetType() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractJConverter)) return false;

        AbstractJConverter that = (AbstractJConverter) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        return to != null ? to.equals(that.to) : that.to == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"{" + JTypeUtils.str(originalType()) + "->" + JTypeUtils.str(targetType()) + "}";
    }
}

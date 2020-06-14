package net.vpc.common.jeep.core;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.util.JTypeUtils;

public abstract class AbstractJConverter implements JConverter {
    private JTypeOrLambda from;
    private JTypeOrLambda to;
    private double weight;

    public AbstractJConverter(Class from, Class to, double weight, JTypes type) {
        this(
                JTypeOrLambda.of(type.forName(from.getName()))
                ,JTypeOrLambda.of(type.forName(to.getName())),
                weight
        );
    }
    public AbstractJConverter(JTypeOrLambda from, JTypeOrLambda to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public AbstractJConverter(JType from, JType to, double weight) {
        this.from = JTypeOrLambda.of(from);
        this.to = JTypeOrLambda.of(to);
        this.weight = weight;
    }

    @Override
    public double weight() {
        return weight;
    }

    @Override
    public JTypeOrLambda originalType() {
        return from;
    }

    @Override
    public JTypeOrLambda targetType() {
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

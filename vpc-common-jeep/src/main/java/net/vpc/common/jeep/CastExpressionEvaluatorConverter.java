package net.vpc.common.jeep;

public class CastExpressionEvaluatorConverter implements ExpressionEvaluatorConverter {
    private Class from;
    private Class to;

    public CastExpressionEvaluatorConverter(Class from, Class to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Class getOriginalType() {
        return from;
    }

    @Override
    public Class getTargetType() {
        return to;
    }

    @Override
    public Object convert(Object value) {
        JeepPlatformUtils.toBoxingType(from).cast(value);
        return JeepPlatformUtils.toBoxingType(to).cast(value);
    }

    @Override
    public String toString() {
        return "Converter{" +
                from +
                "->" + to +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CastExpressionEvaluatorConverter that = (CastExpressionEvaluatorConverter) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        return to != null ? to.equals(that.to) : that.to == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}

package net.vpc.common.jeep;

import net.vpc.common.jeep.util.IntIterator;
import net.vpc.common.jeep.util.IntIteratorBuilder;

import java.util.ArrayList;
import java.util.List;

public class ArgsPossibility {
    private int size;
    private Class[] original;
    private Class[] converted;
    private int[] weights;
    private ExpressionEvaluatorConverter[] converters;

    public ArgsPossibility(Class[] original, Class[] converted, ExpressionEvaluatorConverter[] converters) {
        this.original = original;
        this.converted = converted;
        this.converters = converters;
    }

    public static ArgsPossibility[] allOf(Class[] original, ImplicitSupplier supplier) {
        if(original.length==0){
            return new ArgsPossibility[0];
        }
        ExpressionEvaluatorConverter[][] converters2 = new ExpressionEvaluatorConverter[original.length][];
        int[] max = new int[converters2.length];
        for (int i = 0; i < original.length; i++) {
            ExpressionEvaluatorConverter[] cc = supplier.get(original[i]);
            converters2[i] = cc;
            max[i] = converters2[i].length;
        }
        IntIterator it = IntIteratorBuilder.iter().to(max).breadthFirst();
        List<ArgsPossibility> possibilities = new ArrayList<>();
        while (it.hasNext()) {
            int[] c = it.next();
            Class[] converted = new Class[original.length];
            ExpressionEvaluatorConverter[] convertersOk = new ExpressionEvaluatorConverter[original.length];
            for (int i = 0; i < converted.length; i++) {
                ExpressionEvaluatorConverter cc = converters2[i][c[i]];
                convertersOk[i] = cc;
                converted[i] = cc==null?original[i]:cc.getTargetType();
            }
            possibilities.add(new ArgsPossibility(original, converted, convertersOk));
        }
        return possibilities.toArray(new ArgsPossibility[0]);
    }

    public int getWeight(int index) {
        return weights[index];
    }

    public Class getOriginal(int index) {
        return original[index];
    }

    public Class[] getConverted() {
        return converted;
    }
    public Class getConverted(int index) {
        return converted[index];
    }

    public ExpressionEvaluatorConverter getConverter(int index) {
        return converters[index];
    }

    public ExpressionEvaluatorConverter[] getConverters() {
        return converters;
    }

    interface ImplicitSupplier {
        ExpressionEvaluatorConverter[] get(Class clazz);
    }
}

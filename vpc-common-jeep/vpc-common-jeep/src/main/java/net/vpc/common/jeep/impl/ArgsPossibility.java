package net.vpc.common.jeep.impl;

import net.vpc.common.jeep.JConverter;
import net.vpc.common.jeep.JTypeOrLambda;
import net.vpc.common.jeep.util.IntIterator;
import net.vpc.common.jeep.util.IntIteratorBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ArgsPossibility {
    private int size;
    private JTypeOrLambda[] original;
    private JTypeOrLambda[] converted;
    private double[] weights;
    private JConverter[] converters;

    public ArgsPossibility(JTypeOrLambda[] original, JTypeOrLambda[] converted, JConverter[] converters, double[] weights) {
        this.original = original;
        this.converted = converted;
        this.converters = converters;
        this.weights = weights;
    }

    public static ArgsPossibility[] allOf(JTypeOrLambda[] original, Function<JTypeOrLambda, JConverter[]> supplier) {
        if (original.length == 0) {
            return new ArgsPossibility[]{
                    new ArgsPossibility(original, new JTypeOrLambda[0], new JConverter[0], new double[0])
            };
        }
        class JConverterAndWeight {
            JConverter converter;
            double weight;

            public JConverterAndWeight(JConverter converter, double weight) {
                this.converter = converter;
                this.weight = weight;
            }
        }
        JConverterAndWeight[][] converters2 = new JConverterAndWeight[original.length][];
        int[] max = new int[converters2.length];
        for (int i = 0; i < original.length; i++) {
            JConverter[] cc = supplier.apply(original[i]);
            if (cc == null) {
                converters2[i] = new JConverterAndWeight[]{
                        new JConverterAndWeight(null, 0)
                };
            } else {
                JConverterAndWeight[] c2 = new JConverterAndWeight[cc.length+1];
                c2[0] = new JConverterAndWeight(null, 0);
                for (int j = 0; j < cc.length; j++) {
                    if(cc[j]==null){
                        throw new IllegalArgumentException("Null converter");
                    }
                    c2[j+1] = new JConverterAndWeight(cc[j], j+1);
                }
                converters2[i] = c2;
            }
            max[i] = converters2[i].length;
        }
        IntIterator it = IntIteratorBuilder.iter().to(max).breadthFirst();
        List<ArgsPossibility> possibilities = new ArrayList<>();
        while (it.hasNext()) {
            int[] c = it.next();
            JTypeOrLambda[] converted = new JTypeOrLambda[original.length];
            JConverter[] convertersOk = new JConverter[original.length];
            double[] weights = new double[converted.length];
            for (int i = 0; i < converted.length; i++) {
                JConverterAndWeight cc = converters2[i][c[i]];
                convertersOk[i] = cc.converter;
                converted[i] = cc.converter == null ? original[i] : cc.converter.targetType();
                weights[i] = cc.weight;
            }
            possibilities.add(new ArgsPossibility(original, converted, convertersOk, weights));
        }
        return possibilities.toArray(new ArgsPossibility[0]);
    }

    public double getWeight(int index) {
        return weights[index];
    }

    public JTypeOrLambda getOriginal(int index) {
        return original[index];
    }

    public JTypeOrLambda[] getConverted() {
        return converted;
    }
    public JTypeOrLambda[] getOriginal() {
        return original;
    }

    public JTypeOrLambda getConverted(int index) {
        return converted[index];
    }

    public JConverter getConverter(int index) {
        return converters[index];
    }

    public JConverter[] getConverters() {
        return converters;
    }

    public boolean isIdentity() {
        if (converters != null) {
            for (JConverter converter : converters) {
                if (converter != null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "ArgsPossibility{" +
                Arrays.toString(original) +
                "->" +
                Arrays.toString(converted) +
                " weighted:" + weight() +
                '}';
    }

    public double weight() {
        double d = 0;
        if (converters != null) {
            for (JConverter converter : converters) {
                if (converter != null) {
                    d += converter.weight();
                }
            }
        }
        for (double weight : weights) {
            d += weight;
        }
        return d;
    }

}

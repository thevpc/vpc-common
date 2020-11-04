package net.thevpc.common.lanterna;

import java.util.Arrays;

public class VBarSingleModel {
    double[] values=new double[0];
    String[] labels=new String[0];
    private int minValuesCount = -1;

    public VBarSingleModel() {
    }

    public void setMinValuesCount(int minValuesCount) {
        this.minValuesCount = minValuesCount;
        while (values.length < minValuesCount) {
            addValue(0);
        }
    }

    public int size(){
        return values.length;
    }

    public void rollValue(double value, String label) {
        if (minValuesCount > 0) {
            rollValue(value, label, minValuesCount);
        }
    }

    public void rollValue(double value, String label, int maxValues) {
        double[] values = new double[maxValues];
        System.arraycopy(this.values, 0, values, 1, Math.min(maxValues - 1, this.values.length - 1));
        values[0] = value;
        this.values = values;

        String[] labels = new String[maxValues];
        System.arraycopy(this.labels, 0, labels, 1, Math.min(maxValues - 1, this.labels.length - 1));
        labels[0] = label;
        this.labels = labels;
    }

    public int getMinValuesCount() {
        return minValuesCount;
    }

    public void addValue(double value) {
        addValue(value, null);
    }

    public void addValue(double value, String label) {
        values = Arrays.copyOf(values, values.length + 1);
        labels = Arrays.copyOf(labels, labels.length + 1);
        setValue(values.length - 1, value, label);
    }

    public void setValue(int index, double value, String label) {
        values[index] = value;
        labels[index] = label;
    }

    public void ensureSizeShrinkHead(int size) {
        if (size < values.length) {
            double[] v2 = new double[size];
            System.arraycopy(values, values.length - size, v2, 0, v2.length);
            values = v2;
            String[] l2 = new String[size];
            System.arraycopy(labels, labels.length - size, l2, 0, l2.length);
            labels = l2;
        } else if (size > values.length) {
            values = Arrays.copyOf(values, size);
            labels = Arrays.copyOf(labels, size);
        }
    }

}

package net.vpc.common.util;

import java.util.Arrays;

public class DoubleArrayList {
    private double[] values=new double[0];
    private int size=0;

    public void add(int index,double value){
        ensureSize(size + 1);  // Increments modCount!!
        System.arraycopy(values, index, values, index + 1,
                size - index);
        values[index] = value;
        size++;
    }
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size);
    }

    public double remove(int index){
        rangeCheck(index);

//        modCount++;
        double oldValue = values[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(values, index + 1, values, index, numMoved);
        }
        --size;
        values[size] = 0;
        return oldValue;
    }

    public void add(double value){
        ensureSize(size+1);
        values[size++]=value;
    }

    public void ensureSize(int size){
        if(size>values.length){
            values= Arrays.copyOf(values,size);
        }
    }

    public void trimToSize(){
        if(size<values.length){
            values= Arrays.copyOf(values,size);
        }
    }

    public double[] toArray(){
        return Arrays.copyOf(values,size);
    }

    public Double[] toIntegerArray(){
        Double[] all=new Double[size];
        for (int i = 0; i < size; i++) {
            all[i]=values[i];
        }
        return all;
    }
}

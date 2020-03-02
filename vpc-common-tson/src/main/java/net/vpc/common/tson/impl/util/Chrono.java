/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.tson.impl.util;

/**
 *
 * @author vpc
 */
public class Chrono implements Comparable<Chrono>{

    public static Chrono start(String name) {
        return new Chrono(name);
    }

    private final long start;
    private long end;
    private String name;

    public Chrono(String name) {
        this.start = System.nanoTime();
        this.name=name;
    }

    public Chrono stop() {
        this.end = System.nanoTime();
        return this;
    }

    public String diff(Chrono other) {
        return String.valueOf((1.0) * nanos() / other.nanos());
    }

    @Override
    public String toString() {
        return name+" : "+String.valueOf(nanos() / 1000000000.0);
    }

    public long nanos() {
        return end - start;
    }

    public double seconds() {
        return nanos() / 1000000000.0;
    }

    @Override
    public int compareTo(Chrono o) {
        return Long.compare(nanos(),o.nanos());
    }

    public String name() {
        return name;
    }
}
